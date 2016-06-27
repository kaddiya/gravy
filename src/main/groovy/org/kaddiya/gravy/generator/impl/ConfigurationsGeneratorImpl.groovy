package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_RESOLUTION_STRATEGY_DEPENDENCY
import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_EXCLUDE_DEPENDENCY

import groovy.transform.CompileStatic
import org.kaddiya.gravy.generator.AbstractScriptGenerator
import org.kaddiya.gravy.model.Configuration
import org.kaddiya.gravy.model.ConfigurationType


@CompileStatic
class ConfigurationsGeneratorImpl  extends AbstractScriptGenerator<Configuration>{

    ConfigurationsGeneratorImpl() {
        def configurationList = DEFAULT_EXCLUDE_DEPENDENCY.collect{ dependency ->
            new Configuration(ConfigurationType.EXCLUDE, dependency)
        }
        configurationList.addAll(DEFAULT_RESOLUTION_STRATEGY_DEPENDENCY.collect{ dependency ->
            new Configuration(ConfigurationType.RESOLUTION_STRATEGY, dependency)
        })
        create(configurationList)
    }

    @Override
    protected <Configuration> void create( List<Configuration> modelList ) {
            String dependencyConfig
        def configList = modelList.collect{Configuration configuration ->
            configuration.toString()
        }
            this.binding.putAll(["configDependencies" : configList.join("\n")])
    }

}
