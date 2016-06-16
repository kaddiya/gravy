package org.kaddiya.gravy.initilaiser.impl

import com.google.inject.Inject
import org.gradle.tooling.GradleConnector
import org.kaddiya.gravy.creator.BuildScriptCreator
import org.kaddiya.gravy.creator.DependencyCreator
import org.kaddiya.gravy.creator.PluginCreator
import org.kaddiya.gravy.creator.RepositoryCreator
import org.kaddiya.gravy.creator.impl.BuildScriptCreatorImpl
import org.kaddiya.gravy.creator.impl.DependencyCreatorImpl
import org.kaddiya.gravy.creator.impl.PluginCreatorImpl
import org.kaddiya.gravy.creator.impl.RepositoryCreatorImpl
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.model.GravyProject

/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitialiser implements Initialiser {

    private PluginCreatorImpl pluginCreator
    private DependencyCreatorImpl dependancyCreator
    private BuildScriptCreatorImpl buildScriptCreator
    private RepositoryCreatorImpl repositoryCreator

    def GradleApplicationInitialiser() {
        super()
    }

    @Inject
    GradleApplicationInitialiser( PluginCreator pluginCreator, DependencyCreator dependancyCreator,
                                  BuildScriptCreator buildScriptCreator, RepositoryCreator repositoryCreator ) {
        this.pluginCreator = pluginCreator
        this.dependancyCreator = dependancyCreator
        this.buildScriptCreator = buildScriptCreator
        this.repositoryCreator = repositoryCreator
    }

    @Override
    File prepareEnvironment( String[] args ) {
        assert args.size() == 3: "Invalid arguments"
        String applicationName = args[1]
        String groupPackageName = args[2]
        String currentPath = System.getProperty("user.dir")
        assert currentPath: "Please specify the path"
        File projectRootDirectory = new File(currentPath, applicationName)

        if ( projectRootDirectory.exists() ) {
            projectRootDirectory.deleteDir()
        }
        projectRootDirectory.mkdir()

        downloadGradleWrapper(projectRootDirectory)
        bootstrapProject(projectRootDirectory)
        File mainDirectory = new File(new File(projectRootDirectory, "src"), "main")
        File javaDirectory = new File(mainDirectory, "java")
        javaDirectory.mkdir()
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
        //print(template.toString())
        file.write(template.toString())


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
