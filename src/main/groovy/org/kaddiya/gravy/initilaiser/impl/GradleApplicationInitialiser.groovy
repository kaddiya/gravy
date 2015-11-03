package org.kaddiya.gravy.initilaiser.impl

import org.gradle.tooling.GradleConnector
import org.kaddiya.gravy.initilaiser.Initialiser

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by Webonise on 03/11/15.
 */
class GradleApplicationInitialiser  implements  Initialiser{


    @Override
    Path prepareEnvironment(String currentPath) {
        Path projectRootDirectory =  Paths.get(currentPath)
        downloadGradleWrapper(currentPath)
        return projectRootDirectory
    }


    File downloadGradleWrapper(currentPath){
        def conn = GradleConnector.newConnector().forProjectDirectory(new File(currentPath)).connect()

        try {
            conn.newBuild().forTasks("tasks").run();
        } finally {
            conn.close();
        }

    }

}
