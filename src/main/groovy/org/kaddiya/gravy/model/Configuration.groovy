package org.kaddiya.gravy.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Configuration {

    ConfigurationType  configurationType
    Dependency dependency

    @Override
    public String toString() {
        return "${BuildPhaseType.ALL.phaseTypeValue}.${configurationType.configTypeValue} ${dependency.toString()}"
    }
}
