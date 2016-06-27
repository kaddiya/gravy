package org.kaddiya.gravy.model

enum ConfigurationType {

    EXCLUDE("exclude"), RESOLUTION_STRATEGY("resolutionStrategy")


    private String configTypeValue

    private ConfigurationType(String configTypeValue) {
        this.configTypeValue = configTypeValue
    }

    public getConfigTypeValue() {
        return this.configTypeValue
    }

}
