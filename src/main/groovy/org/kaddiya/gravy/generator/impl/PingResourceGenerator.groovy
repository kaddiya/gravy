package org.kaddiya.gravy.generator.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import io.swagger.models.Path
import org.kaddiya.gravy.generator.AbstractCodeGenerator


class PingResourceGenerator  extends AbstractCodeGenerator{
    static final String PING_RESOURCE_PATH = '/src/main/groovy/${groupId}/resources/meta/PingResource.groovy'
    private Map<String, String> binding

    @Inject
    PingResourceGenerator(@Named("gravyProps")Map gravyProps ) {
        super(PING_RESOURCE_PATH, gravyProps)
        this.binding = new HashMap<>()
        binding.putAll(super.groupIdBinding)
        binding.putAll(super.groupPackageBinding)
    }

    @Override
    public File createFile( File projRootDir, String fileName) {
        def  metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }

    @Override
    protected void createBindings(String key, Path value) {

    }
}
