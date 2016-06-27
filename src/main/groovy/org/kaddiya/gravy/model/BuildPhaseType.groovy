package org.kaddiya.gravy.model

import groovy.transform.CompileStatic

@CompileStatic
enum BuildPhaseType {

    COMPILE("compile"), TEST_COMPILE("testCompile"), RUNTIME("runtime"),
    GRETTY_RUNNER_TOMCAT8("grettyRunnerTomcat8"), TEST_RUNTIME("testRuntime"),
    CLASSPATH("classpath"), ALL("all*")

    private String phaseTypeValue

    private BuildPhaseType(String phaseTypeValue) {
        this.phaseTypeValue = phaseTypeValue
    }

    public getPhaseTypeValue() {
        return this.phaseTypeValue
    }

}
