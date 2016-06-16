package org.kaddiya.gravy.initialiser.impl

import com.google.inject.Guice
import com.google.inject.Injector
import org.kaddiya.gravy.GravyModule
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitliaserSpec extends Specification {
    @Shared
    Initialiser gradleAppInitialiser

    @Shared
    File rootDirectory

    @Shared
    private Injector injector

    def setupSpec(){

        injector = Guice.createInjector(new GravyModule())
        gradleAppInitialiser = injector.getInstance(GradleApplicationInitialiser)

    }

    def "prepare environment should download the gradle wrapper"(){
        when:
        String [] args = ["COOK","foo","com.webonise"]
         rootDirectory = gradleAppInitialiser.prepareEnvironment(args)
        println"root directory ${rootDirectory}"
        then:
        assert  rootDirectory.isDirectory() : "Root Directory not created"
        assert new File(rootDirectory,"settings.gradle").exists() : "Settings file not found"
        assert new File(rootDirectory,"build.gradle").exists()    : "build.gradle not created"
        assert new File(rootDirectory,"src").exists()             : "src folder not created"

    }

    def "writeBuildGradleFile should write all the default values of plugins, repos , dependencies and build scripts"(){
        setup:
            String [] args = ["COOK","foo","com.webonise"]
            rootDirectory = gradleAppInitialiser.prepareEnvironment(args)
            def generatedFileLines = new File(rootDirectory,"build.gradle").readLines()
        when:
            gradleAppInitialiser.writeBuildGradleTemplate(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            assert new File(rootDirectory,"build.gradle").exists()    : "build.gradle not created"
            def lines = new File(rootDirectory,"build.gradle").readLines()


    }

    def cleanup(){
       rootDirectory.deleteDir()
    }
}
