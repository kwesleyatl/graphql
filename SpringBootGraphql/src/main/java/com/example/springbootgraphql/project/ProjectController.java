package com.example.springbootgraphql.project;

import java.util.List;

import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectController {

    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @QueryMapping(name="project")
    public Project getProject(@Argument String slug, GraphQLContext context, DataFetchingEnvironment env) {
        //optionally take other GraphQL-related parameters, such as GraphQLContext, DataFetchingEnvironment, etc., for access to the underlying context and environment.
        System.out.println("________ "+context);
        System.out.println("________ "+env);
        return projectRepository.getProjects(slug).get(0);
    }

    /**
     * Sometimes, the value of a field is non-trivial to load. This might involve database lookups, complex calculations, or anything else.
     * The @SchemaMapping annotation maps the handler method to a field with the same name in the schema and uses it as the DataFetcher for that field.
     *
     * Importantly, if the client doesn't request a field, then the GraphQL Server won't do the work to retrieve it. This means that if a client retrieves a
     * project and doesn't ask for the releases field, the release() method above won't be executed, and the DAO call won't be made.
     *
     * @param project
     * @return
     */
    //@SchemaMapping
    @SchemaMapping(typeName="Project", field="releases")
    public List<Release> getProjectReleases(Project project) {
        return projectRepository.getProjectReleases(project.getSlug());
    }

}
