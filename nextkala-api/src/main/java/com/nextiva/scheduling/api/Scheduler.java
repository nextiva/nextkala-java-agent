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

package com.nextiva.scheduling.api;

import java.util.List;

import com.nextiva.scheduling.api.JobDefinition;
import com.nextiva.scheduling.api.JobStat;
import com.nextiva.scheduling.api.enums.JobStatus;

/**
 * Class Description goes here.
 */
public interface Scheduler {

    /**
     * Add a new Job.
     * @param jobDefinition The Job definition.
     * @return The job's id.
     */
    default String addJob(JobDefinition jobDefinition) {
        return addJob(jobDefinition, null);
    }

    /**
     * Add a new Job.
     * @param jobDefinition The Job definition.
     * @param token The OAuth token.
     * @return The job's id.
     */
    String addJob(JobDefinition jobDefinition, String token);


    /**
     * Retrieve a job definition.
     * @param id The job's id.
     * @return the job's definition.
     */
    default JobDefinition getJob(String id) {
        return getJob(id, null);
    }

    /**
     * Retrieve a job definition.
     * @param id The job's id.
     * @param token The OAuth token.
     * @return the job's definition.
     */
    JobDefinition getJob(String id, String token);

    /**
     * Retrieve a remote job's parameters.
     * @param id The job's id.
     * @return the job's parameters as a String (normally JSON).
     */
    default String getJobParameters(String id) {
        return getJobParameters(id, null);
    }

    /**
     * Retrieve a remote job's parameters.
     * @param id The job's id.
     * @param token The OAuth token.
     * @return the job's parameters as a String (normally JSON).
     */
    String getJobParameters(String id, String token);

    /**
     * Save a job's run parameters.
     * @param id The job's id.
     * @param params The jobs new parameter string. Normally will be JSON.
     */
    default void setJobParameters(String id, String params) {
        setJobParameters(id, params, null);
    }

    /**
     * Save a job's run parameters.
     * @param id The job's id.
     * @param token The OAuth token.
     * @param params The jobs new parameter string. Normally will be JSON.
     */
    void setJobParameters(String id, String params, String token);

    /**
     * Start a job.
     * @param id The job's id.
     */
    default void startJob(String id) {
        startJob(id, null);
    }

    /**
     * Start a job.
     * @param id The job's id.
     * @param token The OAuth token.
     */
    void startJob(String id, String token);

    /**
     * Enable a job.
     * @param id The job's id.
     */
    default void enableJob(String id) {
        enableJob(id, null);
    }

    /**
     * Enable a job.
     * @param id The job's id.
     * @param token The OAuth token.
     */
    void enableJob(String id, String token);

    /**
     * Disable a job.
     * @param id The job's id.
     */
    default void disableJob(String id) {
        disableJob(id, null);
    }

    /**
     * Disable a job.
     * @param id The job's id.
     * @param token The OAuth token.
     */
    void disableJob(String id, String token);

    /**
     * Deletes all job definitions.
     */
    default void deleteAllJobs() {
        deleteAllJobs(null);
    }

    /**
     * Deletes all job definitions.
     * @param token The OAuth token.
     */
    void deleteAllJobs(String token);

    /**
     * Delete a job definition.
     * @param id The id of the job.
     */
    default void deleteJob(String id) {
        deleteJob(id, null);
    }

    /**
     * Delete a job definition.
     * @param id The id of the job.
     * @param token The OAuth token.
     */
    void deleteJob(String id, String token);

    /**
     * Return all job definitions.
     * @return The list of job definitions.
     */
    default List<JobDefinition> listJobs() {
        return listJobs(null);
    }

    /**
     * Return all job definitions.
     * @param token The OAuth token.
     * @return The list of job definitions.
     */
    List<JobDefinition> listJobs(String token);

    /**
     * Retrieve the exection statistics for a particular job execution.
     * @param executionId The job execution's id.
     * @return The job execution statistics or null, if the job execution cannot be located.
     */
    default JobStat getJobExecutionStats(String executionId) {
        return getJobExecutionStats(executionId, null);
    }

    /**
     * Retrieve the exection statistics for a particular job execution.
     * @param executionId The job execution's id.
     * @param token The OAuth token.
     * @return The job execution statistics or null, if the job execution cannot be located.
     */
    JobStat getJobExecutionStats(String executionId, String token);

    /**
     * Retrieve all execution stats for a job.
     * @param jobId The job's id.
     * @return A List of execution statistics.
     */
    default List<JobStat> getAllJobExecutionStats(String jobId) {
        return getAllJobExecutionStats(jobId, null);
    }

    /**
     * Retrieve all execution stats for a job.
     * @param jobId The job's id.
     * @param token The OAuth token.
     * @return A List of execution statistics.
     */
    List<JobStat> getAllJobExecutionStats(String jobId, String token);

    /**
     * Update a job's execution status.
     * @param jobId The job's id.
     * @param executionId The job execution's id.
     * @param status The new job status.
     */
    default void updateJobExecutionStatus(String jobId, String executionId, JobStatus status) {
        updateJobExecutionStatus(jobId, executionId, status, null);
    }

    /**
     * Update a job's execution status.
     * @param jobId The job's id.
     * @param executionId The job execution's id.
     * @param status The new job status.
     * @param token The OAuth token.
     */
    void updateJobExecutionStatus(String jobId, String executionId, JobStatus status, String token);
}
