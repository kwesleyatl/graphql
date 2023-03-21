package com.buland.graphql.netflixdgs.springboot.config;

import com.buland.graphql.netflixdgs.springboot.datatypes.Metric;
import com.buland.graphql.netflixdgs.springboot.datatypes.SearchInput;

public interface Configurator {

    public void load(String metricName);

    public Metric fetchData(SearchInput input);
}
