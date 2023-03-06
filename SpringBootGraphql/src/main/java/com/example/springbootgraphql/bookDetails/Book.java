package com.example.springbootgraphql.bookDetails;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Book {

    private String id;
    private String name;
    private int pageCount;
    private String authorId;

    public Book(String id, String name, int pageCount, String authorId) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
    }

    public Book(String name, int pageCount, String authorId) {
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthorId() {
        return authorId;
    }
}
