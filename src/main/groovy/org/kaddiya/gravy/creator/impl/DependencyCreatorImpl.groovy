package org.kaddiya.gravy.creator.impl

import groovy.transform.CompileStatic
import org.kaddiya.gravy.creator.AbstractScriptCreator
import org.kaddiya.gravy.creator.DependencyCreator
import org.kaddiya.gravy.model.BuildPhaseType

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_COMPILE_DEPENDANCIES
import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_TEST_DEPENDENCIES


class DependencyCreatorImpl<Dependency> extends AbstractScriptCreator<Dependency> implements DependencyCreator {

    DependencyCreatorImpl() {
        this.modelList.add("dependencies { ")
        this.modelList.add("}")
        create(DEFAULT_COMPILE_DEPENDANCIES)
        create(DEFAULT_TEST_DEPENDENCIES)
    }

    @Override
    protected <Dependency> void create( List<Dependency> dependencyList ) {
        List<String> dependencies = dependencyList.collect { dependency ->
            return dependency.toString()
        }
        int lastIndex = this.modelList.lastIndexOf("}")
        this.modelList.addAll(lastIndex, dependencies)
        testCompileDependencies(dependencyList)
        compileDependencies(dependencyList)
        runtimeDependencies(dependencyList)
        testRuntimeDependencies(dependencyList)
        classpathDependencies(dependencyList)
        grettyRunnerTomcat8Dependencies(dependencyList)

    }


    private void testCompileDependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.TEST_COMPILE.phaseTypeValue
        String dependency = modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.TEST_COMPILE
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")
        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }

    private void compileDependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.COMPILE.phaseTypeValue
        String dependency =  modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.COMPILE
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")

        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }

    private void runtimeDependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.RUNTIME.phaseTypeValue
        String dependency =  modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.RUNTIME
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")

        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }

    private void testRuntimeDependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.TEST_RUNTIME.phaseTypeValue
        String dependency =  modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.TEST_RUNTIME
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")

        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }

    private void classpathDependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.CLASSPATH.phaseTypeValue
        String dependency =  modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.CLASSPATH
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")

        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }

    private void grettyRunnerTomcat8Dependencies( List<Dependency> modelList ){
        String phaseTypeValue = BuildPhaseType.GRETTY_RUNNER_TOMCAT8.phaseTypeValue
        String dependency =  modelList.findAll { Dependency dependency ->
            dependency.getPhaseType() == BuildPhaseType.GRETTY_RUNNER_TOMCAT8
        }.collect{Dependency dependency -> dependency.toString()}.join("\n")

        if ( dependency ) {
            this.binding.putAll([ ("${phaseTypeValue}".toString()) : dependency])
        }
    }
}
