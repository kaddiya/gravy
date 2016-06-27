package org.kaddiya.gravy

import static org.kaddiya.gravy.Constants.GROUP_ID_KEY
import static org.kaddiya.gravy.Constants.PROJECT_NAME_KEY
import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY
import static org.kaddiya.gravy.Constants.SERVICE_MODULE_KEY
import static org.kaddiya.gravy.Constants.DEFAULT_GOUP_ID
import static org.kaddiya.gravy.Constants.DEFAULT_PROJECT_NAME
import static org.kaddiya.gravy.Constants.DEFAULT_ROOT_ROUTER
import static org.kaddiya.gravy.Constants.DEFAULT_SERVICE_MODULE


trait CodeGenerator {
    Map<String, String> getGravyPropMap() {
        return [ ( "${ PROJECT_NAME_KEY }".toString() ): DEFAULT_PROJECT_NAME, ( "${ GROUP_ID_KEY }".toString() ): DEFAULT_GOUP_ID, ( "${ SERVICE_MODULE_KEY }".toString() ): DEFAULT_SERVICE_MODULE,
                                ( "${ ROOT_ROUTER_KEY }".toString() ) : DEFAULT_ROOT_ROUTER ]
    }

}
