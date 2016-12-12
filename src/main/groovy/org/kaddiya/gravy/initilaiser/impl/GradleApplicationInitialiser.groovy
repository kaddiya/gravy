package org.kaddiya.gravy.initilaiser.impl

import java.nio.file.Paths

import com.google.inject.Inject
import com.google.inject.name.Named
import groovy.json.JsonSlurper
import org.gradle.tooling.GradleConnector
import org.kaddiya.gravy.generator.impl.*
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.model.API
import org.kaddiya.gravy.model.Swagger
import java.nio.file.Paths

import static org.kaddiya.gravy.Constants.*

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
    private final String gradleBuildTemplate
    private final String webXmlTemplate
    private final String serviceModuleTemplate
    private final String rootRouterTemplate
    private final String metaRouterTemplate
    private final String pingResourceTemplate
    private final Map<String, String> gravyProps
    private final ConfigurationsGeneratorImpl configurationsGenerator



    @Inject
    GradleApplicationInitialiser( PluginGeneratorImpl pluginCreator, DependencyGeneratorImpl dependancyCreator,
                                  BuildScriptGeneratorImpl buildScriptCreator, WebXmlCreator webXmlCreator,
                                  RepositoryGeneratorImpl repositoryCreator, RootRouterGenerator rootRouterCreator,
                                  ServiceModuleGenerator serviceModuleCreator, MetaRouterGenerator metaRouterCreator,
                                  PingResourceGenerator pingResourceCreator, @Named("gravyProps")Map gravyProps,
                                  ConfigurationsGeneratorImpl configurationsGenerator) {

        this.pluginCreator = pluginCreator
        this.dependancyCreator = dependancyCreator
        this.buildScriptCreator = buildScriptCreator
        this.repositoryCreator = repositoryCreator
        this.webXmlCreator = webXmlCreator
        this.serviceModuleCreator = serviceModuleCreator
        this.rootRouterCreator = rootRouterCreator
        this.metaRouterCreator = metaRouterCreator
        this.pingResourceCreator = pingResourceCreator
        this.configurationsGenerator = configurationsGenerator

        this.gravyProps = gravyProps

        ClassLoader classLoader = getClass().getClassLoader();
        gradleBuildTemplate = getResourceFileFromLoader(classLoader, GRADLE_BUILD_TEMPLATE)
        webXmlTemplate = getResourceFileFromLoader(classLoader, WEB_XML_TEMPLATE)
        serviceModuleTemplate = getResourceFileFromLoader(classLoader, SERVICE_MODULE_CLASS_TEMPLATE)
        metaRouterTemplate = getResourceFileFromLoader(classLoader, META_ROUTER_CLASS_TEMPLATE)
        rootRouterTemplate = getResourceFileFromLoader(classLoader, ROOT_ROUTER_CLASS_TEMPLATE)
        pingResourceTemplate = getResourceFileFromLoader(classLoader, PING_RESOURCE_CLASS_TEMPLATE)

    }

    private String getResourceFileFromLoader(ClassLoader classLoader, String fileName){
        def resourceStrem = classLoader.getResourceAsStream(fileName)
        return resourceStrem.text
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
        def libFilePath = "/src/main/groovy/Library.groovy"
        deleteFileIfExist(projectRootDirectory, libFilePath)
        def libTestFilePath = "/src/test/groovy/LibraryTest.groovy"
        deleteFileIfExist(projectRootDirectory, libTestFilePath)
        return projectRootDirectory
    }

    private void deleteFileIfExist(File rootDir, String filePath){
        File fileToDelete = new File(rootDir, Paths.get(filePath).toFile().toString())
        if(fileToDelete.exists()){
            fileToDelete.delete()
        }
    }

    @Override
    void writeBuildGradleTemplate( File projectRootDir ) {

        File file = new File(projectRootDir, "build.gradle")
        def teamplateBinding = new LinkedHashMap<String, String>()
        teamplateBinding.putAll(this.pluginCreator.getModelBinding())
        teamplateBinding.putAll(this.repositoryCreator.getModelBinding())
        teamplateBinding.putAll(this.dependancyCreator.getModelBinding())
        teamplateBinding.putAll(this.configurationsGenerator.getModelBinding())
        teamplateBinding.putAll(["buildScript" : this.buildScriptCreator.getBuildScriptList().join("\n")])

        def engine1 = new groovy.text.GStringTemplateEngine()
        def template = engine1.createTemplate(gradleBuildTemplate).make(teamplateBinding)
        file.write(template.toString())
    }

    @Override
    void writeWebXmlFile( File projectRootDir) {
        def webXmlFile = webXmlCreator.createWebxmlFile(projectRootDir)
        def webXmlTemplate = webXmlCreator.createXmlTemplate(webXmlTemplate, ["serviceModule" : this.gravyProps.get(SERVICE_MODULE_KEY), "groupId" : this.gravyProps.get(GROUP_ID_KEY)])
        webXmlFile.write(webXmlTemplate)
    }

    @Override
    void writeServiceModuleClass( File projectRootDir) {
        def serviceClassFile = this.serviceModuleCreator.createFile(projectRootDir)
        def serviceClassTemplate = this.serviceModuleCreator.createCode(serviceModuleTemplate)
        serviceClassFile.write(serviceClassTemplate)
    }

    @Override
    void writeRootRouterClass( File projectRootDir) {
        def routerClassFile = this.rootRouterCreator.createFile(projectRootDir)
        def rootRouterTemaplte = this.rootRouterCreator.createCode(rootRouterTemplate)
        routerClassFile.write(rootRouterTemaplte)
    }

    @Override
    void writeMetaRouterClass( File projectRootDir ) {
        def metaRouterClassFile = this.metaRouterCreator.createFile(projectRootDir)
        def metaRouterTemplate = this.metaRouterCreator.createCode(metaRouterTemplate)
        metaRouterClassFile.write(metaRouterTemplate)

    }

    @Override
    void writePingResourceClass( File projectRootDir ) {
        def pingResourceClassFile = this.pingResourceCreator.createFile(projectRootDir)
        def pingResourceTemplate = this.pingResourceCreator.createCode(pingResourceTemplate)
        pingResourceClassFile.write(pingResourceTemplate)
    }

    @Override
    void writeAPI() {
        File swaggerFile = Paths.get(System.properties['user.dir'], this.gravyProps.get(SWAGGER_FILE)).toFile()
        print swaggerFile.text
        parseSwaggerFile(swaggerFile);
    }

    private void parseSwaggerFile(File swaggerFile) {
        def jsonSlurper = new JsonSlurper();
        def swagger = (Swagger)jsonSlurper.parse(swaggerFile)
        List<API> apiList = swagger.apis;
        apiList.each {
            api ->
                def Api = new API(api.operations, api.path)
                print Api.toString();
        }

    }

    void bootstrapProject( File projectRootDirectory ) {
        String command;
        if (System.properties['os.name'].toString().toLowerCase().contains('windows')) {
            command = "cmd /c gradlew.bat init --type groovy-library"
        } else {
            command = "./gradlew init --type groovy-library"
        }
        println command.execute(null, projectRootDirectory).text
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
