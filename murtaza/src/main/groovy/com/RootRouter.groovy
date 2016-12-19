package com

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import restling.restlet.RestlingRouter
import com.resources.meta.MetaRouter

@Slf4j
@CompileStatic
class {petId}Router extends RestlingRouter {

    @Override
    void init() throws Exception {
        attachSubRouter('/gotcha', gotchaRouter)
attachSubRouter('/pets', petsRouter)
attachSubRouter('/list', listRouter)
attachSubRouter('/{petId}', {petId}Router)
    }
}
