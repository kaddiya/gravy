package org.kaddiya.gravy.model

import groovy.transform.Canonical

@Canonical
class Repository {

    String name
    String description
    String url

    String toString() {
        if (name && !description && !url) {
            return "${name}()".toString()
        } else {
            assert url: "URL can not be null or empty"
            assert description: " Description can not be empty or null"
            return "${name}{ \n name = ${description} \n url = ${url}".toString()
        }
    }

}
