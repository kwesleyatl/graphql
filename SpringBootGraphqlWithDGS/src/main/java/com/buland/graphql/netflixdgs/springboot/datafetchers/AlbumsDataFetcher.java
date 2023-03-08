package com.buland.graphql.netflixdgs.springboot.datafetchers;

import com.buland.graphqldgs.generated.types.Album;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//to define the DGS data fetcher, we need to create a query method in the @DgsComponent class
@DgsComponent
public class AlbumsDataFetcher {

    private final List<Album> albums = Arrays.asList(
            Album.newBuilder().artist("Fleetwood Mac").title("Rumours").recordNo(20).build(),
            Album.newBuilder().artist("Marvin Gaye").title("What's Going On").recordNo(10).build(),
            Album.newBuilder().artist("The Beach Boys").title("Pet Sounds").recordNo(12).build()
    );

    /*
    * We want to query a list of Albums in our example, so let's mark the method with @DgsQuery:
    * method name by default should match to what you have defined inside the schema
    *
    * We also marked arguments of the method with the annotation @InputArgument. This annotation will use the name of the method
    * argument to match it with the name of an input argument sent in the query.
    *
    * You can use annotation params to identity right mapping to schema like `@InputArgument("titleFilter") String filter`
    *
     */
    @DgsQuery
    public List<Album> albums(@InputArgument String titleFilter) { //@InputArgument("titleFilter") String filter
        if(titleFilter == null) {
            return albums;
        }
        return albums.stream()
                .filter(s -> s.getTitle().contains(titleFilter))
                .collect(Collectors.toList());
    }
}
