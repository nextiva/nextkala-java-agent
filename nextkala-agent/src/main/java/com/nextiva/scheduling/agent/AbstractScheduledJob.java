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

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class Description goes here.
 */
public abstract class AbstractScheduledJob implements ScheduledJob {

    private SchedulingClient schedulingClient;

    /**
     * Sets the SchedulingClient.
     * @param client The Scheduling Client.
     */
    @Autowired
    public final void setSchedulingClient(SchedulingClient client) {
        this.schedulingClient = client;
    }

    /**
     * Retrieve the scheduling client.
     * @return the SchedulingClient.
     */
    protected SchedulingClient getSchedulingClient() {
        return schedulingClient;
    }

    /**
     * Update the job's parameters.
     * @param jobId The job's id.
     * @param jobParams The new job parameters.
     */
    protected void updateJobParams(String jobId, String jobParams) {
        schedulingClient.setJobParameters(jobId, jobParams);
    }
}
