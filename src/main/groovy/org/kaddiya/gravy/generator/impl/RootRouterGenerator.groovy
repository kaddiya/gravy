package org.kaddiya.gravy.generator.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.kaddiya.gravy.generator.AbstractCodeGenerator

class RootRouterGenerator extends AbstractCodeGenerator {
    static final String ROOT_ROUTER_PATH = 'src/main/groovy/${groupId}/${rootRouter}.groovy'
    Map<String, String> binding

    @Inject
    RootRouterGenerator( @Named("gravyProps") Map gravyProps ) {
        super(ROOT_ROUTER_PATH, gravyProps)
        binding = new HashMap<>()
        binding.putAll(super.groupIdBinding)
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
