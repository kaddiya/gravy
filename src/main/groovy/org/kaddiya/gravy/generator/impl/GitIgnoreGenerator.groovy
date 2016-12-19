package org.kaddiya.gravy.generator.impl
import groovy.text.SimpleTemplateEngine
import groovy.transform.CompileStatic

@CompileStatic
class GitIgnoreGenerator {
    public static final String GIT_IGNORE = ".gitignore"

    File createGitIgnoreFile(File projectRootDir){
        File gitIgnoreFile = new File(projectRootDir, GIT_IGNORE);
        return  gitIgnoreFile
    }

    String createGitIgnoreTemplate(String gitIgnoreText){
        SimpleTemplateEngine simpleTemplateEngine = new SimpleTemplateEngine()
        def gitIgnoreTemplate = simpleTemplateEngine.createTemplate(gitIgnoreText).make();
        return gitIgnoreTemplate.toString()
    }
}
