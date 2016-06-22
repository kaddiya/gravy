package org.kaddiya.gravy.generator.impl

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import org.kaddiya.gravy.CodeGenerator
import org.kaddiya.gravy.GravyModule
import org.kaddiya.gravy.generator.CreatorApiModule
import org.kaddiya.gravy.model.Repository
import spock.lang.Shared
import spock.lang.Specification


class RepositoryCreatorImplSpec  extends Specification implements CodeGenerator{

    @Shared
    private Injector injector

    private RepositoryGeneratorImpl repositoryCreator
    def setupSpec(){
        def props = this.gravyPropMap
        def gravyProps = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Map).annotatedWith(Names.named("gravyProps")).toInstance(props)
            }
        }

        injector = Guice.createInjector(new GravyModule(gravyProps))
    }


    def "Repository can not be null or empty when getRepositories is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryGeneratorImpl)
        when:
        def repositories =  repositoryCreator.getScript()
        then:
        assert repositories : "Repositry can not be null"
    }

    def "Repository can not be null or empty when addRepository is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryGeneratorImpl)
        when:
        repositoryCreator.addOne(new Repository(name : "sample1"))
        then:
        def repositories = repositoryCreator.getScript()
        assert repositories : "Repositry can not be null"
        assert repositories.each { String repo -> repo.contains("sample1()")} :  "Added repository does not exist"
    }

    def "Repository can not be null or empty when addRepositories is called"(){
        setup:
        repositoryCreator = injector.getInstance(RepositoryGeneratorImpl)
        when:
        repositoryCreator.addList([new Repository(name:  "sample2")])
        then:
        def repositories = repositoryCreator.getScript()
        assert repositories : "Repositry can not be null"
        assert repositories.each { String repo -> repo.contains("sample2()")} :  "Added repository does not exist"
    }
}
