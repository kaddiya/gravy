package org.kaddiya.gravy

import com.google.inject.AbstractModule
import com.google.inject.Module
import org.kaddiya.gravy.generator.CreatorApiModule
import org.kaddiya.gravy.initilaiser.InitialiserApiModule


class GravyModule extends AbstractModule{
    private Module[] modules

    GravyModule() {
    }

    GravyModule(Module ... modules) {
        super()
        this.modules = modules
    }


    @Override
    protected void configure() {
        def gravyModule = this
        this.modules.each {module -> gravyModule.install(module)}
        this.install(new CreatorApiModule())
        this.install(new InitialiserApiModule())
    }


}
