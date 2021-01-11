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
 * Job Type.
 */
public enum JobType {
    /**
     * Local Job.
     */
    LOCAL_JOB(0),
    /**
     * Remote Job.
     */
    REMOTE_JOB(1);

    private final int value;

    JobType(int value) {
        this.value = value;
    }

    /**
     * Return the JobType's integer value.
     * @return The JobType as an integer.
     */
    @JsonValue
    public int getValue() {
        return this.value;
    }

    /**
     * Locate a JobType from its integer value.
     * @param value The value.
     * @return The located JobType or null.
     */
    @JsonCreator
    public static JobType getType(int value) {
        for (JobType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

}