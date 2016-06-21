package org.kaddiya.gravy.generator.impl

import com.google.inject.Guice
import com.google.inject.Injector
import org.kaddiya.gravy.generator.CreatorApiModule
import org.kaddiya.gravy.model.Plugin
import spock.lang.Shared
import spock.lang.Specification


class PluginCreatorImplSpec extends Specification{


    @Shared
    private Injector injector

    private PluginGeneratorImpl pluginCreator


    def setupSpec(){
        injector = Guice.createInjector(new CreatorApiModule())
    }

    def setup(){
        pluginCreator =  injector.getInstance(PluginGeneratorImpl)
    }

    def "Plugins can not be null when getPlugins() is called "(){
        when:
        def plugins = pluginCreator.getScript()
        then:
        assert plugins != null : "Plugins list can not be null"

    }

    def "Plugins can not be null or empty when addPlugin() is called "(){
        when:
        pluginCreator.addOne(new Plugin("sample"))
        then:
        def plugins = pluginCreator.getScript()
        assert plugins : "Plugins list can not be null"
        assert plugins.contains("apply plugin: 'sample'") : "Added plugin not found"

    }

    def "Plugins can not be null or empty  when addPlugins() is called "(){
        when:
        pluginCreator.addList([new Plugin("sample1"), new Plugin("sample2")])
        then:
        def plugins = pluginCreator.getScript()
        assert plugins : "Plugins list can not be null"
        assert plugins.contains("apply plugin: 'sample1'") : "Added plugin not found"
        assert plugins.contains("apply plugin: 'sample2'") : "Added plugin not found"


    }
}
