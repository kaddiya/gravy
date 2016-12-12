package org.kaddiya.gravy.initilaiser;


import java.io.File;

/**
 * Created by Webonise on 03/11/15.
 */
public interface Initialiser {

    /**
     * This is the method that prepare the environment.Any prerequisites are supposed to be downloaded and prepped
     */
    File prepareEnvironment();

    void writeBuildGradleTemplate(File projectRootDir);

    void writeWebXmlFile(File projectRootDir);

    void writeServiceModuleClass(File projRootDir);

    void writeRootRouterClass(File projRootDir);

    void writeMetaRouterClass(File projectRootDir);

    void writePingResourceClass(File projectRootDir);

    void writeAPI();
}
