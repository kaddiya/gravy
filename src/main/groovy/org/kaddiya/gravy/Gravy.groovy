package org.kaddiya.gravy
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import groovy.transform.CompileStatic
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser

import static org.kaddiya.gravy.Constants.*
/**
 * Created by Webonise on 03/11/15.
 */

@CompileStatic
class Gravy {

    public static void main(String[] args ){

        println("Welcome to the Dev Kitchen,the Groovy way.Please help us with your order!")
        assert args.size() >= 1 : "Please provide the recipe to cook!"

        String parentArg = args[0]
        def props = getGravyCookProperties(args)
        switch (parentArg.toUpperCase()){
            case "COOK":

                def gravyPropsModule = new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Map).annotatedWith(Names.named("gravyProps")).toInstance(props)
                    }
                }
                Injector gravyInjector = Guice.createInjector(new GravyModule(gravyPropsModule))
                Initialiser gradleAppInitialiser =  gravyInjector.getInstance(GradleApplicationInitialiser)
                assert System.getProperty("user.dir") : "the current path should not be null"
                def rootDir = gradleAppInitialiser.prepareEnvironment();
                gradleAppInitialiser.writeBuildGradleTemplate(rootDir)
                gradleAppInitialiser.writeWebXmlFile(rootDir)
                gradleAppInitialiser.writeAPI()
                gradleAppInitialiser.writeRootRouterClass(rootDir)
                gradleAppInitialiser.writeServiceModuleClass(rootDir)
               // gradleAppInitialiser.writeMetaRouterClass(rootDir)
                gradleAppInitialiser.writePingResourceClass(rootDir)
                println("Lets bootstrap your application")
                break;
            default:
                throw new IllegalArgumentException("Top level argument not supported")
        }
    }

    static Map<String, String> getGravyCookProperties(String[] args){
        String group
        if(args.size() >= 2){
            group = args[1]
        }
        String projectName
        if(args.size() >= 3){
            projectName = args[2]
        }

        String swaggerFile
        if (args.size() >= 4) {
            swaggerFile = args[3];
        }

        if(!projectName){
            projectName = DEFAULT_PROJECT_NAME
            println"WARN: Projecct name is not provided so creating project with default name foo"
        }

        if(!group){
            group = DEFAULT_GOUP_ID
            println"WARN: Projecct groupId  is not provided so creating project with default name foo"
        }

        Map<String, String> gravyCookProps = new HashMap<>()
        gravyCookProps.putAll([("${PROJECT_NAME_KEY}".toString()): projectName, ("${GROUP_ID_KEY}".toString()): group, ("${SERVICE_MODULE_KEY}".toString()): DEFAULT_SERVICE_MODULE,
                               ("${ROOT_ROUTER_KEY}".toString()) : DEFAULT_ROOT_ROUTER, ("${SWAGGER_FILE}".toString()): swaggerFile])
        return gravyCookProps
    }
}
