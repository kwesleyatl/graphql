package com.buland.graphql.netflixdgs.springboot.datatypes;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Dimension {

        DOLLAR, PERCENTAGE, COUNT;

        @JsonCreator
        public static Dimension fromName(String name) {
            return Arrays.stream(Dimension.values())
                    .filter(type -> type.name().equals(name))
                    .findFirst()
                    .orElse(Dimension.DOLLAR);
        }
}
