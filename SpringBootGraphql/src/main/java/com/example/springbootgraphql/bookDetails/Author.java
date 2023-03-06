package com.example.springbootgraphql.bookDetails;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class Author {

    private String id;
    private String firstName;
    private String lastName;

    public Author(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }
}
