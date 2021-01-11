# NextKala Java Agent

This project provides a Java client for the NextKala REST endpoints to create and manipulate jobs, 
retrieve and manage statistics for job runs including updating the status of a job execution
as it completes.

This project also provides the scaffolding required to create a Job that can be run by NextKala.

Implementing a job requires the following:
1. Create a class that implements the ScheduledJob interface and provide the Job's name by 
   annotating that class with the Job annotation.
2. Implement the validateJob method. This method will be called by the scheduler to verify
   that the calling user has permission to schedule the job. Detail on this are below.
3. Implement the executeJob method. This will be run in its own thread and must return true
   to signify that the job completed successfully.
   
Although this project contains a controller, this project is not a full web application.
It is expected that this library will be included and wired into a Spring Boot application
that includes the various job definitions as well as any other REST services that are 
desired.
   
##Security

The NextKala agent does not implement any security on its own. It expects that the user has
integrated Spring Security or something similar. Therefore any supplied tokens or credentials
are expected to be validated by the time NextKala's controller is invoked. The controller 
does not perform any permission checking. The ScheduledJob implementation is expected to 
perform any permission checking it requires.

When the validateJob method is called it will be using the token or credentials of the 
user that called NextKala's addJob endpoint. validateJob should verify that the user has 
the appropriate permissions to schedule that job.

When executeJob is called it will be using the token or credentials of the NextKala service
user. The executeJob should switch to a service user that is more appropriate for the job 
being run as the KextKala service user typically will only have permissions to execute jobs.
