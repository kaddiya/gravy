package org.kaddiya.gravy.model


class Swagger {
    String swaggerVersion;
    String basePath;
    List<API> apis;
    String models;

    String getModels() {
        return models
    }

    void setModels(String models) {
        this.models = models
    }

    String getSwaggerVersion() {
        return swaggerVersion
    }

    void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion
    }

    String getBasePath() {
        return basePath
    }

    void setBasePath(String basePath) {
        this.basePath = basePath
    }

    List<API> getApis() {
        return apis
    }

    void setApis(List<API> apis) {
        this.apis = apis
    }
}
