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

package com.nextiva.scheduling.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Job Status.
 */
public enum JobStatus {
    /**
     * Job has started.
     */
    STARTED("Started"),
    /**
     * Job is running.
     */
    RUNNING("Running"),
    /**
     * Job execution failed.
     */
    FAILED("Failed"),
    /**
     * Job execution completed successfully.
     */
    SUCCESS("Success");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }

    /**
     * Return the status value.
     * @return The String value of the status.
     */
    @JsonValue
    public String getStatus() {
        return this.value;
    }

    /**
     * Located a JobStatus from its string value.
     * @param status The string value.
     * @return The JobStatus or null if it cannot be found.
     */
    @JsonCreator
    public static JobStatus getStatus(String status) {
        for (JobStatus jobStatus : values()) {
            if (jobStatus.value.equals(status)) {
                return jobStatus;
            }
        }
        return null;
    }
}
