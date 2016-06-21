package org.kaddiya.gravy.generator.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.kaddiya.gravy.generator.AbstractCodeGenerator

class ServiceModuleGenerator extends AbstractCodeGenerator {

    final static String MODULE_CLASS_PATH = 'src/main/groovy/${groupId}/${serviceModule}.groovy'
    private Map<String, String> binding

    @Inject
    ServiceModuleGenerator( @Named("gravyProps") Map gravyProps ) {
        super(MODULE_CLASS_PATH, gravyProps)
        binding = new HashMap<>()
        binding.putAll(super.groupIdBinding)
        binding.putAll(super.serviceModuleBinding)
        binding.putAll(super.rootRouterBinding)
        binding.putAll(super.groupPackageBinding)
    }

    @Override
    public File createFile( File projRootDir ) {
        def metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( File metaRouterTemplateFile ) {
        return super.generateCode(metaRouterTemplateFile, binding)
    }
}
