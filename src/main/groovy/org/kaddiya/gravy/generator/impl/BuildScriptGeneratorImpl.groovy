package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_BUILDSCRIPT_DEPENDENCY

import com.google.inject.Inject
import org.kaddiya.gravy.generator.BuildScriptGenerator
import org.kaddiya.gravy.model.Dependency
import org.kaddiya.gravy.model.Repository

class BuildScriptGeneratorImpl implements BuildScriptGenerator{
    private static final String FORMATTER = "    "
    private List<String> buildScriptList
    private final DependencyGeneratorImpl dependancyGenerator
    private final RepositoryGeneratorImpl repositoryGenerator

    @Inject
    BuildScriptGeneratorImpl(RepositoryGeneratorImpl repositoryGenerator, DependencyGeneratorImpl dependancyGenerator ) {
        this.repositoryGenerator = repositoryGenerator
        this.dependancyGenerator = dependancyGenerator
        buildScriptList = new ArrayList<>()
        buildScriptList.add("buildscript { ")
        buildScriptList.add("}")
        this.createBuildScript(this.repositoryGenerator.getScript())
        this.dependancyGenerator.getScript().clear()
        this.dependancyGenerator.getScript().add("dependencies { ")
        this.dependancyGenerator.getScript().add("}")
        addDependencies(DEFAULT_BUILDSCRIPT_DEPENDENCY)
    }

    @Override
    List<String> getBuildScriptList() {
        return buildScriptList
    }

    @Override
    void addRepositories(List<Repository> repositories) {
        this.repositoryGenerator.addList(repositories)
        this.createBuildScript(this.repositoryGenerator.getScript())
    }

    @Override
    void addDependencies(List<Dependency> dependencies) {
        this.dependancyGenerator.addList(dependencies)
        createBuildScript(this.dependancyGenerator.getScript())
    }

    private void createBuildScript(List<String> buildScriptChild){
        def repoLastIndex = this.buildScriptList.lastIndexOf(this.buildScriptList.last())
        this.buildScriptList.addAll(repoLastIndex, buildScriptChild)
    }
}
