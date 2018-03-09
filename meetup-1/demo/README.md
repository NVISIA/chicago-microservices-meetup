# Demo services

#### Demonstration of a service calling one or more other services.

To start the demo service on the default port 8080 with no downstream services

```
./gradlew bootRun
```

We can then send invoke the `/hello` endpoint:

http://localhost:8080/hello


To start multiple instances of the `foo` service and have one call the others we can use the following java system properties on the `bootRun` command

##### 1) Port to run the boot server

```
-Dserver.port=9001
```

##### 2) Name of service

```
-Dspring.application.name=foo1
```

Since service discovery isn't enabled for this project, it assumes the following service/port combinations for communication purposes

```
foo1/9001
foo2/9002
foo3/9003
foo4/9004
foo5/9005
```

##### 3) Introduce latency in a service's response:

time unit is milliseconds

```
-Dcom.nvisia.demo.latency=10000
```

##### 4) Introduce a 500 error in a service's response.  If latency is also configured that will happen first before the exception.  In both cases this is a post operation to any other service calls it will make as we will see below.

Option is a boolean `true/false`

```
-Dcom.nvisia.demo.exception=true
```

##### 5) To have a service call its neighbors configure the services it should call by name separated with a comma.

E.g. Suppose we also have or will have `foo2,foo3,foo4` and want to call them in that order:

```
-Dcom.nvisia.demo.chain=foo2,foo3,foo4
```

Here is a full example that starts a new service foo1 that also calls `foo2,foo3,foo4`, introduces `10s` of latency and then also returns a `500 error`

```
./gradlew bootRun -Dserver.port=9001 \
-Dspring.application.name=foo1 \
-Dcom.nvisia.demo.chain=foo2,foo3,foo4 \
-Dcom.nvisia.demo.latency=10000 \
-Dcom.nvisia.demo.exception=true
```
