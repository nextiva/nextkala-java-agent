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
 * Properties for remote job.
 */
public class RemoteProperties {
    private String url;
    private String method;
    // The object to attach to the HttpRequest.
    private String body;
    // Timeout value for the HTTP request in seconds.
    private Integer timeout;
    // A list of expected response code (e.g. [200, 201])
    @JsonProperty("expected_response_codes")
    private int []expectedResponseCodes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public int[] getExpectedResponseCodes() {
        return expectedResponseCodes;
    }

    public void setExpectedResponseCodes(int[] expectedResponseCodes) {
        this.expectedResponseCodes = expectedResponseCodes;
    }
}
