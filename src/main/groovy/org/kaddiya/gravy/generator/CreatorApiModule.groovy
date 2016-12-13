package org.kaddiya.gravy.generator

import com.google.inject.AbstractModule
import org.kaddiya.gravy.generator.impl.BuildScriptGeneratorImpl
import org.kaddiya.gravy.generator.impl.ConfigurationsGeneratorImpl
import org.kaddiya.gravy.generator.impl.DependencyGeneratorImpl
import org.kaddiya.gravy.generator.impl.GitIgnoreGenerator
import org.kaddiya.gravy.generator.impl.MetaRouterGenerator
import org.kaddiya.gravy.generator.impl.PingResourceGenerator
import org.kaddiya.gravy.generator.impl.PluginGeneratorImpl
import org.kaddiya.gravy.generator.impl.RepositoryGeneratorImpl
import org.kaddiya.gravy.generator.impl.RootRouterGenerator
import org.kaddiya.gravy.generator.impl.ServiceModuleGenerator
import org.kaddiya.gravy.generator.impl.WebXmlCreator

class CreatorApiModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(DependencyGeneratorImpl)
        bind(RepositoryGeneratorImpl)
        bind(PluginGeneratorImpl)
        bind(BuildScriptGenerator).to(BuildScriptGeneratorImpl)
        bind(WebXmlCreator)
        bind(GitIgnoreGenerator)
        bind(MetaRouterGenerator)
        bind(PingResourceGenerator)
        bind(ServiceModuleGenerator)
        bind(RootRouterGenerator)
        bind(ConfigurationsGeneratorImpl)
    }
}
