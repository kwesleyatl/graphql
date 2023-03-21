package com.buland.graphql.netflixdgs.springboot.datafetchers;

import com.buland.graphql.netflixdgs.springboot.datatypes.Metric;
import com.buland.graphql.netflixdgs.springboot.datatypes.SearchInput;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;

public class MetricsDataFetcher {

    public MetricsDataFetcher(){
    }

    @DgsData(parentType = "Query", field = "retrieveMetrics")
    public List<Metric> metrics(@InputArgument(collectionType = SearchInput.class) List<Metric> metrics){
        return null;
    }

}
