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

import org.springframework.context.annotation.Bean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.nextiva.scheduling.agent.SchedulerClient;

/**
 * Test Configuration.
 */
public class HelloConfiguration {

    /**
     * Creates the RestTemplate.
     * @return the RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates the Mock server.
     * @param restTemplate The RestTemplate.
     * @return the Mock server.
     */
    @Bean
    public MockRestServiceServer mockServer(RestTemplate restTemplate) {
        return MockRestServiceServer.createServer(restTemplate);
    }

    /**
     * Create the SchedulingClient.
     * @param mockServer the MockServer.
     * @param restTemplate the RestTemplate.
     * @return the SchedulingClient.
     */
    @Bean
    public SchedulerClient client(MockRestServiceServer mockServer, RestTemplate restTemplate) {
        String baseUri = "http://localhost:8888/";
        return new SchedulerClient(restTemplate, baseUri);
    }
}
