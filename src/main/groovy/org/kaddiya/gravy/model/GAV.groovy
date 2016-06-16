package org.kaddiya.gravy.model

import groovy.transform.CompileStatic
import groovy.transform.Immutable


@Immutable
@CompileStatic
 class GAV{
    String groupId
    String artifactId
    String version

    String toString(){
        return "'${groupId}:${artifactId}:${version}'"
    }
}
