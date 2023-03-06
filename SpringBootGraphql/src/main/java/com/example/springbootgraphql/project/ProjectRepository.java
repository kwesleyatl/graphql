package com.example.springbootgraphql.project;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProjectRepository {

    List<Project> projects = new ArrayList<Project>();

    public ProjectRepository() {
        addProjects("1","Test1","https://github.com/BulandMalik/graphql/test1",ProjectStatus.ACTIVE);
        addProjects("2","Test2","https://github.com/BulandMalik/graphql/test2",ProjectStatus.COMMUNITY);
        addProjects("3","Test3","https://github.com/BulandMalik/graphql/test3",ProjectStatus.ACTIVE);
        addProjects("4","Test4","https://github.com/BulandMalik/graphql/test4",ProjectStatus.INCUBATING);
        addProjects("5","Test5","https://github.com/BulandMalik/graphql/test5",ProjectStatus.ACTIVE);
    }

    private void addProjects(String slug, String pName, String repUrl, ProjectStatus pStatus) {
        Project p = new Project(slug,pName,repUrl, pStatus);
        List<Release> r = Arrays.asList(
                new Release(p, "1.0.0.0.SNAPSHOT", ReleaseStatus.SNAPSHOT),
                new Release(p, "1.0.0.1.SNAPSHOT", ReleaseStatus.SNAPSHOT),
                new Release(p, "1.0.0.0", ReleaseStatus.GENERAL_AVAILABILITY)
        );
        p.setReleases(r);
        projects.add( p );
    }

    public List<Project> getProjects(String slug) {
        return projects.stream().filter(project -> project.getSlug().equals(slug)).collect(Collectors.toList());
    }

    public List<Release> getProjectReleases(String slug) {
        List<Project> projects = getProjects(slug);
        List<Release> releases = new ArrayList<Release>();
        projects.forEach( project -> releases.addAll(project.getReleases()));
        return releases;
    }
}
