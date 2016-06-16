package org.kaddiya.gravy.creator.impl

import com.google.inject.Inject
import org.kaddiya.gravy.creator.BuildScriptCreator
import org.kaddiya.gravy.creator.DependencyCreator
import org.kaddiya.gravy.creator.RepositoryCreator
import org.kaddiya.gravy.model.Dependency
import org.kaddiya.gravy.model.Repository

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_BUILDSCRIPT_DEPENDENCY

class BuildScriptCreatorImpl implements BuildScriptCreator{
    private static final String FORMATTER = "    "
    private List<String> buildScriptList
    private final DependencyCreatorImpl dependancyCreator
    private final RepositoryCreatorImpl repositoryCreator

    @Inject
    BuildScriptCreatorImpl(RepositoryCreator repositoryCreator, DependencyCreator dependancyCreator) {
        this.repositoryCreator = repositoryCreator
        this.dependancyCreator = dependancyCreator
        buildScriptList = new ArrayList<>()
        buildScriptList.add("buildscript { ")
        buildScriptList.add("}")
        this.createBuildScript(this.repositoryCreator.getScript())
        this.dependancyCreator.getScript().clear()
        this.dependancyCreator.getScript().add("dependencies { ")
        this.dependancyCreator.getScript().add("}")
        addDependencies(DEFAULT_BUILDSCRIPT_DEPENDENCY)
    }

    @Override
    List<String> getBuildScriptList() {
        return buildScriptList
    }

    @Override
    void addRepositories(List<Repository> repositories) {
        this.repositoryCreator.addList(repositories)
        this.createBuildScript(this.repositoryCreator.getScript())
    }

    @Override
    void addDependencies(List<Dependency> dependencies) {
        this.dependancyCreator.addList(dependencies)
        createBuildScript(this.dependancyCreator.getScript())
    }

    private void createBuildScript(List<String> buildScriptChild){
        def repoLastIndex = this.buildScriptList.lastIndexOf(this.buildScriptList.last())
        this.buildScriptList.addAll(repoLastIndex, buildScriptChild)
    }
}
