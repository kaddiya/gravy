package org.kaddiya.gravy.creator.impl

import groovy.transform.CompileStatic
import org.kaddiya.gravy.creator.AbstractScriptCreator
import org.kaddiya.gravy.creator.RepositoryCreator

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_REPOSITRIES

@CompileStatic
class RepositoryCreatorImpl<Repository> extends AbstractScriptCreator<Repository> implements RepositoryCreator{
    private static final String FORMATTER = "    "
    int repoLastIndex

    RepositoryCreatorImpl() {
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
