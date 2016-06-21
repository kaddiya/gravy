package org.kaddiya.gravy

import static org.kaddiya.gravy.Constants.DEFAULT_GOUP_ID
import static org.kaddiya.gravy.Constants.DEFAULT_PROJECT_NAME
import static org.kaddiya.gravy.Constants.DEFAULT_ROOT_ROUTER
import static org.kaddiya.gravy.Constants.DEFAULT_SERVICE_MODULE
import static org.kaddiya.gravy.Constants.GROUP_ID_KEY
import static org.kaddiya.gravy.Constants.PROJECT_NAME_KEY
import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY
import static org.kaddiya.gravy.Constants.SERVICE_MODULE_KEY
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import groovy.transform.CompileStatic
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser

/**
 * Created by Webonise on 03/11/15.
 */

@CompileStatic
class Gravy {

    public static void main(String[]args ){

        println("Welcome to the Dev Kitchen,the Groovy way.Please help us with your order!")
        assert args.size() >= 1 : "Please provide the recipe to cook!"

        String parentArg = args[0]
        switch (parentArg.toUpperCase()){
            case "COOK":
                def props = getGravyCookProperties()
                def gravyPropsModule = new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Map).annotatedWith(Names.named("gravyProps")).toInstance(props)
                    }
                }
                Injector gravyInjector = Guice.createInjector(new GravyModule(gravyPropsModule))
                Initialiser gradleAppInitialiser =  gravyInjector.getInstance(GradleApplicationInitialiser)
                assert System.getProperty("user.dir") : "the current path should not be null"
                def rootDir = gradleAppInitialiser.prepareEnvironment(args);
                gradleAppInitialiser.writeBuildGradleTemplate(rootDir)
                gradleAppInitialiser.writeWebXmlFile(rootDir,"ServiceModule")
                gradleAppInitialiser.writeRootRouterClass(rootDir, "RootRouter")
                gradleAppInitialiser.writeServiceModuleClass(
                        rootDir, "ServiceModule", "MainRouter"
                )
                gradleAppInitialiser.writeMetaRouterClass(rootDir)
                gradleAppInitialiser.writePingResourceClass(rootDir)
                println("Lets bootstrap your application")
                break;
            default:
                throw new IllegalArgumentException("Top level argument not supported")
        }
    }

    static Map<String, String> getGravyCookProperties(){

        Map<String, String> gravyCookProps = new HashMap<>()
        /*Scanner scanner = new Scanner(System.in)
        println"Please enter project name"
        String projectName = scanner.next()
        projectName = projectName ? projectName : DEFAULT_PROJECT_NAME
        gravyCookProps.put("projectName", projectName)
        println"Please enter groupId name(default is ${DEFAULT_GOUP_ID} com.foo)"
        String groupPackage = scanner.next()
        gravyCookProps.put("groupPackage", groupPackage)
        println"Please Enter Service Module Class name (default is ${DEFAULT_SERVICE_MODULE})"
        String serviceModule = scanner.next()
        serviceModule =  serviceModule ? serviceModule : DEFAULT_SERVICE_MODULE
        gravyCookProps.put("serviceModule", serviceModule)
        println"Please enter Root router class name (default is ${DEFAULT_ROOT_ROUTER})"
        String rootRouter = scanner.next()
        rootRouter = rootRouter ? rootRouter : DEFAULT_ROOT_ROUTER
        gravyCookProps.put("rootRouter", rootRouter)
        println"${gravyCookProps}"*/
        gravyCookProps.putAll([("${PROJECT_NAME_KEY}".toString()) : DEFAULT_PROJECT_NAME, ("${GROUP_ID_KEY}".toString()) : DEFAULT_GOUP_ID, ("${SERVICE_MODULE_KEY}".toString()) : DEFAULT_SERVICE_MODULE,
                               ("${ROOT_ROUTER_KEY}".toString()) : DEFAULT_ROOT_ROUTER])
        return gravyCookProps
    }
}
