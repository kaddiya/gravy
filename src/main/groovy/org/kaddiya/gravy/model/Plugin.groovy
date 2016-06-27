package org.kaddiya.gravy.model

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@Immutable
@CompileStatic
class Plugin {
    private static final String APPLY_PUGIN = "apply plugin:"

    String pluginName

    String toString() {
        return "${APPLY_PUGIN} '${pluginName}'".toString()
    }
}
