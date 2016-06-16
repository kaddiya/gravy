package org.kaddiya.gravy.creator;


import java.util.List;

import org.kaddiya.gravy.model.Dependency;
import org.kaddiya.gravy.model.Repository;

public interface BuildScriptCreator {

    List<String> getBuildScriptList();
    void addRepositories(List<Repository> repositories);
    void addDependencies(List<Dependency> dependencies);

}
