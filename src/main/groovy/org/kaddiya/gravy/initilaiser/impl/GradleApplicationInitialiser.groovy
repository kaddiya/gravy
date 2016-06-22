package org.kaddiya.gravy.initilaiser.impl

import java.nio.file.Paths

import com.google.inject.Inject
import com.google.inject.name.Named
import org.gradle.tooling.GradleConnector
import static org.kaddiya.gravy.Constants.GROUP_ID_KEY
import static org.kaddiya.gravy.Constants.GROUP_PACKAGE_KEY
import static org.kaddiya.gravy.Constants.PROJECT_NAME_KEY
import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY
import static org.kaddiya.gravy.Constants.SERVICE_MODULE_KEY

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

class GradleApplicationInitialiser implements Initialiser {

    private final PluginGeneratorImpl pluginCreator
    private final DependencyGeneratorImpl dependancyCreator
    private final BuildScriptGeneratorImpl buildScriptCreator
    private final RepositoryGeneratorImpl repositoryCreator
    private final WebXmlCreator webXmlCreator
    private final ServiceModuleGenerator serviceModuleCreator
    private final RootRouterGenerator  rootRouterCreator
    private final MetaRouterGenerator metaRouterCreator
    private final PingResourceGenerator pingResourceCreator
    private final String GRADLE_BUILD_TEMPLATE = "gradleBuildTemplate.txt";
    private final String WEB_XML_TEMPLATE = "webXmlTemplate.txt"
    private final String SERVICE_MODULE_CLASS_TEMPLATE = "serviceModuleClassTemplate.txt"
    private final String ROOT_ROUTER_CLASS_TEMPLATE = "rootRouterClassTemplate.txt"
    private final String META_ROUTER_CLASS_TEMPLATE = "metaRouterClassTemplate.txt"
    private final String PING_RESOURCE_CLASS_TEMPLATE = "pingResourceClassTemplate.txt"
    private final File gradleBuildTemplateFile
    private final File webXmlTemplateFile
    private final File serviceModuleTemplateFile
    private final File rootRouterTemplateFile
    private final File metaRouterTemplateFile
    private final File pingResourceTemplateFile
    private final Map<String, String> gravyProps


    @Inject
    GradleApplicationInitialiser( PluginGeneratorImpl pluginCreator, DependencyGeneratorImpl dependancyCreator,
                                  BuildScriptGeneratorImpl buildScriptCreator, WebXmlCreator webXmlCreator,
                                  RepositoryGeneratorImpl repositoryCreator, RootRouterGenerator rootRouterCreator,
                                  ServiceModuleGenerator serviceModuleCreator, MetaRouterGenerator metaRouterCreator,
                                  PingResourceGenerator pingResourceCreator, @Named("gravyProps")Map gravyProps) {
        this.pluginCreator = pluginCreator
        this.dependancyCreator = dependancyCreator
        this.buildScriptCreator = buildScriptCreator
        this.repositoryCreator = repositoryCreator
        this.webXmlCreator = webXmlCreator
        this.serviceModuleCreator = serviceModuleCreator
        this.rootRouterCreator = rootRouterCreator
        this.metaRouterCreator = metaRouterCreator
        this.pingResourceCreator = pingResourceCreator

        this.gravyProps = gravyProps

        ClassLoader classLoader = getClass().getClassLoader();
        gradleBuildTemplateFile = new File(classLoader.getResource(GRADLE_BUILD_TEMPLATE).getFile())
        webXmlTemplateFile = new File(classLoader.getResource(WEB_XML_TEMPLATE).getFile())
        serviceModuleTemplateFile = new File(classLoader.getResource(SERVICE_MODULE_CLASS_TEMPLATE).getFile())
        metaRouterTemplateFile = new File(classLoader.getResource(META_ROUTER_CLASS_TEMPLATE).getFile())
        rootRouterTemplateFile = new File(classLoader.getResource(ROOT_ROUTER_CLASS_TEMPLATE).getFile())
        pingResourceTemplateFile = new File(classLoader.getResource(PING_RESOURCE_CLASS_TEMPLATE).getFile())

    }

    @Override
    File prepareEnvironment() {
        String applicationName = this.gravyProps.get(PROJECT_NAME_KEY)
        String currentPath = System.getProperty("user.dir")
        assert currentPath: "Please specify the path"
        File projectRootDirectory = new File(currentPath, applicationName)
        if ( projectRootDirectory.exists() ) {
            projectRootDirectory.deleteDir()
        }
        projectRootDirectory.mkdir()
        downloadGradleWrapper(projectRootDirectory)
        bootstrapProject(projectRootDirectory)
        String groupPackage = this.gravyProps.get(GROUP_ID_KEY).replace(".", "/")
        String groovyPath = "/src/main/groovy/${groupPackage}"

        def groovyDir = Paths.get(projectRootDirectory.toString()+groovyPath).toFile()
        groovyDir.mkdirs()
        return projectRootDirectory
    }

    @Override
    void writeBuildGradleTemplate( File projectRootDir ) {

        File file = new File(projectRootDir, "build.gradle")
        def teamplateBinding = new LinkedHashMap<String, String>()
        teamplateBinding.putAll(this.pluginCreator.getModelBinding())
        teamplateBinding.putAll(this.repositoryCreator.getModelBinding())
        teamplateBinding.putAll(this.dependancyCreator.getModelBinding())
        teamplateBinding.putAll(["buildScript" : this.buildScriptCreator.getBuildScriptList().join("\n")])

        def engine1 = new groovy.text.GStringTemplateEngine()
        def template = engine1.createTemplate(gradleBuildTemplateFile).make(teamplateBinding)
        file.write(template.toString())
    }

    @Override
    void writeWebXmlFile( File projectRootDir) {
        def webXmlFile = webXmlCreator.createWebxmlFile(projectRootDir)
        def webXmlTemplate = webXmlCreator.createXmlTemplate(webXmlTemplateFile, ["serviceModule" : this.gravyProps.get(SERVICE_MODULE_KEY), "groupId" : this.gravyProps.get(GROUP_ID_KEY)])
        webXmlFile.write(webXmlTemplate)
    }

    @Override
    void writeServiceModuleClass( File projectRootDir) {
        def serviceClassFile = this.serviceModuleCreator.createFile(projectRootDir)
        def serviceClassTemplate = this.serviceModuleCreator.createCode(serviceModuleTemplateFile)
        serviceClassFile.write(serviceClassTemplate)
    }

    @Override
    void writeRootRouterClass( File projectRootDir) {
        def routerClassFile = this.rootRouterCreator.createFile(projectRootDir)
        def rootRouterTemaplte = this.rootRouterCreator.createCode(rootRouterTemplateFile)
        routerClassFile.write(rootRouterTemaplte)
    }

    @Override
    void writeMetaRouterClass( File projectRootDir ) {
        def metaRouterClassFile = this.metaRouterCreator.createFile(projectRootDir)
        def metaRouterTemplate = this.metaRouterCreator.createCode(metaRouterTemplateFile)
        metaRouterClassFile.write(metaRouterTemplate)

    }

    @Override
    void writePingResourceClass( File projectRootDir ) {
        def pingResourceClassFile = this.pingResourceCreator.createFile(projectRootDir)
        def pingResourceTemplate = this.pingResourceCreator.createCode(pingResourceTemplateFile)
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
