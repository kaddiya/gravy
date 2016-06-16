package org.kaddiya.gravy.creator.impl

import com.google.inject.Guice
import com.google.inject.Injector
import org.kaddiya.gravy.creator.CreatorApiModule
import org.kaddiya.gravy.creator.RepositoryCreator
import org.kaddiya.gravy.model.Repository
import spock.lang.Shared
import spock.lang.Specification


class RepositoryCreatorImplSpec  extends Specification{

    @Shared
    private Injector injector

    private RepositoryCreator repositoryCreator
    def setupSpec(){
        injector = Guice.createInjector(new CreatorApiModule())
    }


    def "Repository can not be null or empty when getRepositories is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryCreatorImpl)
        when:
        def repositories =  repositoryCreator.getScript()
        then:
        assert repositories : "Repositry can not be null"
    }

    def "Repository can not be null or empty when addRepository is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryCreatorImpl)
        when:
        repositoryCreator.addOne(new Repository(name : "sample1"))
        then:
        def repositories = repositoryCreator.getScript()
        assert repositories : "Repositry can not be null"
        assert repositories.each { String repo -> repo.contains("sample1()")} :  "Added repository does not exist"
    }

    def "Repository can not be null or empty when addRepositories is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryCreatorImpl)
        when:
        repositoryCreator.addList([new Repository(name:  "sample2")])
        then:
        def repositories = repositoryCreator.getScript()
        assert repositories : "Repositry can not be null"
        assert repositories.each { String repo -> repo.contains("sample2()")} :  "Added repository does not exist"
    }
}
