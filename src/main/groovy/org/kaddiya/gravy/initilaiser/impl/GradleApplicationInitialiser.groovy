package org.kaddiya.gravy.initilaiser.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import io.swagger.models.Path
import io.swagger.models.Swagger
import io.swagger.parser.SwaggerParser
import org.apache.commons.lang3.StringUtils
import org.gradle.tooling.GradleConnector
import org.kaddiya.gravy.generator.impl.BuildScriptGeneratorImpl
import org.kaddiya.gravy.generator.impl.ConfigurationsGeneratorImpl
import org.kaddiya.gravy.generator.impl.DependencyGeneratorImpl
import org.kaddiya.gravy.generator.impl.MetaRouterGenerator
import org.kaddiya.gravy.generator.impl.PingResourceGenerator
import org.kaddiya.gravy.generator.impl.PluginGeneratorImpl
import org.kaddiya.gravy.generator.impl.RepositoryGeneratorImpl
import org.kaddiya.gravy.generator.impl.RootRouterGenerator
import org.kaddiya.gravy.generator.impl.ServiceModuleGenerator
import org.kaddiya.gravy.generator.impl.WebXmlCreator
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.predefined.Node
import org.saur.initilaiser.TreeParser

import java.nio.file.Paths

import static org.kaddiya.gravy.Constants.*
import static org.kaddiya.gravy.Constants.GROUP_ID_KEY
import static org.kaddiya.gravy.Constants.PROJECT_NAME_KEY
import static org.kaddiya.gravy.Constants.REGEX
import static org.kaddiya.gravy.Constants.ROUTER_PACKAGE
import static org.kaddiya.gravy.Constants.SERVICE_MODULE_KEY
import static org.kaddiya.gravy.Constants.SWAGGER_FILE

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
    private Swagger swagger
    private HashMap<String, Path> paths
    private File projectRootDir;

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
        def serviceClassFile = this.serviceModuleCreator.createFile(projectRootDir, "")
        def serviceClassTemplate = this.serviceModuleCreator.createCode(serviceModuleTemplate)
        serviceClassFile.write(serviceClassTemplate)
    }

    @Override
    void writeRootRouterClass(File projectRootDir) {
        List <org.saur.model.Node> nodeList = TreeParser.parseTree(paths.keySet().asList()).values().asList();
        List<org.saur.model.Node> rootNodes = nodeList.findAll {node -> node.path.equals(node.pathName)}
        this.projectRootDir = projectRootDir
        def routerClassFile = this.rootRouterCreator.createFile(projectRootDir, DEFAULT_ROOT_ROUTER, ROUTER_PACKAGE)
        rootNodes.forEach {node -> this.rootRouterCreator.createBindings(node)}
        def rootRouterTemplate = this.rootRouterCreator.createCode(rootRouterTemplate)
        routerClassFile.write(rootRouterTemplate)
        writeMetaRouterClass(rootNodes)
    }

    void writeMetaRouterClass(List<Node> rootNodes) {
        this.rootRouterCreator.resetBindings()
        rootNodes.forEach { node ->
            String fileName;
            String packageName;
            if (node.hasChildren()) {
                fileName = StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                        .capitalize(ROUTER_PACKAGE)
                packageName = ROUTER_PACKAGE;
            } else {
                fileName = StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                        .capitalize(RESOURCE_PACKAGE)
                packageName = RESOURCE_PACKAGE;
            }
            def routerClassFile = this.rootRouterCreator.createFile(projectRootDir, fileName, packageName)
            List<org.saur.model.Node> childNodes = node.getChildren().values().asList()
            def metaRouterTemplate
            if (childNodes.isEmpty()) {
                metaRouterTemplate = this.rootRouterCreator.createCode(pingResourceTemplate)
            } else {
                childNodes.forEach { childNode -> this.rootRouterCreator.createBindings(childNode) }
                metaRouterTemplate = this.rootRouterCreator.createCode(rootRouterTemplate)
            }

            routerClassFile.write(metaRouterTemplate)
            writeMetaRouterClass(childNodes)

        }
    }

    @Override
    void writePingResourceClass( File projectRootDir ) {
        def pingResourceClassFile = this.pingResourceCreator.createFile(projectRootDir)
        def pingResourceTemplate = this.pingResourceCreator.createCode(pingResourceTemplate)
        pingResourceClassFile.write(pingResourceTemplate)
    }

    @Override
    void writeAPI() {
        String swaggerFilePath = Paths.get(System.properties['user.dir'], this.gravyProps.get(SWAGGER_FILE)).toString()
        print swaggerFilePath
        swagger = new SwaggerParser().read(swaggerFilePath);
        paths = swagger.getPaths();
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
