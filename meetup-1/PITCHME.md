### Chicago Microservices Meetup
<br><br>
#### Hosted by NVISIA
<br>
![NVISIA adopt.build.enable](./meetup-1/assets/images/nvisia-abe-rect.jpg)
---
### How We Enable Successful Meetups
<br>

1. NVISIA = Passion && Hard Core Engineering
2. Technology Consulting Experience
<br><br>
---  
### Meetup Goals:
<br>
We want to build a channel to collaborate and share our experiences with microservices
<br> 
  - Benefits of a microservice architecture
  - The tools, frameworks, and practices that make working with microservices easier
  - The business benefits of microservices
<br><br>
---  
### WE NEED YOU  
<br><br>
If you have ideas on topics to discuss or wish to present,
please come talk to us afterwards.
<br><br>
---
### Issue Root Cause Analysis in Microservices Architecture
<br>
#### Chicago Microservices Meetup
<br><br>
---  
### Microservices Overview
<br>
![Microservices Everywhere!](./meetup-1/assets/images/ms-everywhere.jpg)
<br><br>
---  
### What are Microservices?
<br>
![Microservices For Ants!](./meetup-1/assets/images/ms-ants.jpg)
<br><br>
---
### What are Microservices?
<br>
“Microservice architecture (MSA) is an approach to building software systems that decomposes business domain models into smaller, consistent, bounded contexts implemented by services.”
<br>
Excerpt From: Christian Posta. “Microservices for Java Developers.”
---
### Why use Microservices?
<br>
With a Microservices architecture, we see improvements in:
- Scalability
- Deployability
- Maintainability
- Failure Isolation *

Note:
- Maintainability
    - In theory, smaller teams
    - Can hide implementation details
        - Loose coupling allows for change/replacement with minimal impact to system   
    - Easier for developers to get working
    - Easier to introduce changes in a controlled manner
- can scale independently vs Monolithic applications
- microservices are independently deployable
- Failure Isolation
    - (within the boundaries of domain)
---
### Design Considerations
<br><br>
- **Service interactions - synchronous or event-based?**
- **Appropriate level of decomposition of domain into bounded contexts following single responsibility principle**
- **Design for Faults**
- **Distributed Systems Management**
---
### Foo Service
<br><br>
---
```
$ curl localhost:9001/hello

curl: (7) Failed to connect to
localhost port 9001: Connection refused
```
@[1](request "/actuator/health")
@[3-4](FAIL!)

Note:
talk about service, and attempt to hit /hello endpoint
---
### Spring Boot Actuator
<br>
Provides production-ready features to our microservices for:
- monitoring
- gathering metrics
- analyzing traffic

---
```
$ curl localhost:9001/actuator/health

curl: (7) Failed to connect to localhost port 8080:
Connection refused
```
@[1](request "/actuator/health")
@[3-4](FAIL!)

---
```
$ curl localhost:9001/actuator/health

{
    "status" : "UP"
}
```
@[1](request "/actuator/health")
@[3-5](Service is Up!)
---
```
$ curl localhost:8080/hello

{"id":"b3596e3d-b43e-4abb-bdee-b094392ac7d8"}
```
@[1](request "/hello")
@[3](inspect my response)
Note:
talk about service, and attempt to hit /hello endpoint, but get uuid id
---
```
$ curl localhost:8080/actuator/info

{
    "build": {
        "version":"1.0",
        "artifact":"demo",
        "name":"demo",
        "group":"'com.nvisia.meetup.microservices'",
        "time":"2018-03-13T16:00:56.870Z"
        }
}
```
---
@title[Spring Boot Actuator]
---?code=./meetup-1/demo/build.gradle&lang=groovy&title=build.gradle&size=auto

@[74, 77, 95](Add actuator as a dependency)

---

@title[Spring Boot Admin]
### What can we do with this information?

Note:
Who really wants to look at text? Dazzle me!
---
@title[Spring Boot Admin]
### Spring Boot Admin

Spring Boot Admin is a simple application to manage and monitor your Spring Boot Applications.
- Vue.js application on top of Actuator endpoints
---
@title[Spring Boot Admin]
### Spring Boot Admin:
#### Adding dependencies

---?code=./meetup-1/spring-boot-admin-server/build.gradle&lang=groovy&title=build.gradle&size=auto

@[49, 54, 57](Add "spring-boot-admin-starter-server" as a dependency)

---
@title[Spring Boot Admin]
### Spring Boot Admin:
#### Configuring

---?code=./meetup-1/spring-boot-admin-server/src/main/java/com/nvisia/meetup/microservices/bootadmin/Application.java&lang=Java&title=Spring%20Boot%20Admin&size=80%

@[32](Annotate application with @EnableAdminServer in Application.java)
---
@title[Spring Boot Admin]
### Spring Boot Admin
#### Foo Service Setup
---?code=./meetup-1/demo/src/main/resources/application.yaml&lang=yml&title=Spring%20Boot%20Admin

@[4-7, 13-18](Set url for admin server in demo application properties & expose endpoints)

Note:
Pause here for first part of demo showing foo services registering with Spring Boot Admin.
---
### Tracing sources of errors

Note:
Talk about exceptions, latency, etc.
---
@title[Spring Cloud Sleuth]
### Spring Cloud Sleuth & Zipkin

- A distributed tracing solution for Spring Cloud that attaches a correlation identifier through requests to external systems via a `X-B3-Trace-Id` request header.
- If not present on first service call, it will be generated and propagated to other service requests
    - Spring Cloud refers to the correlation identifier as the Trace Id.
---
@title[Spring Cloud Sleuth]
### Spring Cloud Sleuth & Zipkin  
- Unique Span Id is generated for tracing activity within one service under a given Trace Id.
- Id(s) will be added to logs automatically.
---
Spring Cloud Sleuth
<br>
![Magic](./meetup-1/assets/images/magic.gif)
---?code=./meetup-1/demo/build.gradle&lang=groovy&title=Spring%20Cloud%20Sleuth&size=auto

@[74, 87-88, 95](Add "sleuth" dependencies in Foo service)

---?code=./meetup-1/demo/src/main/resources/application.yaml&lang=yml&title=Spring%20Cloud%20Sleuth

@[8-9](Set url for zipkin server in Foo service properties & expose endpoints)

---?code=./meetup-1/zipkin-server/build.gradle&lang=groovy&title=Zipkin%20Server&size=auto

@[49, 54-55, 58](Add zipkin dependencies)

---?code=./meetup-1/zipkin-server/src/main/java/com/nvisia/meetup/microservices/bootadmin/Application.java&lang=Java&title=Zipkin%20Server&size=auto

@[30](Annotate application with @EnableZipkinServer)

Note: Switch to demo of Spring-Cloud Sleuth and Zipkin.  Explain how trace id added to response and show with curl -i 
---
### Questions?

<br>

@fa[twitter gp-contact](@nvisia)

@fa[twitter gp-contact](@jcvillalta03)

@fa[twitter gp-contact](@justinjohn83)

@fa[github gp-contact](nvisia)

@fa[github gp-contact](justinjohn83)

@fa[github gp-contact](jcvillalta03)
