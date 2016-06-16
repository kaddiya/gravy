package org.kaddiya.gravy.model

import groovy.transform.Canonical
import groovy.transform.Immutable

@Immutable
class Dependency {

     BuildPhaseType phaseType;
     GAV gav;
     boolean isBuildScript

    String toString() {
        assert gav: "GroupId, ArtifactId or version can not be null or empty"
        assert this.phaseType: "BuildPhaseType should not be null orempty"
        return !isBuildScript? "${phaseType.getPhaseTypeValue()} ${gav.toString()}".toString() :
                "classpath ${gav.toString()}".toString()
    }
}
