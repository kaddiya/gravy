package org.kaddiya.gravy.generator

import static org.kaddiya.gravy.Constants.GROUP_ID_KEY
import static org.kaddiya.gravy.Constants.GROUP_PACKAGE_KEY
import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY
import static org.kaddiya.gravy.Constants.SERVICE_MODULE_KEY

import java.nio.file.Paths

import groovy.text.SimpleTemplateEngine
import groovy.text.Template


abstract class AbstractCodeGenerator {

    private Template filePathTemplate
    private SimpleTemplateEngine simpleTemplateEngine
    private Map<String, String> gravyProps
    protected Map<String, String> serviceModuleBinding
    protected Map<String, String> groupIdBinding
    protected Map<String, String> groupPackageBinding
    protected Map<String, String> rootRouterBinding

    AbstractCodeGenerator(String codeFilePath, Map<String, String> gravyProps) {
        simpleTemplateEngine = new SimpleTemplateEngine()
        filePathTemplate = simpleTemplateEngine.createTemplate(codeFilePath)
        this.gravyProps = gravyProps
        populateBindings()
    }


    protected File generateFile(File projRootDir, Map<String, String> binding){
        def generatedFilePath = filePathTemplate.make(binding).toString()
        generatedFilePath = Paths.get(generatedFilePath).toFile().toString()
        def generatedFile = new File(projRootDir, generatedFilePath)
        generatedFile.getParentFile().mkdirs()
        generatedFile.createNewFile()
        return generatedFile
    }

    protected String generateCode(String template,  Map<String, String> binding){
        return simpleTemplateEngine.createTemplate(template).make(binding).toString()

    }

    abstract protected File createFile(File projRootDir);
    abstract protected String createCode(String metaRouterTemplate);

    protected String groupPackage(String groupId){
        return groupId.replace(".", "/")
    }

    private void populateBindings() {
        String groupIdValue = this.gravyProps.get(GROUP_ID_KEY)
        this.groupIdBinding = [("${GROUP_ID_KEY}".toString()):this.groupPackage(groupIdValue)]
        this.serviceModuleBinding =[("${SERVICE_MODULE_KEY}".toString()) : this.gravyProps.get(SERVICE_MODULE_KEY)]
        this.rootRouterBinding = [("${ROOT_ROUTER_KEY}".toString()) :this.gravyProps.get(ROOT_ROUTER_KEY)]
        this.groupPackageBinding = [("${GROUP_PACKAGE_KEY}".toString()): groupIdValue]
    }
}
