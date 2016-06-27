package org.kaddiya.gravy.generator.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.kaddiya.gravy.generator.AbstractCodeGenerator

class MetaRouterGenerator extends AbstractCodeGenerator{
    static final String META_ROUTER_PATH = '/src/main/groovy/${groupId}/resources/meta/MetaRouter.groovy'
    private Map<String, String> binding

    @Inject
    MetaRouterGenerator(@Named("gravyProps")Map gravyProps) {
        super(META_ROUTER_PATH, gravyProps)
        binding = new HashMap<>()
        binding.putAll(super.groupIdBinding)
        binding.putAll(super.groupPackageBinding)
    }

    @Override
    public File createFile( File projRootDir) {
        def  metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }
}
