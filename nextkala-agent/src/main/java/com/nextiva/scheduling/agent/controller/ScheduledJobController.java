/*
 * Licensed to Nextiva under one or more contributor license agreements. See
 * the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * Nextiva licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.nextiva.scheduling.agent.controller;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.nextiva.scheduling.agent.ScheduledJob;
import com.nextiva.scheduling.agent.SchedulerClient;
import com.nextiva.scheduling.api.enums.JobStatus;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Dispatches scheduled jobs.
 */
@RestController
public class ScheduledJobController {

    private static final Logger LOGGER = LogManager.getLogger(ScheduledJobController.class);
    private static final int TIMEOUT = 15;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private Map<String, ScheduledJob> scheduledJobMap;

    @Autowired
    private SchedulerClient schedulingClient;

    @PreDestroy
    private void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            executorService.shutdownNow();
            try {
                executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
            } catch (final InterruptedException ie) {
                LOGGER.warn("Scheduled jobs may not have completed");
            }
        }
    }

    /**
     * Handle a scheduled job request.
     * @param jobName The name of the job.
     * @param jobParams A map of parameters to pass to the job.
     * @param jobId The job's id.
     * @param runId The execution id.
     * @return The response entity.
     */
    @ApiOperation(value = "Run a scheduled job", tags = { "scheduler", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The job was dispatched"),
            @ApiResponse(code = 404, message = "Job Not Found") })
    @PostMapping(value = "/private/v1/scheduledJob/{jobName}", produces = { "application/json" })
    public ResponseEntity<Void> runJob(@ApiParam(value = "The job name") @PathVariable String jobName,
            @ApiParam(value = "The job parameters") @Valid @RequestBody(required = false)
                    String jobParams,
            @RequestHeader(name = "NextKala-JobId", required = true) String jobId,
            @RequestHeader(name = "NextKala-RunId", required = true) String runId) {
        ScheduledJob scheduledJob = scheduledJobMap.get(jobName);
        if (scheduledJob != null) {
            Map<String, String> threadContext = ThreadContext.getContext();
            executorService.submit(new Agent(jobName, jobParams, jobId, runId, scheduledJob, schedulingClient,
                    threadContext));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            LOGGER.error("Unable to locate job named {}", jobName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Vaalidate job creation.
     * @param jobName The name of the job.
     * @param jobParams A map of parameters to pass to the job.
     * @return The response entity.
     */
    @ApiOperation(value = "Validate a job", tags = { "scheduler", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The job was dispatched"),
            @ApiResponse(code = 404, message = "Job Not Found") })
    @PostMapping(value = "/private/v1/scheduledJob/{jobName}/validate", produces = { "application/json" })
    public ResponseEntity<Boolean> validateJob(@ApiParam(value = "The job name") @PathVariable String jobName,
            @ApiParam(value = "The job parameters") @Valid @RequestBody(required = false)
                    String jobParams) {
        ScheduledJob scheduledJob = scheduledJobMap.get(jobName);
        if (scheduledJob != null) {
            boolean result = scheduledJob.validateJob(jobParams);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            LOGGER.error("Unable to locate job named {}", jobName);
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }
    }

    private static class Agent implements Runnable {
        private final String jobParams;
        private final ScheduledJob job;
        private final String jobId;
        private final String executionId;
        private final String jobName;
        private final SchedulerClient client;
        private final Map<String, String> threadContext;

        public Agent(String name, String jobParams, String jobId, String executionId, ScheduledJob job,
                SchedulerClient client, Map<String, String> threadContext) {
            this.jobParams = jobParams;
            this.jobId = jobId;
            this.executionId = executionId;
            this.job = job;
            this.jobName = name;
            this.client = client;
            this.threadContext = threadContext;
        }

        public void run() {
            try {
                LOGGER.info("Starting job {}", jobName);
                ThreadContext.putAll(threadContext);
                client.updateJobExecutionStatus(jobId, executionId, JobStatus.RUNNING);
                int status = job.executeJob(jobId, executionId, jobParams);
                client.updateJobExecutionStatus(jobId, executionId,
                        status == 0 ? JobStatus.SUCCESS : JobStatus.FAILED);
                LOGGER.info("Job {} completed with status {}", jobName, status);
            } catch (Throwable throwable) {
                LOGGER.error("Job {} failed due to {}: {}", jobName, throwable.getClass().getName(),
                        throwable.getMessage());
                try {
                    client.updateJobExecutionStatus(jobId, executionId, JobStatus.FAILED);
                } catch (Exception ex) {
                    LOGGER.warn("Unable to update job status for job execution {} due to {}", executionId,
                            ex.getMessage());
                }
            } finally {
                ThreadContext.clearMap();
            }
        }
    }
}
