package com.example.springbootgraphql.bookDetails;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BookRepository {

    private Logger _LOG = java.util.logging.Logger.getLogger(BookRepository.class.getName());
    private static List<Book> books = Stream.of(
            new Book("book-1", "Harry Potter and the Philosopher's Stone", 223, "author-1"),
            new Book("book-2", "Moby Dick", 635, "author-2"),
            new Book("book-3", "Interview with the vampire", 371, "author-3")
    ).collect(Collectors.toList());

    public static Book getById(String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    public static Book getByName(String name) {
        return books.stream().filter(book -> book.getName().equals(name)).findFirst().orElse(null);
    }

    public Book save(Book book) {
        book.setId( "book-"+ (books.size()+1) );
        books.add(book);
        return book;
    }

    public Book upsert(Book book) {
        _LOG.info("upsert save with book");
        if ( book.getId() == null ) {
            book.setId( "book-"+(books.size()+1));
            _LOG.info("adding book:"+book.toString());
            books.add(book);
        }
        else {
            _LOG.info("updating book:"+book.toString());
            books.stream().filter( book1 -> book.getId() == book1.getId() ).findAny().ifPresent( filteredBook -> {
                filteredBook.setName(book.getName());
                filteredBook.setPageCount(book.getPageCount());
            } );
        }

        return book;
    }

}
