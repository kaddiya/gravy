package org.kaddiya.gravy.generator;


import java.util.List;

import org.kaddiya.gravy.model.Dependency;
import org.kaddiya.gravy.model.Repository;

public interface BuildScriptGenerator {

    List<String> getBuildScriptList();
    void addRepositories(List<Repository> repositories);
    void addDependencies(List<Dependency> dependencies);

}
