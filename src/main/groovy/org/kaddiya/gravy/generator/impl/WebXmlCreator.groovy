package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.Constants.FORMATTER

import java.nio.file.Paths

import groovy.transform.CompileStatic

@CompileStatic
class WebXmlCreator {
    static final String WEB_XML_PATH = "/src/main/webapp/WEB-INF/web.xml"

    private File webXmlFile

    File createWebxmlFile(File projectRootDir){

        File webXmlFile = new File(projectRootDir, Paths.get(WEB_XML_PATH).toFile().toString())
        webXmlFile.getParentFile().mkdirs()
        webXmlFile.createNewFile()
        return  webXmlFile
    }

    String createXmlTemplate(String xmlTextFile, Map<String, String> binding ){
        def xmlEngine = new groovy.text.XmlTemplateEngine()
        xmlEngine.indentation = FORMATTER
        def webXmlTemplate = xmlEngine.createTemplate(xmlTextFile).make(binding)
        return webXmlTemplate.toString()
    }
}
