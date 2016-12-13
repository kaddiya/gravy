package org.kaddiya.gravy.initialiser.impl
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import groovy.text.SimpleTemplateEngine
import org.kaddiya.gravy.CodeGenerator
import org.kaddiya.gravy.GravyModule
import org.kaddiya.gravy.generator.impl.MetaRouterGenerator
import org.kaddiya.gravy.generator.impl.PingResourceGenerator
import org.kaddiya.gravy.generator.impl.RootRouterGenerator
import org.kaddiya.gravy.generator.impl.ServiceModuleGenerator
import org.kaddiya.gravy.generator.impl.WebXmlCreator
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Paths

import static org.kaddiya.gravy.Constants.DEFAULT_SERVICE_MODULE
import static org.kaddiya.gravy.Constants.DEFAULT_GOUP_ID
import static org.kaddiya.gravy.Constants.DEFAULT_ROOT_ROUTER
import static org.kaddiya.gravy.generator.impl.GitIgnoreGenerator.GIT_IGNORE

class GradleApplicationInitliaserSpec extends Specification implements CodeGenerator{
    @Shared
    Initialiser gradleAppInitialiser


    File rootDirectory

    @Shared
    private Injector injector

    private String groupId

    def setupSpec(){
        def props = this.gravyPropMap
        def gravyProps = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Map).annotatedWith(Names.named("gravyProps")).toInstance(props)
            }
        }
        injector = Guice.createInjector(new GravyModule(gravyProps))
        gradleAppInitialiser = injector.getInstance(GradleApplicationInitialiser)

    }

    def setup(){
        rootDirectory = gradleAppInitialiser.prepareEnvironment()
        groupId = DEFAULT_GOUP_ID.replace(".", "/")
    }

    def "prepare environment should download the gradle wrapper"(){
        when:
            rootDirectory = gradleAppInitialiser.prepareEnvironment()
        then:
        assert  rootDirectory.isDirectory() : "Root Directory not created"
        assert new File(rootDirectory,"settings.gradle").exists() : "Settings file not found"
        assert new File(rootDirectory,"build.gradle").exists()    : "build.gradle not created"
        assert new File(rootDirectory,"src").exists()             : "src folder not created"

    }

    def "writeBuildGradleFile should write all the default values of plugins, repos , dependencies and build scripts"(){
        when:
            gradleAppInitialiser.writeBuildGradleTemplate(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            assert new File(rootDirectory,"build.gradle").exists()    : "build.gradle not created"
            def lines = new File(rootDirectory,"build.gradle").readLines()


    }

    def "writeWebXmlFile should be created with given servlet names"(){
        when:
            gradleAppInitialiser.writeWebXmlFile(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def webXmlFile = new File(rootDirectory, Paths.get(WebXmlCreator.WEB_XML_PATH).toFile().toString())
            assert webXmlFile.exists() :" Web.xml does not exist"


    }

    def "gitIgnoreFile should be created"(){
        when:
        gradleAppInitialiser.writeGitIgnoreFile(rootDirectory)
        then:
        assert  rootDirectory.isDirectory() : "Root Directory not created"
        def gitIgnoreFile = new File(rootDirectory, GIT_IGNORE)
        assert gitIgnoreFile.exists() :" gitIgnoreFile does not exist"


    }

    def "writeServiceModuleFile should be created with given  names"(){
        when:
            gradleAppInitialiser.writeServiceModuleClass(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def engine = new SimpleTemplateEngine()

            def filePath = engine.createTemplate(ServiceModuleGenerator.MODULE_CLASS_PATH).make(["groupId" : groupId, "serviceModule" : "${DEFAULT_SERVICE_MODULE}"]).toString()
            def serviceModule = new File(rootDirectory, Paths.get(filePath).toFile().toString())
            assert serviceModule.exists() :" ServiceModule.groovy does not exist"


    }
    def "writeRootRouterFile should be created with given servlet names"(){
        when:
            gradleAppInitialiser.writeRootRouterClass(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def engine = new SimpleTemplateEngine()
            def filePath = engine.createTemplate(RootRouterGenerator.ROOT_ROUTER_PATH).make(["groupId" : groupId, "rootRouter" : DEFAULT_ROOT_ROUTER]).toString()
            def rootRouter = new File(rootDirectory, Paths.get(filePath).toFile().toString())
            assert rootRouter.exists() :" MainRouter.groovy does not exist"


    }

    def "writeMetaRouterClass should be created "(){
        when:
            gradleAppInitialiser.writeMetaRouterClass(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def engine = new SimpleTemplateEngine()
            def pingResourcePath = engine.createTemplate(MetaRouterGenerator.META_ROUTER_PATH).make(["groupId" : groupId]).toString()
            def metaRouter = new File(rootDirectory, Paths.get(pingResourcePath).toFile().toString())

            assert metaRouter.exists() :" MetaRouter.groovy does not exist"

    }

    def "writePingResourceClass should be created with given servlet names"(){
        when:
            gradleAppInitialiser.writePingResourceClass(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def engine = new SimpleTemplateEngine()

            def pingResourcePath = engine.createTemplate(PingResourceGenerator.PING_RESOURCE_PATH).make(["groupId" : groupId]).toString()
            def pingResource = new File(rootDirectory, Paths.get(pingResourcePath).toFile().toString())
            assert pingResource.exists() :" PingResource.groovy does not exist"

    }

    def cleanup(){
      rootDirectory.deleteDir()
    }
}
