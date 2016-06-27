package org.kaddiya.gravy.model

import groovy.transform.CompileStatic


@CompileStatic
class GravyProject {

    private File gravyBuildGradle
    private List<Plugin> plugins = new ArrayList<>();
    private List<Dependency> dependencies = new ArrayList<>()
    List<String> lines = new ArrayList<>()

    GravyProject( File buildGradle ) {
        this.gravyBuildGradle = buildGradle
       // lines = this.gravyBuildGradle.readLines()
    }

    void writePlugins( List<String> plugins ) {
        int indexOfFirstPlugin = this.lines.findLastIndexOf { String line -> return line.contains("apply plugin:") }
        if ( indexOfFirstPlugin < 0 ) {
            indexOfFirstPlugin = 0
        }else {
            ++indexOfFirstPlugin
        }
        this.lines.addAll(indexOfFirstPlugin, plugins)
    }

    void writeDependencies( List<String> dependencies ) {
        List<String> list = dependencies.collect { String dependency -> "    " + dependency.toString() }
        this.lines.addAll(list)
    }

    void writeRepositories( List<String> repositories ) {
        this.lines.addAll(repositories)
    }

    void writeBuildScript( List<String> buildScripts ) {
        this.lines.addAll(buildScripts)
    }

    void writeFile(){
        this.gravyBuildGradle.write(this.lines.join("\n"))
    }



}
