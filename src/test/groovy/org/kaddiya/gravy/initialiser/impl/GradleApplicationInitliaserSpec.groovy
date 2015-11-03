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

    def "prepare environment should download the gradle wrapper"(){

        when:
        Path rootDirectory = gradleAppInitialiser.prepareEnvironment(System.getProperty("user.dir"))
        then:
        assert  rootDirectory.toFile().isDirectory() : "Directory not created"


    }
}
