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

/**
 * A scheduled job.
 */
public interface ScheduledJob  {

    /**
     * Executes a job.
     * @param jobId The job's id.
     * @param executionid The id of the job run.
     * @param jobParams The parameters defined for the Job. May be a String containing a JSON object.
     * @return The completion status of the job.
     */
    int executeJob(String jobId, String executionid, String jobParams);

    /**
     * Called when a job is created to validate that the user making the request has the necessary privleges to
     * schedule this job. Normally, this should get the user's information from the Spring Security Context
     * and then check whether the user has permission to create this job.
     * @param jobParams The parameters to be passed to the job.
     * @return true if the job can be scheduled, false otherwise.
     */
    boolean validateJob(String jobParams);
}
