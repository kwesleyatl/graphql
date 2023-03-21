package com.buland.graphql.netflixdgs.springboot.datatypes;

import java.util.Date;
import java.util.List;

public class SearchInput {

    String metricName;
    Date absoluteDateRangeStart;

    Date absoluteDateRangeEnd;
    RelativeDateRange relativeDataRange;
    List<FieldInput>  fields;
}
