package org.kaddiya.gravy.initilaiser

import com.google.inject.AbstractModule
import org.kaddiya.gravy.initilaiser.impl.GradleApplicationInitialiser


class InitialiserApiModule extends AbstractModule{


    @Override
    protected void configure() {
        bind(Initialiser).to(GradleApplicationInitialiser)
    }
}
