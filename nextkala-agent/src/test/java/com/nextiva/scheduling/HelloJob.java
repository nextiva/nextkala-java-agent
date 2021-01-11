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

package com.nextiva.scheduling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nextiva.scheduling.agent.ScheduledJob;
import com.nextiva.scheduling.agent.annotation.Job;


/**
 * Test job.
 */
@Job("Hello")
public class HelloJob implements ScheduledJob {
    private static Logger LOGGER = LogManager.getLogger(HelloJob.class);

    @Override
    public int executeJob(String jobParams) {
        LOGGER.debug("Executing job Hello with params: {}", jobParams);
        return 0;
    }

    @Override
    public boolean validateJob(String jobParams) {
        return jobParams.equals("TestUser1");
    }
}
