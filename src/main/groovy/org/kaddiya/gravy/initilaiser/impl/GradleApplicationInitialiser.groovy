package org.kaddiya.gravy.initilaiser.impl

import groovy.transform.CompileStatic
import org.gradle.api.Task
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ModelBuilder
import org.gradle.tooling.model.GradleProject
import org.kaddiya.gravy.initilaiser.Initialiser


/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitialiser  implements  Initialiser{


    def GradleApplicationInitialiser() {
        super()
    }

    @Override
    File prepareEnvironment(String[] args) {
        assert args.size() == 2 :"Invalid arguments"
        String applicationName = args[1]
        String currentPath = System.getProperty("user.dir")
        assert currentPath : "Please specify the path"
        File projectRootDirectory =  new File(currentPath,applicationName)

        if(projectRootDirectory.exists()){
            projectRootDirectory.deleteDir()
        }
        projectRootDirectory.mkdir()

        downloadGradleWrapper(projectRootDirectory)
        bootstrapProject(projectRootDirectory)


        return projectRootDirectory
    }

    void bootstrapProject(File projectRootDirectory){
        //this is a hack because the tooling-api doesnt accept the arguments
        println "./gradlew init --type groovy-library".execute(null,projectRootDirectory).text
    }

    void downloadGradleWrapper(File projectRootDirectory){
        def conn = GradleConnector.newConnector().forProjectDirectory(projectRootDirectory).connect()
        try {
            conn.newBuild().forTasks("wrapper").run()
        } finally {
            conn.close();
        }


    }

}
