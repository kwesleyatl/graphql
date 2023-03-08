# DGS

## Overview
One of the most significant paradigm changes over the last few years regarding client/server communication has been [GraphQL](https://graphql.org/), an open-source query language, and runtime for manipulating APIs. We can use it to request the exact data we need and therefore limit the number of requests we need.

Netflix created a Domain Graph Service Framework (DGS) server framework to make things even easier. In this quick tutorial, we'll cover key features of the DGS Framework. We'll see how to add this framework to our app and check how its basic annotations work. To learn more about GraphQL itself, check out [Introduction to GraphQL article](https://www.baeldung.com/graphql).

## What is Netflix DGS?

`Netflix DGS (Domain Graph Service)` is a GraphQL server framework written in Kotlin based on Spring Boot and is designed to have minimal external dependencies aside from Spring framework.

DGS also comes with code-gen plugin to generate Java or Kotlin code from GraphQL Schema.

The DGS Framework (Domain Graph Service) is a GraphQL server framework for Spring Boot, developed by Netflix.

The Netflix DGS framework uses an annotation-based GraphQL Java library built on top of Spring Boot. Besides the annotation-based programming model, it provides several useful features. It allows generating source code from GraphQL schemas. Let's sum up some key features:

Features include:

    Annotation based Spring Boot programming model
    Test framework for writing query tests as unit tests
    Gradle Code Generation plugin to create types from schema
    Easy integration with GraphQL Federation
    Integration with Spring Security
    GraphQL subscriptions (WebSockets and SSE)
    File uploads
    Error handling
    Many extension points

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

We also need to add other dependencies below is a full set of different sections we need within our pom.xml (this article assumes you are using maven).

```aidl
    <properties>
        <graphql-dgs-codegen-client-core.version>5.1.14</graphql-dgs-codegen-client-core.version>
        <graphql-dgs-spring-boot-starter.version>4.9.15</graphql-dgs-spring-boot-starter.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.graphql.dgs.codegen</groupId>
            <artifactId>graphql-dgs-codegen-client-core</artifactId>
            <version>${graphql-dgs-codegen-client-core.version}</version>
        </dependency>
        <dependency>
            <groupId>com.netflix.graphql.dgs</groupId>
            <artifactId>graphql-dgs-spring-boot-starter</artifactId>
            <version>${graphql-dgs-spring-boot-starter.version}</version>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.netflix.graphql.dgs</groupId>
                <artifactId>graphql-dgs-platform-dependencies</artifactId>
                <version>4.1.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>    
    
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.deweyjose</groupId>
                <artifactId>graphqlcodegen-maven-plugin</artifactId>
                <version>1.14</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <schemaPaths>
                        <param>src/main/resources/schema/schema.graphqls</param>
                    </schemaPaths>
                    <packageName>com.bealdung.graphqldgs.generated</packageName>
                </configuration>
            </plugin>
        </plugins>
    </build>    
```

## Data Fetcher
Data fetchers are responsible for returning data for a query. ```The `@DgsQuery`, `@DgsMutation`, and `@DgsSubscription` annotations are shorthands to define data fetchers on the Query, Mutation, and Subscription types```. All mentioned annotations are equivalent to the @DgsData annotation. We can use one of these annotations on a Java method to make that method a data fetcher and define a type with a parameter.

## How to Implement Data fetcher in DGS
In order to define the DGS data fetcher, we need to create a query method in the @DgsComponent class. We want to query a list of Albums in our example, so let's mark the method with @DgsQuery:
```aidl
    @DgsQuery
    public List<Album> albums(@InputArgument String titleFilter) { //@InputArgument("titleFilter") String filter
        if(titleFilter == null) {
            return albums;
        }
        return albums.stream()
                .filter(s -> s.getTitle().contains(titleFilter))
                .collect(Collectors.toList());
    }
```

## Basic Anatomy of DGS Data Fetcher
```aidl
@DgsComponent
public class CustomerDataFetcher {

    CustomerRepository customerRepository;

    AccountRepository accountRepository;
    
    public CustomerDataFetcher(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @DgsQuery methods
        
    @DgsData methods    
    
    @DgsMutation methods    
    
    @DgsSubscription methods
}
```

### DgsComponent

It acts like @RestController annotation.

The @DgsQuery, @DgsMutation and @DgsSubscription annotations are shorthands to define datafetchers on the Query, Mutation and Subscription types

### DgsQuery

Its same as `@GetMapping` annotation for Spring MVC app.

Its a method level annotation used to make the method as a datafetcher. It has only one parameter — “field”. It will be used to perform fetch operation.

Here the parentType is `Query` — derived from the annotation itself and `field` — if specified picks from the value provided or else it will be derived from the method name.

Basically, If the field parameter is not set, the method name will be used as the field name.

```aidl
@DgsQuery(field = "customers")
public List<Customer> fetchAllCustomers() {
    return customerRepository.findAll();
}
```

```aidl
/ The parentType is "Query", the field name is derived from the method name.
@DgsQuery
public List<Show> shows() { .... }

// The parentType is "Query", the field name is explicitly specified.
@DgsQuery(field = "shows")
public List<Show> shows() { .... }
```

### DgsData

Its same as `@RequestMapping` annotation for Spring MVC app.

Its a method level annotation used to make the method as a datafetcher. It has two parameters — “parentType” and “field”.
1. `parentType` — its a type that contains field.
2. `field` — indicates the field the datafetcher is responsible for.

Point to note here, parameter `field` is an optional parameter which will be derived from method name.

We can’t define `@DgsQuery` and `@DgsMutation` in a single method, instead we can use `@DgsData` with parentType as ``Query`` or ``Mutation``.

```aidl
@DgsData(parentType = "Query", field = "shows")
public List<Show> shows() { .... }

// The "field" argument is omitted. It uses the method name as the field name.
@DgsData(parentType = "Query")
public List<Show> shows() { .... }
```

```aidl
@DgsData(parentType = "Query", field = "customers")
public List<Customer> fetchAllCustomers() {
    return customerRepository.findAll();
}

@DgsData(parentType = "Customer", field = "accounts")
public List<Account> getAllAccounts(DgsDataFetchingEnvironment dgsDataFetchingEnvironment) {
    Customer customer = dgsDataFetchingEnvironment.getSource();
    List<Account> accountList = new ArrayList<>();
    for (Account account : customer.getAccounts()) {
       Account accountResponse = accountRepository.findById(account.getAccountId()).get();
       accountList.add(accountResponse);
    }
    return accountList;
}
```
### DgsMutation

Its same as `@PostMapping`, `@PutMapping`, `@DeleteMapping` annotation for Spring MVS app.

Its a method level annotation used to make the method as a datafetcher. It has only one parameter — `field`. It will be used to perform persist/update/delete operations.

Here the parentType is `Mutation` — derived from the annotation itself and `field` — if specified picks from the value provided or else it will be derived from the method name.

```aidl
@DgsMutation
public Customer customer(CustomerInput customerInput) {
    Customer customer = Customer.builder()
            .contact(customerInput.getContact())
            .name(customerInput.getName())
            .gender(customerInput.getGender())
            .mail(customerInput.getMail())
 .accounts(mapCustomerAccounts(customerInput.getAccounts()))
            .build();
    Customer customerResponse = customerRepository.save(customer);
    return customerResponse;
}
```
OR
```aidl
@DgsMutation(filed="customer")
public Customer saveCustomer(CustomerInput customerInput) {
    Customer customer = Customer.builder()
            .contact(customerInput.getContact())
            .name(customerInput.getName())
            .gender(customerInput.getGender())
            .mail(customerInput.getMail())
 .accounts(mapCustomerAccounts(customerInput.getAccounts()))
            .build();
    Customer customerResponse = customerRepository.save(customer);
    return customerResponse;
}
```

### DgsSubscription

## Child Datafetchers
The previous example assumed that you could load a list of Show objects from your database with a single query. It wouldn't matter which fields the user included in the GraphQL query; the cost of loading the shows would be the same. What if there is an extra cost to specific fields? For example, what if loading actors for a show requires an extra query? It would be wasteful to run the extra query to load actors if the actors field doesn't get returned to the user.

In such scenarios, it's better to create a separate datafetcher for the expensive field.

```aidl
@DgsQuery
public List<Show> shows() {

    //Load shows, which doesn't include "actors"
    return shows;
}

@DgsData(parentType = "Show", field = "actors")
public List<Actor> actors(DgsDataFetchingEnvironment dfe) {

   Show show = dfe.getSource();
   actorsService.forShow(show.getId());
   return actors;
}
```

The `actors` datafetcher only gets executed when the `actors` field is included in the query. The `actors` datafetcher also introduces a new concept; the `DgsDataFetchingEnvironment`. 

#### DgsDataFetchingEnvironment
The `DgsDataFetchingEnvironment` gives access to the `context`, the query itself, data loaders, and the source object. The source object is the object that contains the field. For this example, the source is the Show object, which you can use to get the show's identifier to use in the query for actors.

Do note that the shows datafetcher is returning a list of Show, while the actors datafetcher fetches the actors for a single show. The framework executes the actors datafetcher for each Show returned by the shows datafetcher. If the actors get loaded from a database, this would now cause an `N+1 problem`. To solve the N+1 problem, you use [data loaders](https://netflix.github.io/dgs/data-loaders/).

## N+1 Problem

## Multiple @DgsData Annotations Via @DgsData.List

```aidl
@DgsData.List({
    @DgsData(parentType = "Query", field = "movies"),
    @DgsData(parentType = "Query", field = "shows")
})
```

## InputArgument
It's very common for GraphQL queries to have one or more input arguments. According to the GraphQL specification, an input argument can be:

    An input type
    A scalar
    An enum

Other types, such as output types, unions and interfaces, are not allowed as input arguments.

You can get input arguments as method arguments in a datafetcher method using the @InputArgument annotation.

```aidl
type Query {
    shows(title: String, filter: ShowFilter): [Show]
}

input ShowFilter {
   director: String
   genre: ShowGenre
}

enum ShowGenre {
   commedy, action, horror
}

//We can write a datafetcher with the following signature
public List<Show> shows(@InputArgument String title, @InputArgument ShowFilter filter)
```
The framework converts input arguments to Java/Kotlin types. The first step for converting input arguments is graphql-java using scalar implementations to convert raw string input into whatever type the scalar represents. A GraphQL Int becomes an Integer in Java, a formatted date string becomes a LocalDateTime (depending on the scalars you're using!), lists become an instance of java.util.ArrayList. Input objects are represented as a Map<String, Object> in graphql-java.

The next step is the DGS Framework converting the `Map<String, Object>` to the Java/Kotlin classes that you use for the @InputArgument. For Java classes, the framework creates a new instance of the class using the no-arg constructor. This implies that a no-arg constructor is required. It then sets each field of the instance to the input argument values.

```aidl
type Query {
    hello(people:[Person]): String
}

//An input argument can also be a list. If the list type is an input type, you must specify the type explicitly in the @InputArgument annotation.
public String hello(@InputArgument(collectionType = Person.class) List<Person> people)
```

## Accessing/Set Request/Response Headers
Sometimes you need to evaluate HTTP headers, or other elements of the request, in a datafetcher. You can easily get an HTTP header value by using the @RequestHeader annotation. 

The `@RequestHeader` annotation is the same annotation as used in Spring WebMVC.
```aidl
@DgsQuery
public String hello(@RequestHeader String host)
```
Similarly, you can get request parameters using @RequestParam. Both @RequestHeader and @RequestParam support a defaultValue and required argument. If a @RequestHeader or @RequestParam is required, doesn't have a defaultValue and isn't provided, a DgsInvalidInputArgumentException is thrown.

To easily get access to cookie values you can use Spring's @CookieValue annotation.
```aidl
@DgsQuery
public String usingCookieWithDefault(@CookieValue(defaultValue = "defaultvalue") myCookie: String) {
    return myCookie
}
```

You can get access to the request object, representing the HTTP request itself, as well. It's stored on the DgsContext object in the DgsDataFetchingEnvironment.
```aidl
@DgsQuery
@DgsMutation
public String updateCookie(@InputArgument String value, DgsDataFetchingEnvironment dfe) {
    DgsWebMvcRequestData requestData = (DgsWebMvcRequestData) dfe.getDgsContext().getRequestData();
    ServletWebRequest webRequest = (ServletWebRequest) requestData.getWebRequest();
    javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("mydgscookie", value);
    webRequest.getResponse().addCookie(cookie);

    return value;
}
```

## Code-Gen Plugin
DGS also comes with a code-gen plugin to generate Java or Kotlin code from GraphQL Schema. Code generation is typically integrated with the build.

`The DGS Code Generation plugin is available for Gradle and Maven`. The plugin generates code during our project’s build process based on our Domain Graph Service’s GraphQL schema file.

The plugin can generate data types for types, input types, enums, and interfaces, sample data fetchers, and type-safe query API. There is also a DgsConstants class containing the names of types and fields.

## More Info

1. [GraphQL Server with Netflix DGS and Spring Boot](https://medium.com/geekculture/graphql-server-with-netflix-dgs-and-spring-boot-2b2f1b42d163)
2. 1. [GraphQL Server with Spring Boot]()