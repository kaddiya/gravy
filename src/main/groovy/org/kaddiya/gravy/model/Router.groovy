package org.kaddiya.gravy.model;

import groovy.transform.Canonical;

@Canonical
class Router {
    String path
    String pathRouter

    String toString() {
        assert path: "URL can not be null or empty"
        assert pathRouter: " Description can not be empty or null"
        String methodName = pathRouter.contains("Resource") ? "attachRouter" : "attachSubRouter"
        return "$methodName('$path', $pathRouter)".toString()
    }
}
