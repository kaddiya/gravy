package org.kaddiya.gravy.initilaiser.impl

import java.nio.file.Paths

import com.google.inject.Inject
import org.gradle.tooling.GradleConnector
import org.kaddiya.gravy.Constants
import org.kaddiya.gravy.generator.impl.BuildScriptGeneratorImpl
import org.kaddiya.gravy.generator.impl.DependencyGeneratorImpl
import org.kaddiya.gravy.generator.impl.MetaRouterGenerator
import org.kaddiya.gravy.generator.impl.PingResourceGenerator
import org.kaddiya.gravy.generator.impl.PluginGeneratorImpl
import org.kaddiya.gravy.generator.impl.RepositoryGeneratorImpl
import org.kaddiya.gravy.generator.impl.RootRouterGenerator
import org.kaddiya.gravy.generator.impl.ServiceModuleGenerator
import org.kaddiya.gravy.generator.impl.WebXmlCreator
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.model.GravyProject

/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitialiser implements Initialiser {

    private PluginGeneratorImpl pluginCreator
    private DependencyGeneratorImpl dependancyCreator
    private BuildScriptGeneratorImpl buildScriptCreator
    private RepositoryGeneratorImpl repositoryCreator
    private WebXmlCreator webXmlCreator
    private ServiceModuleGenerator serviceModuleCreator
    private RootRouterGenerator  rootRouterCreator
    private MetaRouterGenerator metaRouterCreator
    private PingResourceGenerator pingResourceCreator
    String groupId
    File groupPackage


    @Inject
    GradleApplicationInitialiser( PluginGeneratorImpl pluginCreator, DependencyGeneratorImpl dependancyCreator,
                                  BuildScriptGeneratorImpl buildScriptCreator, RepositoryGeneratorImpl repositoryCreator,
                                  WebXmlCreator webXmlCreator, ServiceModuleGenerator serviceModuleCreator,
                                  RootRouterGenerator rootRouterCreator, MetaRouterGenerator metaRouterCreator,
                                  PingResourceGenerator pingResourceCreator ) {
        this.pluginCreator = pluginCreator
        this.dependancyCreator = dependancyCreator
        this.buildScriptCreator = buildScriptCreator
        this.repositoryCreator = repositoryCreator
        this.webXmlCreator = webXmlCreator
        this.serviceModuleCreator = serviceModuleCreator
        this.rootRouterCreator = rootRouterCreator
        this.metaRouterCreator = metaRouterCreator
        this.pingResourceCreator = pingResourceCreator
    }

    @Override
    File prepareEnvironment( String[] args ) {
        assert args.size() == 3: "Invalid arguments"
        String applicationName = args[1]
        groupId = args[2]
        String currentPath = System.getProperty("user.dir")
        assert currentPath: "Please specify the path"
        File projectRootDirectory = new File(currentPath, applicationName)

        if ( projectRootDirectory.exists() ) {
            projectRootDirectory.deleteDir()
        }
        projectRootDirectory.mkdir()

        downloadGradleWrapper(projectRootDirectory)
        bootstrapProject(projectRootDirectory)
        String groovyPath = "/src/main/groovy"
        String[] packageDirs = groupId.split("\\.")
        packageDirs.each {String dirName ->
                groovyPath = "${groovyPath}/${dirName}"
        }
        def groovyDir = Paths.get(projectRootDirectory.toString()+groovyPath).toFile()
        groovyDir.mkdirs()
        return projectRootDirectory
    }

    @Override
    void writeBuildGradleFile( File projectRootDirectory ) {
        GravyProject buildGradle = new GravyProject(new File(projectRootDirectory, "build.gradle"))
        buildGradle.writePlugins(this.pluginCreator.getScript())
        buildGradle.writeRepositories(this.repositoryCreator.getScript())
        buildGradle.writeDependencies(this.dependancyCreator.getScript())
        buildGradle.writeBuildScript(this.buildScriptCreator.getBuildScriptList())
        buildGradle.writeFile()
    }

    @Override
    void writeBuildGradleTemplate( File projectRootDir ) {

        File file = new File(projectRootDir, "build.gradle")
        def teamplateBinding = new LinkedHashMap<String, String>()
        teamplateBinding.putAll(this.pluginCreator.getModelBinding())
        teamplateBinding.putAll(this.repositoryCreator.getModelBinding())
        teamplateBinding.putAll(this.dependancyCreator.getModelBinding())
        teamplateBinding.putAll(["buildScript" : this.buildScriptCreator.getBuildScriptList().join("\n")])

        ClassLoader classLoader = getClass().getClassLoader();
        File gradleBuildFile = new File(classLoader.getResource("gradleBuildTemplate.txt").getFile())
        String gradleBuildTemplate = gradleBuildFile.text
        def engine1 = new groovy.text.GStringTemplateEngine()
        def template = engine1.createTemplate(gradleBuildTemplate).make(teamplateBinding)
        file.write(template.toString())


    }

    @Override
    void writeWebXmlFile( File projectRootDir, String apiModuleClassName) {
        def webXmlFile = webXmlCreator.createWebxmlFile(projectRootDir)
        ClassLoader classLoader = getClass().getClassLoader();
        File webXmlTextFile = new File(classLoader.getResource("webXmlTemplate.txt").getFile())
        def webXmlTemplate = webXmlCreator.createXmlTemplate(webXmlTextFile, ["serviceModule" : apiModuleClassName, "groupId" : groupId])
        webXmlFile.write(webXmlTemplate)
    }

    @Override
    void writeServiceModuleClass( File projectRootDir, String className, String rootRouterClassName ) {
        def serviceClassFile = this.serviceModuleCreator.createFile(projectRootDir)
        ClassLoader classLoader = getClass().getClassLoader();
        File serviceClassTextFile = new File(classLoader.getResource("serviceModuleClassTemplate.txt").getFile())
        def serviceClassTemplate = this.serviceModuleCreator.createCode(serviceClassTextFile)
        serviceClassFile.write(serviceClassTemplate)
    }

    @Override
    void writeRootRouterClass( File projectRootDir, String className ) {
        def routerClassFile = this.rootRouterCreator.createFile(projectRootDir)
        ClassLoader classLoader = getClass().getClassLoader();
        File routerClassTextFile = new File(classLoader.getResource("rootRouterClassTemplate.txt").getFile())
        def rootRouterTemaplte = this.rootRouterCreator.createCode(routerClassTextFile)
        routerClassFile.write(rootRouterTemaplte)
    }

    @Override
    void writeMetaRouterClass( File projectRootDir ) {
        def metaRouterClassFile = this.metaRouterCreator.createFile(projectRootDir)
        ClassLoader classLoader = getClass().getClassLoader();
        File routerClassTextFile = new File(classLoader.getResource("metaRouterClassTemplate.txt").getFile())
        def metaRouterTemplate = this.metaRouterCreator.createCode(routerClassTextFile)
        metaRouterClassFile.write(metaRouterTemplate)

    }

    @Override
    void writePingResourceClass( File projectRootDir ) {
        def pingResourceClassFile = this.pingResourceCreator.createFile(projectRootDir)
        ClassLoader classLoader = getClass().getClassLoader();
        File pingResourceTextFile = new File(classLoader.getResource("pingResourceClassTemplate.txt").getFile())
        def pingResourceTemplate = this.pingResourceCreator.createCode(pingResourceTextFile)
        pingResourceClassFile.write(pingResourceTemplate)


    }

    void bootstrapProject( File projectRootDirectory ) {
        //this is a hack because the tooling-api doesnt accept the arguments
        println "./gradlew init --type groovy-library".execute(null, projectRootDirectory).text
    }

    void downloadGradleWrapper( File projectRootDirectory ) {
        def conn = GradleConnector.newConnector().forProjectDirectory(projectRootDirectory).connect()
        try {
            conn.newBuild().forTasks("wrapper").run()
        } finally {
            conn.close();
        }


    }

}
