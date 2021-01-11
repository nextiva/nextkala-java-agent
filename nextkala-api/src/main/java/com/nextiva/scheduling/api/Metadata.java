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
 * Job metadata.
 */
public class Metadata {
    @JsonProperty("success_count")
    private int successCount;
    // Convert to java.time.ZonedDateTime
    @JsonProperty("last_success")
    private String lastSuccess;
    @JsonProperty("error_count")
    private int errorCount;
    // Convert to java.time.ZonedDateTime
    @JsonProperty("last_error")
    private String lastError;
    // Convert to java.time.ZonedDateTime
    @JsonProperty("last_attempted_run")
    private String lastAttemptedRun;
    @JsonProperty("number_of_finished_runs")
    private int numberOfFinishedRuns;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public String getLastSuccess() {
        return lastSuccess;
    }

    public void setLastSuccess(String lastSuccess) {
        this.lastSuccess = lastSuccess;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getLastAttemptedRun() {
        return lastAttemptedRun;
    }

    public void setLastAttemptedRun(String lastAttemptedRun) {
        this.lastAttemptedRun = lastAttemptedRun;
    }

    public int getNumberOfFinishedRuns() {
        return numberOfFinishedRuns;
    }

    public void setNumberOfFinishedRuns(int numberOfFinishedRuns) {
        this.numberOfFinishedRuns = numberOfFinishedRuns;
    }
}
