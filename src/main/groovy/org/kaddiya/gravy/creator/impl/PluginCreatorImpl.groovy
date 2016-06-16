package org.kaddiya.gravy.creator.impl

import groovy.transform.CompileStatic
import org.kaddiya.gravy.creator.AbstractScriptCreator
import org.kaddiya.gravy.creator.PluginCreator
import org.kaddiya.gravy.model.Plugin
import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_PLUGINS

class PluginCreatorImpl<Plugin> extends AbstractScriptCreator<Plugin> implements PluginCreator{

    PluginCreatorImpl() {
        create(DEFAULT_PLUGINS)
    }

    @Override
    protected <Plugin> void create(List<Plugin> modelList) {

       def pluginBinding = modelList.collectEntries {Plugin plugin ->
           String pluginName =  plugin.getPluginName()
           //plugin.
           if(pluginName.contains("gretty")){
               return ["gretty" : plugin.toString()]
           }
           return [("${pluginName}".toString()) : plugin.toString()] }

        this.binding.putAll(pluginBinding)

        List<String> plugins = modelList.collect{Plugin plugin -> plugin.toString()}
        this.modelList.addAll(plugins)
    }

}
