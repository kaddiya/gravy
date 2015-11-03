package org.kaddiya.gravy

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser

/**
 * Created by Webonise on 03/11/15.
 */

@CompileStatic
class Gravy {

    public static void main(String[ ]args ){
        //GUICE it up
        Initialiser gradleAppInitialiser =  new GradleApplicationInitialiser();
        println("Welcome to the Dev Kitchen,the Groovy way.Please help us with your order!")
        assert args.size() >= 1 : "Please provide the recipe to cook!"
        List<String> argsList = Arrays.asList(args)
        String parentArg = argsList.get(0)
        switch (parentArg.toUpperCase()){
            case "COOK":
                assert System.getProperty("user.dir") : "the current path should not be null"
                gradleAppInitialiser.prepareEnvironment(System.getProperty("user.dir"));
                println("Lets bootstrap your application")
                break;
            default:
                throw new IllegalArgumentException("Top level argument not supported")
        }
    }
}
