package com.example.springbootgraphql.bookDetails;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.logging.Logger;

/**
 * We need to annotate the handler methods with @QueryMapping annotation and place these inside standard @Controller components in our application.
 * This registers the annotated classes as data fetching components in our GraphQL application.
 *
 * Its similar to Spring MVC
 */
@Controller
public class BookController {

    private Logger _LOG = java.util.logging.Logger.getLogger(BookController.class.getName());

    BookRepository bookRepository;

    AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * The @QueryMapping annotation binds this method to a query, a field under the Query type.
     * The query field is then determined from the method name, bookById. It could also be declared on the annotation.
     * Spring for GraphQL uses RuntimeWiring.Builder to register the handler method as a graphql.schema.DataFetcher
     * for the query field bookById.
     *
     * In GraphQL Java, DataFetchingEnvironment provides access to a map of field-specific argument values.
     * Use the @Argument annotation to have an argument bound to a target object and injected into the handler method.
     * By default, the method parameter name is used to look up the argument. The argument name can be specified in
     * the annotation.
     *
     * @param id
     *
     * @return
     */
    @QueryMapping
    public Book bookById(@Argument String id) {
        return bookRepository.getById(id);
    }

    @QueryMapping
    public Book bookByName(@Argument String name) {
        return bookRepository.getByName(name);
    }

    /**
     * The @SchemaMapping annotation maps a handler method to a field in the GraphQL schema and declares it to be the
     * DataFetcher for that field. The field name defaults to the method name, and the type name defaults to the
     * simple class name of the source/parent object injected into the method. In this example, the field defaults to
     * author and the type defaults to Book as Book is the Parent that has `author` field under it.
     *
     * The type and field can be specified in the annotation.
     *
     * So Basically, First it will get Book using Book Query as its the parent and once it has Book object and client asked to
     * also return its 'author' filed than it will call this method and pass the parent to it.
     *
     * @param book
     * @return
     */
    @SchemaMapping
    public Author author(Book book) {
        return authorRepository.getById(book.getAuthorId());
    }


    /**
     * The “!” at the end of some names indicates that it's a non-nullable type. Any type that doesn't have this can be null in the response from the server.
     * The GraphQL service handles these correctly, allowing us to safely request child fields of nullable types.
     *
     * In Spring for GraphQL, we can implement mutation using @SchemaMapping or @MutationMapping.
     *
     * @param name
     * @param pageCount
     * @param authorFName
     * @param authorLName
     * @return
     */
    @MutationMapping(name="addBook")
    public Book createBookWithAuthor(@Argument String name, @Argument int pageCount, @Argument String authorFName, @Argument String authorLName) {
        Author author = Author.builder().firstName(authorFName).lastName(authorLName).build();
        author = authorRepository.save(author);
        Book book = new Book(name,pageCount,author.getId());
        return bookRepository.save(book);
    }

    //@MutationMapping(name="updateBook")
    @SchemaMapping(typeName = "Mutation", field = "updateBook")
    public Book updateBookWithAuthor(@Argument String id, @Argument String name, @Argument int pageCount, @Argument String authorFName, @Argument String authorLName) {
        Book book = bookRepository.getById(id);
        //book.setName(name);
        book.setPageCount(pageCount);

        Author author = Author.builder().id(book.getAuthorId()).firstName(authorFName).lastName(authorLName).build();
        author = authorRepository.upsert(author);
        return bookRepository.upsert(book);
    }

    @MutationMapping(name="addBookWithInput")
    //@SchemaMapping(typeName = "Mutation", field = "addBookWithInput")
    public Book addBookWithInput(@Argument BookInput book) {
        _LOG.info("___inside addBookWithInput:"+book);
        Author author = Author.builder().firstName(book.getAuthorFName()).lastName(book.getAuthorLName()).build();
        author = authorRepository.save(author);
        Book __book = new Book(book.getName(),book.getPageCount(),author.getId());
        return bookRepository.save(__book);
    }
}
