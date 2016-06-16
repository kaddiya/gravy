package org.kaddiya.gravy.creator

import com.google.inject.AbstractModule
import org.kaddiya.gravy.creator.impl.BuildScriptCreatorImpl
import org.kaddiya.gravy.creator.impl.DependencyCreatorImpl
import org.kaddiya.gravy.creator.impl.PluginCreatorImpl
import org.kaddiya.gravy.creator.impl.RepositoryCreatorImpl

class CreatorApiModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(DependencyCreator).to(DependencyCreatorImpl)
        bind(RepositoryCreator).to(RepositoryCreatorImpl)
        bind(PluginCreator).to(PluginCreatorImpl)
        bind(BuildScriptCreator).to(BuildScriptCreatorImpl)
    }
}
