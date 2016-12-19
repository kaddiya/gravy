package org.kaddiya.gravy.generator.impl

import static org.kaddiya.gravy.predefined.DefaultValues.DEFAULT_PLUGINS

import org.kaddiya.gravy.generator.AbstractScriptGenerator

class PluginGeneratorImpl<Plugin> extends AbstractScriptGenerator<Plugin> {

    PluginGeneratorImpl() {
        create(DEFAULT_PLUGINS)
    }

    @Override
    protected <Plugin> void create(List<Plugin> modelList) {

        def pluginBinding = modelList.collectEntries { Plugin plugin ->
            String pluginName = plugin.getPluginName()

            if (pluginName.contains("gretty")) {
                pluginName = "gretty"
            } else if (pluginName.contains("liquibase")) {
                pluginName = "liquibase"
            }

            return [("${pluginName}".toString()): plugin.toString()] }

        this.binding.putAll(pluginBinding)

        List<String> plugins = modelList.collect{Plugin plugin -> plugin.toString()}
        this.modelList.addAll(plugins)
    }

}
