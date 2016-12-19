package org.kaddiya.gravy.generator.impl
import com.google.inject.Inject
import com.google.inject.name.Named
import io.swagger.models.Path
import org.kaddiya.gravy.generator.AbstractCodeGenerator
import org.kaddiya.gravy.model.Router

import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY

class RootRouterGenerator extends AbstractCodeGenerator {
    static final String ROOT_ROUTER_PATH = 'src/main/groovy/${groupId}/${rootRouter}.groovy'
    Map<String, String> binding
    protected final List<Router> modelList

    @Inject
    RootRouterGenerator( @Named("gravyProps") Map gravyProps ) {
        super(ROOT_ROUTER_PATH, gravyProps)
        binding = new HashMap<>()
        binding.putAll(super.groupIdBinding)
        binding.putAll(super.rootRouterBinding)
        binding.putAll(super.groupPackageBinding)
        modelList = new ArrayList<>();
    }

    @Override
    public File createFile(File projRootDir, String fileName) {
        this.rootRouterBinding = [("${ROOT_ROUTER_KEY}".toString()) :fileName]
        binding.putAll(super.rootRouterBinding)
        def metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }

    @Override
    protected void createBindings(String key, Path value) {
        String[] keys = key.split("/")
        def router = new Router("path" : "'/" + keys[1] + "'", "pathRouter" : keys[1].concat("Router"));
        this.modelList.add(router)
        this.binding.putAll(["attachSubRouter" : this.modelList.join("\n")])
    }
}
