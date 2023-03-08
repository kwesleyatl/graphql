# GraphQL 101

GraphQL was created in 2012 by Facebook and was open-sourced in 2015. It solves an important problem of getting only needed data from one place rather than making requests to multiple microservices and getting tons of unwanted data in response. It is very useful for network and performance-constrained environments like mobile devices.

GraphQL is often explained as a “unified interface to access data from different sources”. Although this explanation is accurate, it doesn’t reveal the underlying ideas or the motivation behind GraphQL, or even why it is called “GraphQL” — you can see the stars and the night, but not quite the “The Starry Night”.

The true heart of GraphQL lies in what I think of as the application data graph. In this article, I’ll introduce the app data graph, talk about how GraphQL queries operate on the app data graph and how we can cache GraphQL query results by exploiting their tree structure.

## Definition
GraphQL is a query language for APIs and a runtime for fulfilling those queries with your existing data. GraphQL provides a complete and understandable description of the data in your API, gives clients the power to ask for exactly what they need and nothing more, makes it easier to evolve APIs over time, and enables powerful developer tools.

Other than `query` operation type, it does provide CRUD functionality at full scale as well using `mutations` and way to update/notify clients using `subscription`.

## GraphQL API development

There are two of approaches to GraphQL API development as below

    1. Schema First API development: Here we define API with GraphQL schema file and provide service implementation for the Query/Mutation operations.
    2. Code First API development: Its opposite to above, here GraohQL schema gets generated at run time from the code definitions.

[Netflix DGS](https://netflix.github.io/dgs/) prefers Schema-first development over code-first approach.

[Graphql Kotlin](https://opensource.expediagroup.com/graphql-kotlin/docs/schema-generator/schema-generator-getting-started) on the other side prefers code first approach.

## GraphQL Schema Types

1. `Query Type`: To define read only operations (Eg: GET)
2. `Mutation Type`: To define data alter operations (Eg: POST, PUT, PATCH, DELETE)
3. `Input Type`: To define user-defined json type used as an request parameter with Query/Mutation operations.
4. `Output Type`: To define user-defined json type used as an response type with Query/Mutation operations.
5. `Enum Type`: To define constants — can be used both for properties within input/output types.
6. `Scalar Type`: All GraphQL default primitives types are considered as scalars.
7. `Interface Type`: We can use interface type as response parameter.

## The application data graph

A lot of data in modern applications can be represented using a graph of nodes and edges, with nodes representing objects and the edges representing relationships between these objects.

For instance, say we’re building a cataloging system for libraries. To keep it simple, our catalog has a bunch of books and authors and each of these books has at least one author. The authors also have coauthors with whom the author has written at least one book.

If we visualize these relationships in the form of a graph, they look something like this:
![](./images/application-graph.png)

GraphQL queries represent a way to get trees out of your app data graph. We call these query result trees.

The graph represents the relationships between the various pieces of data that we have and the entities (e.g. “Book” and “Author”) we’re trying to represent. Pretty much all applications operate on this kind of graph: they read from it and write to it. This is where GraphQL comes in.
```aidl
query {
  book(isbn: "9780674430006") {
    title 
    authors {
      name
    }
  }
}
```
### Query Paths
GraphQL allows us to define a Root Query Type (we’ll refer to it as RootQuery), which defines where a GraphQL query can start when traversing the app data graph. In our example, we start with a “Book” node, which we’ve selected using its ISBN number with the query field “book(isbn: …)”. Then, the GraphQL query traverses the graph by following the edges marked by each of the nested fields. For our query, it hops from the “Book” node to the node containing the string title of the book through the “title” field in the query. It also gets “Author” nodes by following the edges on the “Book” that are labelled with the “authors” field and gets each author’s “name” as well.
![](./images/graphql-book-exam-1.png)
For each piece of information that the query returns, there’s an associated query path, which consists of the fields in the GraphQL query that we followed to get to that information. For example, the book’s title “Capital” has the following query path:

`RootQuery → book(isbn: “9780674430006”) → title`

The fields in our GraphQL query (i.e. book, authors, name) specify which edges should be followed in the application data graph to get our desired result. This is where GraphQL gets its name: `GraphQL is a query language that traverses your data graph to produce a query result tree.`

## Very High Level View
![](./images/graphql-works-1.png)
![](./images/graphql-works-2.png)

## Graghql in Visualizations
![](./images/grahpl_datafetchers.png)
![](./images/grahpl_datafetchers_app_httpendpoint.png)
![](./images/grahpl_websockets.png)
![](./images/grahpl_RSocket.png)
![](./images/grahpl_error_partialresponse.png)
![](./images/grahpl_multiple-datafetchers.png)

## Spring MVC + GraphQL

![](./images/graphql-datafetcher-with-parent.png)
![](./images/spring-graphql-1.png)
![](./images/spring-graphql-2.png)
![](./images/spring-graphql-3.png)
![](./images/spring-graphql-4.png)
![](./images/spring-graphql-5.png)
![](./images/spring-graphql-6.png)
![](./images/spring-graphql-7.png)
![](./images/spring-graphql-8.png)
![](./images/spring-graphql-9.png)
![](./images/spring-graphql-10.png)
![](./images/spring-graphql-11.png)
![](./images/spring-graphql-12.png)

>Testing
![](./images/spring-graphql-test-1.png)
![](./images/spring-graphql-test-2.png)
![](./images/spring-graphql-test-3.png)
![](./images/spring-graphql-test-4.png)
![](./images/spring-graphql-test-5.png)

![](./images/grahpl_mutations-1.png)

## Caching GraphQL results
It turns out that the tree structure of GraphQL lends itself extremely well to client-side caching.

Apollo Client or Relay helps you in caching it out.
Apollo Client assumes that each path within your application data graph, as specified by your GraphQL queries, points to a stable piece of information.

If this doesn’t hold true in some cases (e.g. when the information pointed to by a particular query path changes really frequently), we can prevent Apollo Client from making this assumption with the concept of object identifiers, which we’ll introduce later. But, in general, this turns out to be a reasonable assumption to make when it comes to caching.
![](./images/graphql-cache-travelsal.png)
![](./images/graphql-cache-RQ.png)
![](./images/graphql-cache-RQ-2.png)
![](./images/graphql-cache-RQ3.png)

The main philosophy behind it is `Same path, same object`

>Apollo Client assumes that all objects with the same object identifier represent the same piece of information.
![](./images/applo-client-caching-objectid.png)
![](./images/applo-client-caching-without-objectid.png)
> ![](./images/applo-client-caching-with-objectid.png)
> ![](./images/applo-client-caching-with-objectid-updates.png)

## Key things to Keep In Mind
>GraphQl Query Complexity
![](./images/graphql-query-complexity.png)

>Graphql Max Query Depth Instrumentation
> >Could happen due tpo recursion (obj.obj2, obj2.obj1)
