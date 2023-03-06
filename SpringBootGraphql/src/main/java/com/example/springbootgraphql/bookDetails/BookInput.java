package com.example.springbootgraphql.bookDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookInput {
    private String name;
    private int pageCount;
    private String authorFName;

    private String authorLName;

/*    public BookInput(String name, int pageCount, String authorFName, String authorLName) {
        this.name = name;
        this.pageCount = pageCount;
        this.authorFName = authorFName;
        this.authorLName = authorLName;
    }*/

}
