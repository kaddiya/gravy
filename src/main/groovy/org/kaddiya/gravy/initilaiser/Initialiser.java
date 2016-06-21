package org.kaddiya.gravy.initilaiser;


import java.io.File;

/**
 * Created by Webonise on 03/11/15.
 */
public interface Initialiser {

    /**
     * This is the method that prepare the environment.Any prerequisites are supposed to be downloaded and prepped
     */
    File prepareEnvironment(String[] args);

    void writeBuildGradleFile(File projectRootDir);

    void writeBuildGradleTemplate(File projectRootDir);

    void writeWebXmlFile(File projectRootDir, String apiModuleClassName);

    void writeServiceModuleClass(File projRootDir, String className, String rootRouterClassName);

    void writeRootRouterClass(File projRootDir, String className);

    void writeMetaRouterClass(File projectRootDir);

    void writePingResourceClass(File projectRootDir);
}
