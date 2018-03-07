# Meetup #1: Microservice Madness

#### This repository contains 3 projects

- [Spring Boot Admin](spring-boot-admin-server/README.md)
- [Zipkin Server](zipkin-server/README.md)
- [Demo service](demo/README.md)

##### All Demos

In separate terminals start the spring boot server for `Spring Boot Admin` and `Zipkin` with `./gradlew bootRun`

In all cases we will hit the endpoint on `foo1`

http://localhost:9001/hello

###### Demo 1: Normal service operation

Run each in separate terminal windows

```
./gradlew bootRun -Dserver.port=9001 \
-Dspring.application.name=foo1 \
-Dcom.nvisia.demo.chain=foo2,foo3
```

```
./gradlew bootRun -Dserver.port=9002 \
-Dspring.application.name=foo2 \
-Dcom.nvisia.demo.chain=foo4
```

```
./gradlew bootRun -Dserver.port=9003 \
-Dspring.application.name=foo3
```

```
./gradlew bootRun -Dserver.port=9004 \
-Dspring.application.name=foo4
```


##### Demo 2: latency in a service

Introduce latency in foo2:

```
com.nvisia.demo.latency=10000
```

Command Line:
```
./gradlew -Dserver.port=9001 \
-Dspring.application.name=foo1 \
-Dcom.nvisia.demo.chain=foo2,foo3
```

```
./gradlew bootRun -Dserver.port=9002 \
-Dspring.application.name=foo2 \
-Dcom.nvisia.demo.chain=foo4 \
-Dcom.nvisia.demo.latency=10000
```

```
./gradlew bootRun -Dserver.port=9003 \
-Dspring.application.name=foo3
```

```
./gradlew bootRun -Dserver.port=9004 \
-Dspring.application.name=foo4
```

##### Demo 3: exception in a service

Introduce latency in foo2 followed by exception in
service 4

Introduce exception in foo3:
```
com.nvisia.demo.exception=true
```

Command Line:
```
./gradlew bootRun -Dserver.port=9001 \
-Dspring.application.name=foo1 \
-Dcom.nvisia.demo.chain=foo2,foo3
```

```
./gradlew bootRun -Dserver.port=9002 \
-Dspring.application.name=foo2 \
-Dcom.nvisia.demo.chain=foo4 \
-Dcom.nvisia.demo.latency=10000 \
```

```
./gradlew bootRun -Dserver.port=9003 \
-Dspring.application.name=foo3 \
-Dcom.nvisia.demo.exception=true
```

```
./gradlew bootRun -Dserver.port=9004 \
-Dspring.application.name=foo4
```
