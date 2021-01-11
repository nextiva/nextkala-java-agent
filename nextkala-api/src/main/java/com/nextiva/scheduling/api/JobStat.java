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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Job Status.
 */
public class JobStat {
    private String id;
    @JsonProperty("job_id")
    private String jobId;
    // Convert to java.time.ZonedDateTime
    @JsonProperty("ran_at")
    private String rantAt;
    @JsonProperty("number_of_retries")
    private int numberOfRetries;
    private boolean success;
    // Convert to java.time.Duration
    @JsonProperty("execution_duration")
    private String executionDuration;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRantAt() {
        return rantAt;
    }

    public void setRantAt(String rantAt) {
        this.rantAt = rantAt;
    }

    public int getNumberOfRetries() {
        return numberOfRetries;
    }

    public void setNumberOfRetries(int numberOfRetries) {
        this.numberOfRetries = numberOfRetries;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExecutionDuration() {
        return executionDuration;
    }

    public void setExecutionDuration(String executionDuration) {
        this.executionDuration = executionDuration;
    }
}
