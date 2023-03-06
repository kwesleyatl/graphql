# DGS

## What is Netflix DGS?

`Netflix DGS (Domain Graph Service)` is a GraphQL server framework written in Kotlin based on Spring Boot and is designed to have minimal external dependencies aside from Spring framework.

DGS also comes with code-gen plugin to generate Java or Kotlin code from GraphQL Schema.

## DGS framework dependencies

1. Maven
```aidl
<dependency>
   <groupId>com.netflix.graphql.dgs</groupId>
   <artifactId>graphql-dgs-spring-boot-starter</artifactId>
   <!-- Set the latest framework version! -->
   <version>3.11.0</version>
</dependency>
```
2. Gradle
```aidl
repositories {
    mavenCentral()
}dependencies {
    implementation "com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:latest.release"
}
```



## More Info

1. [GraphQL Server with Netflix DGS and Spring Boot](https://medium.com/geekculture/graphql-server-with-netflix-dgs-and-spring-boot-2b2f1b42d163)
2. 1. [GraphQL Server with Spring Boot]()