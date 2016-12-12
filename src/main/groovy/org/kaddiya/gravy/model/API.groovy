package org.kaddiya.gravy.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class API {
    String path;
    List<Operation> operations

    API(List<Operation> operations, String path) {
        this.operations = operations
        this.path = path
    }

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }

    List<Operation> getOperations() {
        return operations
    }

    void setOperations(List<Operation> operations) {
        this.operations = operations
    }

}
