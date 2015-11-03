package org.kaddiya.gravy

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Created by Webonise on 03/11/15.
 */

@CompileStatic
class Gravy {
    public static void main(String[ ]args ){
       println("Welcome to the Dev Kitchen,the Groovy way.Please help us with your order!")
        assert args.size() >= 1 : "Please provide the recipe to cook!"
        List<String> argsList = Arrays.asList(args)
        String parentArg = argsList.get(0)
        switch (parentArg.toUpperCase()){
            case "COOK":
                println("Lets bootstrap your application")
                break;
            default:
                throw new IllegalArgumentException("Top level argument not supported")
        }
    }
}
