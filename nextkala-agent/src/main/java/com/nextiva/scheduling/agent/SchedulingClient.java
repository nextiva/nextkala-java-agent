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

package com.nextiva.scheduling.agent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nextiva.scheduling.api.JobDefinition;
import com.nextiva.scheduling.api.JobStat;
import com.nextiva.scheduling.api.enums.JobStatus;

/**
 * Client to access the scheduler.
 */
public class SchedulingClient {

    private static final Logger LOGGER = LogManager.getLogger(SchedulingClient.class);
    private static final String BASE_PATH = "/api/v1/";

    private static final String API_JOB_PATH = BASE_PATH + "job/";
    
    private static final String DELETE_ALL_JOBS = API_JOB_PATH + "all/";
    private static final String START_JOB = API_JOB_PATH + "start/";
    private static final String ENABLE_JOB = API_JOB_PATH + "enable/";
    private static final String DISABLE_JOB = API_JOB_PATH + "disable/";

    private final RestTemplate restTemplate;
    private final String baseUri;
    
    public SchedulingClient(RestTemplate template, String baseUri) {
        this.restTemplate = template;
        this.baseUri = baseUri;
    }

    /**
     * Add a new Job.
     * @param jobDefinition The Job definition.
     * @return The job's id.
     */
    public String addJob(JobDefinition jobDefinition) {
        LOGGER.traceEntry();
        String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH).toUriString();
        String result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(jobDefinition, headers);
            ResponseEntity<AddJobResponse> response = restTemplate.exchange(restUri, HttpMethod.POST, entity,
                    AddJobResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                AddJobResponse addJobResponse = response.getBody();
                if (addJobResponse != null) {
                    result = addJobResponse.id;
                }
            } else {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException hsce) {
            LOGGER.error("Unable to add job defintion due to: {}", hsce.getMessage());
        }
        return LOGGER.traceExit(result);
    }

    /**
     * Retrieve a job definition.
     * @param id The job's id.
     * @return the job's definition.
     */
    public JobDefinition getJob(String id) {
        LOGGER.traceEntry();
        JobDefinition result = null;
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString())).path("/")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(null, headers);
            ResponseEntity<JobDefinition> response = restTemplate.exchange(restUri, HttpMethod.GET, entity,
                    JobDefinition.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                result = response.getBody();
            } else {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to enable job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
        return result;
    }

    /**
     * Retrieve a remote job's parameters.
     * @param id The job's id.
     * @return the job's parameters as a String (normally JSON).
     */
    public String getJobParameters(String id) {
        LOGGER.traceEntry();
        String result = null;
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString()))
                    .pathSegment("params").path("/").toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(null, headers);
            ResponseEntity<String> response = restTemplate.exchange(restUri, HttpMethod.GET, entity,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                result = response.getBody();
            } else {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to enable job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
        return result;
    }

    /**
     * Save a job's run parameters.
     * @param id The job's id.
     * @param params The jobs new parameter string. Normally will be JSON.
     */
    public void setJobParameters(String id, String params) {
        LOGGER.traceEntry();
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString()))
                    .pathSegment("params").path("/").toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(restUri, HttpMethod.PUT, entity,
                    String.class);
            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to update job parameters for job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Start a job.
     * @param id The job's id.
     */
    public void startJob(String id) {
        LOGGER.traceEntry();
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + START_JOB)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString())).path("/")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(null, headers);
            ResponseEntity<Void> response = restTemplate.exchange(restUri, HttpMethod.POST, entity,
                    Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to start job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Enable a job.
     * @param id The job's id.
     */
    public void enableJob(String id) {
        LOGGER.traceEntry();
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + ENABLE_JOB)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString())).path("/")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(null, headers);
            ResponseEntity<Void> response = restTemplate.exchange(restUri, HttpMethod.POST, entity,
                    Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to enable job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Disable a job.
     * @param id The job's id.
     */
    public void disableJob(String id) {
        LOGGER.traceEntry();
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + DISABLE_JOB)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString())).path("/")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(null, headers);
            ResponseEntity<Void> response = restTemplate.exchange(restUri, HttpMethod.POST, entity,
                    Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to disable job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Deletes all job definitions.
     */
    public void deleteAllJobs() {
        LOGGER.traceEntry();
        String restUri = UriComponentsBuilder.fromUriString(baseUri + DELETE_ALL_JOBS).toUriString();
        try {
            HttpEntity<?> entity = new HttpEntity<>(null);
            ResponseEntity<Void> response = restTemplate.exchange(restUri, HttpMethod.DELETE, entity, Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Unable to delete all jobs");
            }
        } catch (HttpStatusCodeException hsce) {
            LOGGER.error("Unable to delete all jobs due to: {}", hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Delete a job definition.
     * @param id The id of the job.
     */
    public void deleteJob(String id) {
        LOGGER.traceEntry();
        String result = null;
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(id, StandardCharsets.UTF_8.toString())).toUriString();
            HttpEntity<?> entity = new HttpEntity<>(null);
            ResponseEntity<Void> response =
                    restTemplate.exchange(restUri,
                    HttpMethod.DELETE, entity, Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Unable to delete job {}", id);
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to delete job {} due to: {}", id, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    /**
     * Return all job definitions.
     * @return The list of job definitions.
     */
    public List<JobDefinition> listJobs() {
        LOGGER.traceEntry();
        String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH).toUriString();
        List<JobDefinition> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<List<JobDefinition>> response =
                    restTemplate.exchange(restUri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
            if (response.getStatusCode() == HttpStatus.OK) {
                result = response.getBody();
            } else {
                LOGGER.error("Unable to retrieve jobs");
            }
        } catch (HttpStatusCodeException hsce) {
            LOGGER.error("Unable to retrieve job due to: {}", hsce.getMessage());
        }
        LOGGER.traceExit();
        return result;
    }

    /**
     * Retrieve the exection statistics for a particular job execution.
     * @param executionId The job execution's id.
     * @return The job execution statistics or null, if the job execution cannot be located.
     */
    public JobStat getJobExecutionStats(String executionId) {
        LOGGER.traceEntry();
        JobStat result = null;
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment("executions")
                    .pathSegment(URLEncoder.encode(executionId, StandardCharsets.UTF_8.toString()))
                    .path("/").toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(restUri, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.error("Unable to retrieve job statistics for job with execution id {}", executionId);
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to get job stats due to: {}", hsce.getMessage());
        }
        return LOGGER.traceExit(result);
    }

    /**
     * Retrieve all execution stats for a job.
     * @param jobId The job's id.
     * @return A List of execution statistics.
     */
    public List<JobStat> getAllJobExecutionStats(String jobId) {
        LOGGER.traceEntry();
        List<JobStat> result = null;
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(jobId, StandardCharsets.UTF_8.toString()))
                    .pathSegment("executions").path("/").toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<List<JobStat>> response = restTemplate.exchange(restUri, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<>() {});
            if (response.getStatusCode() == HttpStatus.OK) {
                result = response.getBody();
            } else {
                LOGGER.error("Unable to retrieve job statistics for job {}", jobId);
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to get job stats due to: {}", hsce.getMessage());
        }
        LOGGER.traceExit();
        return result;
    }

    /**
     * Update a job's execution status.
     * @param executionId The job execution's id.
     * @param status The new job status.
     */
    public void updateJobExecutionStatus(String jobId, String executionId, JobStatus status) {
        LOGGER.traceEntry();
        try {
            String restUri = UriComponentsBuilder.fromUriString(baseUri + API_JOB_PATH)
                    .pathSegment(URLEncoder.encode(jobId, StandardCharsets.UTF_8.toString())).pathSegment("executions")
                    .pathSegment(URLEncoder.encode(executionId, StandardCharsets.UTF_8.toString())).path("/")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(status, headers);
            ResponseEntity<Void> response = restTemplate.exchange(restUri, HttpMethod.PUT, entity,
                    Void.class);
            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                LOGGER.error("Call to {} returned {}", restUri, response.getStatusCode());
            }
        } catch (HttpStatusCodeException | UnsupportedEncodingException hsce) {
            LOGGER.error("Unable to update job execution {} for status for job {} due to: {}",
                    executionId, jobId, hsce.getMessage());
        }
        LOGGER.traceExit();
    }

    private static class AddJobResponse {
        String id;
    }
    
}
