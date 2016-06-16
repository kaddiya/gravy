package org.kaddiya.gravy

import com.google.inject.Guice
import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.kaddiya.gravy.initilaiser.Initialiser
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser

/**
 * Created by Webonise on 03/11/15.
 */

@CompileStatic
class Gravy {

    public static void main(String[]args ){
        Injector gravyInjector = Guice.createInjector(new GravyModule())
        Initialiser gradleAppInitialiser =  gravyInjector.getInstance(GradleApplicationInitialiser)
        println("Welcome to the Dev Kitchen,the Groovy way.Please help us with your order!")
        assert args.size() >= 1 : "Please provide the recipe to cook!"

        String parentArg = args[0]
        switch (parentArg.toUpperCase()){
            case "COOK":
                assert System.getProperty("user.dir") : "the current path should not be null"
                def rootDir = gradleAppInitialiser.prepareEnvironment(args);
                gradleAppInitialiser.writeBuildGradleTemplate(rootDir)
                println("Lets bootstrap your application")
                break;
            default:
                throw new IllegalArgumentException("Top level argument not supported")
        }
    }
}
