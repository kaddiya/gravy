package org.kaddiya.gravy.generator.impl

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import org.kaddiya.gravy.CodeGenerator
import org.kaddiya.gravy.GravyModule
import org.kaddiya.gravy.generator.CreatorApiModule
import org.kaddiya.gravy.model.BuildPhaseType
import org.kaddiya.gravy.model.Dependency
import org.kaddiya.gravy.model.GAV
import spock.lang.Shared
import spock.lang.Specification


class DependancyCreatorImplSpec extends Specification implements CodeGenerator{


    @Shared
    private Injector injector
    private DependencyGeneratorImpl dependencyCreator


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


    def "Dependency list can not be null"(){
        setup:
            dependencyCreator = injector.getInstance(DependencyGeneratorImpl)
        when:
        def dependencyList = dependencyCreator.getScript()

        then:
        assert dependencyList != null : "Depenency list can not be null"
    }


    def "Dependency list can not be empty when addDependency is called"(){
        setup:
        dependencyCreator = injector.getInstance(DependencyGeneratorImpl)
        when:
        dependencyCreator.addOne(new Dependency(phaseType : BuildPhaseType.COMPILE, gav : new GAV("com.foo", "sample","1.0"), isBuildScript: false))
        then:
        def dependency = dependencyCreator.getScript()
        assert dependency : "Depenency list can not be null or empty"
        assert dependency.contains("compile 'com.foo:sample:1.0'") : "Added depnendcy not found"
    }

    def "Dependency list can not be empty when addDependencies is called"(){
        setup:
        dependencyCreator = injector.getInstance(DependencyGeneratorImpl)
        when:
            this.dependencyCreator.addList([new Dependency(phaseType : BuildPhaseType.COMPILE, gav : new GAV("com.foo", "sample","1.0"), isBuildScript: false)])
        then:
        def dependency = dependencyCreator.getScript()
        assert dependency : "Depenency list can not be null or empty"
        assert dependency.contains("compile 'com.foo:sample:1.0'") : "Added depnendcy not found"
    }



}
