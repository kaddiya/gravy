package org.kaddiya.gravy.model

import groovy.transform.CompileStatic

@CompileStatic
class Parameter {
    String name;
    String description;
    boolean required;
    String type;
    String paramType;

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    boolean getRequired() {
        return required
    }

    void setRequired(boolean required) {
        this.required = required
    }

    String getParamType() {
        return paramType
    }

    void setParamType(String paramType) {
        this.paramType = paramType
    }
}
