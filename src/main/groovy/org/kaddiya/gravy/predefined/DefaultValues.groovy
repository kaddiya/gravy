package org.kaddiya.gravy.predefined

import org.kaddiya.gravy.model.BuildPhaseType
import org.kaddiya.gravy.model.Dependency
import org.kaddiya.gravy.model.GAV
import org.kaddiya.gravy.model.Plugin
import org.kaddiya.gravy.model.Repository

class DefaultValues {

    static final List<Plugin> DEFAULT_PLUGINS = Collections.unmodifiableList(
            Arrays.asList("groovy", "java", "maven", "idea", "war", "eclipse", "org.akhikhl.gretty", "jacoco")
                    .collect { String pluginName -> new Plugin(pluginName) });

    static
    final List<Repository> DEFAULT_REPOSITRIES = Collections.unmodifiableList(Arrays.asList("mavenLocal",
            "mavenCentral", "jcenter")
            .collect { String repoName -> new Repository(repoName) }
    );

    static final List<Dependency> DEFAULT_COMPILE_DEPENDANCIES = Collections.unmodifiableList(Arrays.asList(
            new GAV("restling", "restling-core", "0.0.7")).collect { GAV gav ->
        new Dependency(phaseType : BuildPhaseType.COMPILE, gav: gav, isBuildScript : false)
    }
    );

    static final List<Dependency> DEFAULT_TEST_DEPENDENCIES = Collections.unmodifiableList(Arrays.asList(
    new GAV("org.spockframework", "spock-core", "0.7-groovy-2.0"), new GAV("junit", "junit", "4.12")).collect{
        GAV gav ->  new Dependency(phaseType:  BuildPhaseType.TEST_COMPILE, gav:  gav, isBuildScript:  false)
    });

    static final List<Dependency> DEFAULT_BUILDSCRIPT_DEPENDENCY = Collections.unmodifiableList(Arrays.asList(
            new GAV("org.akhikhl.gretty", "gretty", "+")).collect { GAV gav ->
        new Dependency(phaseType:  BuildPhaseType.COMPILE, gav : gav, isBuildScript:  true)
    })

}
