# Spring Boot Admin Server

If you are using Spring Cloud Discovery such as Eureka, then clients will automatically register with the spring boot admin server.  Otherwise, you can bootstrap the registration with the following client

```
dependencies {
// ...
compile('de.codecentric:spring-boot-admin-starter-client:2.0.0-SNAPSHOT')
```

Run with
```
./gradlew bootRun
```
It is configured to listen on port 9000
<p>
http://localhost:9000
</p>
