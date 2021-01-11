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
import com.nextiva.scheduling.api.enums.JobType;

/**
 * Job definition.
 */

public class JobDefinition {

    private String name;
    private String id;
    // Command to run
    // e.g. "bash /path/to/my/script.sh"
    private String command;
    // Email of the owner of this job
    // e.g. "admin@example.com"
    private String owner;
    // Is this job disabled?
    private boolean disabled;
    // Jobs that are dependent upon this one will be run after this job runs.
    @JsonProperty("dependent_jobs")
    private String[] dependentJobs;

    // List of ids of jobs that this job is dependent upon.
    @JsonProperty("parent_jobs")
    private String[] parentJobs;
    // Job that gets run after all retries have failed consecutively
    @JsonProperty("on_failure_job")
    private String onFailureJob;

    // ISO 8601 String
    // e.g. "R/2014-03-08T20:00:00.000Z/PT2H"
    private String schedule;
    // Number of times to retry on failed attempt for each run.
    private int retries;
    // Duration in which it is safe to retry the Job.
    private String epsilon;
    // Convert to java.time.ZonedDateTime
    @JsonProperty("next_run_at")
    private String nextRunAt;

    // Templating delimiters, the left & right separated by space,
    // for example `{{ }}` or `${ }`.
    //
    // If this field is non-empty, then each time this
    // job is executed, Kala will template its main
    // content as a Go Template with the job itself as data.
    //
    // The Command is templated for local jobs,
    // and Url and Body in RemoteProperties.
    @JsonProperty("TemplateDelimiters")
    private String templateDelimiters;

    // If the job is disabled (or the system inoperative) and we pass
    // the scheduled run point, when the job becomes active again,
    // normally the job will run immediately.
    // With this setting on, it will not run immediately, but will wait
    // until the next scheduled run time comes along.
    @JsonProperty("resume_at_next_scheduled_time")
    private boolean resumeAtNextScheduledTime;

    // Meta data about successful and failed runs.
    private Metadata metadata;

    // Type of the job
    private JobType type;

    // Custom properties for the remote job type
    @JsonProperty("remote_properties")
    private RemoteProperties remoteProperties;

    // Says if a job has been executed right numbers of time
    // and should not been executed again in the future
    @JsonProperty("is_done")
    private boolean isDone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String[] getDependentJobs() {
        return dependentJobs;
    }

    public void setDependentJobs(String[] dependentJobs) {
        this.dependentJobs = dependentJobs;
    }

    public String[] getParentJobs() {
        return parentJobs;
    }

    public void setParentJobs(String[] parentJobs) {
        this.parentJobs = parentJobs;
    }

    public String getOnFailureJob() {
        return onFailureJob;
    }

    public void setOnFailureJob(String onFailureJob) {
        this.onFailureJob = onFailureJob;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(String epsilon) {
        this.epsilon = epsilon;
    }

    public String getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(String nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public String getTemplateDelimiters() {
        return templateDelimiters;
    }

    public void setTemplateDelimiters(String templateDelimiters) {
        this.templateDelimiters = templateDelimiters;
    }

    public boolean isResumeAtNextScheduledTime() {
        return resumeAtNextScheduledTime;
    }

    public void setResumeAtNextScheduledTime(boolean resumeAtNextScheduledTime) {
        this.resumeAtNextScheduledTime = resumeAtNextScheduledTime;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public RemoteProperties getRemoteProperties() {
        return remoteProperties;
    }

    public void setRemoteProperties(RemoteProperties remoteProperties) {
        this.remoteProperties = remoteProperties;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}