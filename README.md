> Current learning from deploying a spring boot application to AWS ECS

* After the basic configuration is created: Cluster, Task Definition, Container and Service with
  tasks running, the deployed service could not be accessed with the Public IP of the task.
*
    * solution was in the security group configuration associated to the service. it had to define
      inbound/outbound rules that would allow the service to be accessed from Internet (outside),
      and allow the service to access Internet.