package org.kaddiya.gravy

import com.google.inject.AbstractModule
import org.kaddiya.gravy.creator.CreatorApiModule
import org.kaddiya.gravy.initilaiser.InitialiserApiModule


class GravyModule extends AbstractModule{

    @Override
    protected void configure() {
        this.install(new CreatorApiModule())
        this.install(new InitialiserApiModule())

    }
}
