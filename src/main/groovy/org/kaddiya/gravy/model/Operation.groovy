package org.kaddiya.gravy.model

import groovy.transform.CompileStatic

@CompileStatic
class Operation {
    String method
    String summary
    String type;
    String nickname
    List<Parameter> parameters

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    String getNickname() {
        return nickname
    }

    void setNickname(String nickname) {
        this.nickname = nickname
    }

    List<Parameter> getParameters() {
        return parameters
    }

    void setParameters(List<Parameter> parameters) {
        this.parameters = parameters
    }

    String getMethod() {
        return method
    }

    void setMethod(String method) {
        this.method = method
    }

    String getSummary() {
        return summary
    }

    void setSummary(String summary) {
        this.summary = summary
    }
}
