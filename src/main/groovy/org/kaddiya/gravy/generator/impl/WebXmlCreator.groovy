package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.Constants.FORMATTER

import groovy.transform.CompileStatic

@CompileStatic
class WebXmlCreator {


    private File webXmlFile

    File createWebxmlFile(File projectRootDir){
        def mainDir =   new File(new File(projectRootDir, "src"), "main")
        def webAppDir = new File(mainDir, "webapp")
        if(webAppDir.exists()){
            webAppDir.delete()
        }
        webAppDir.mkdir()
        def webInfFolder = new File(webAppDir, "WEB_INF")
        webInfFolder.mkdir()
        return  new File(webInfFolder, "web.xml")
    }

    String createXmlTemplate(File xmlTextFile, Map<String, String> binding ){
        def xmlEngine = new groovy.text.XmlTemplateEngine()
        xmlEngine.indentation = FORMATTER
        def webXmlTemplate = xmlEngine.createTemplate(xmlTextFile).make(binding)
        return webXmlTemplate.toString()
    }
}
