package org.kaddiya.gravy.initialiser.impl

import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitliaserSpec extends Specification {
    @Shared
    Initialiser gradleAppInitialiser =  new GradleApplicationInitialiser();

    @Shared
    File rootDirectory

    def "prepare environment should download the gradle wrapper"(){
        when:
        String [] args = ["COOK","foo"]
         rootDirectory = gradleAppInitialiser.prepareEnvironment(args)
        then:
        assert  rootDirectory.isDirectory() : "Root Directory not created"
        assert new File(rootDirectory,"settings.gradle").exists() : "Settings file not found"
        assert new File(rootDirectory,"build.gradle").exists()    : "build.gradle not created"
        assert new File(rootDirectory,"src").exists()             : "src folder not created"

    }

    def cleanup(){
        rootDirectory.deleteDir()
    }
}
