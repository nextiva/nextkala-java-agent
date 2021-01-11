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

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextiva.scheduling.agent.controller.ScheduledJobController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test validating and running a job.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ScheduledJobController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {HelloTestApplication.class})
public class HelloTest {

    public static CountDownLatch postSignal = new CountDownLatch(2);

    private static final String HELLO_JOB = "/private/v1/scheduledJob/Hello";
    private static final String VALIDATE_HELLO_JOB = "/private/v1/scheduledJob/Hello/validate";
    private static final String MEDIA_TYPE = "application/json";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    public void testExecuteJob() throws Exception {
        mockServer.expect(ExpectedCount.manyTimes(), method(HttpMethod.POST)).andRespond(signalWithNoContent());
        String body = "TestUser1";
        MvcResult result = mockMvc.perform(post(HELLO_JOB)
                .contentType(MEDIA_TYPE)
                .header("NextKala-RunId", "12345")
                .content(body))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();
        boolean finished = postSignal.await(2, TimeUnit.SECONDS);
        assertTrue("Two Post requests were not received", finished);
    }

    @Test
    public void testValidateJob() throws Exception {
        String body = "TestUser1";
        MvcResult result = mockMvc.perform(post(VALIDATE_HELLO_JOB)
                .contentType(MEDIA_TYPE)
                .header("NextKala-RunId", "12345")
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull("No response returned", response);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    public void testInvalidRequester() throws Exception {
        String body = "TestUser2";
        MvcResult result = mockMvc.perform(post(VALIDATE_HELLO_JOB)
                .contentType(MEDIA_TYPE)
                .header("NextKala-RunId", "12345")
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull("No response returned", response);
        assertFalse(Boolean.parseBoolean(response));
    }

    private static DefaultResponseCreator signalWithNoContent() {
        return new SignalWithNoContentResponseCreator();
    }

    private static class SignalWithNoContentResponseCreator extends DefaultResponseCreator {
        public SignalWithNoContentResponseCreator() {
            super(HttpStatus.NO_CONTENT);
        }

        @Override
        public ClientHttpResponse createResponse(@Nullable ClientHttpRequest request) throws IOException {
            HelloTest.postSignal.countDown();
            return super.createResponse(request);
        }
    }
}
