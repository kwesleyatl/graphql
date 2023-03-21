package com.buland.graphql.netflixdgs.springboot.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum RelativeDateRange {
    LAST_7_DAYS,
    LAST_30_DAYS,
            LAST_60_DAYS,
    LAST_90_DAYS,
            LAST_360_DAYS ;

    @JsonCreator
    public static RelativeDateRange fromName(String name) {
        return Arrays.stream(RelativeDateRange.values())
                .filter(type -> type.name().equals(name))
                .findFirst()
                .orElse(RelativeDateRange.LAST_7_DAYS);
    }
}
