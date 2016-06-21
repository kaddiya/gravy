package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_REPOSITRIES

import groovy.transform.CompileStatic
import org.kaddiya.gravy.generator.AbstractScriptGenerator

@CompileStatic
class RepositoryGeneratorImpl<Repository> extends AbstractScriptGenerator<Repository> {
    private static final String FORMATTER = "    "
    int repoLastIndex

    RepositoryGeneratorImpl() {
        this.modelList.add("repositories {")
        this.modelList.add("}")
        create(DEFAULT_REPOSITRIES)
    }

    @Override
    protected <Repository> void create(List<Repository> repositoryList) {
        def repoList = repositoryList.collect{ Repository repository -> FORMATTER+repository.toString()}
        repoLastIndex = this.modelList.lastIndexOf(this.modelList.last())
        this.modelList.addAll(repoLastIndex, repoList)
        this.binding.putAll(["repository" : repoList.join("\n")])
    }
}
