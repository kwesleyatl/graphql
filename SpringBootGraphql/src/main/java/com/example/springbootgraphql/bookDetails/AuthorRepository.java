package com.example.springbootgraphql.bookDetails;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class AuthorRepository {

    private Logger _LOG = java.util.logging.Logger.getLogger(AuthorRepository.class.getName());
/*
    private List<Author> authors = Arrays.asList(
            new Author("author-1", "Joanne", "Rowling"),
            new Author("author-2", "Herman", "Melville"),
            new Author("author-3", "Anne", "Rice")
    );
*/
    private List<Author> authors = Stream.of(
            new Author("author-1", "Joanne", "Rowling"),
            new Author("author-2", "Herman", "Melville"),
            new Author("author-3", "Anne", "Rice")
    ).collect(Collectors.toList());

    public Author getById(String id) {
        return authors.stream().filter(author -> author.getId().equals(id)).findFirst().orElse(null);
    }

    public Author save(Author author) {
        _LOG.info("___inside save with author");
        author.setId( "author-"+(authors.size()+1));
        _LOG.info("adding author:"+author.toString());
        authors.add(author);
        return author;
    }

    public Author upsert(Author author) {
        _LOG.info("upsert save with author");
        if ( author.getId() == null ) {
            author.setId( "author-"+(authors.size()+1));
            _LOG.info("adding author:"+author.toString());
            authors.add(author);
        }
        else {
            _LOG.info("updating author:"+author.toString());
            authors.stream().filter( author1 -> author.getId() == author1.getId() ).findAny().ifPresent( filteredAuthor -> {
                filteredAuthor.setFirstName(author.getFirstName());
                filteredAuthor.setLastName(author.getLastName());
            } );
        }

        return author;
    }

    public List<Author> saveAll(List<Author> authors) {
        authors.forEach( author -> {
            author.setId( "author-"+authors.size());
            authors.add(author);
        });
        return authors;
    }
}
