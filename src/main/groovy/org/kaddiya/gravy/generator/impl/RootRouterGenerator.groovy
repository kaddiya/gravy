package org.kaddiya.gravy.generator.impl
import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.commons.lang3.StringUtils
import org.kaddiya.gravy.generator.AbstractCodeGenerator
import org.kaddiya.gravy.model.Router
import org.kaddiya.gravy.predefined.Node

import static org.kaddiya.gravy.Constants.REGEX
import static org.kaddiya.gravy.Constants.ROUTER_PACKAGE
import static org.kaddiya.gravy.Constants.BLANK_SPACE
import static org.kaddiya.gravy.Constants.ROOT_ROUTER_KEY
import static org.kaddiya.gravy.Constants.RESOURCE_PACKAGE


class RootRouterGenerator extends AbstractCodeGenerator {
    static final String ROOT_ROUTER_PATH = 'src/main/groovy/${groupId}/${rootRouter}.groovy'
    Map<String, String> binding
    protected final List<org.saur.model.Router> modelList

    @Inject
    RootRouterGenerator( ) {
        super(ROOT_ROUTER_PATH)
        binding = new HashMap<>()
        modelList = new ArrayList<>();
    }

    @Override
    public File createFile(File projRootDir, String fileName, String packageName) {
        binding.putAll([("${PACKAGE}".toString()) : packageName])
        binding.putAll([("${ROOT_ROUTER_KEY}".toString()) : fileName])
        def metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }

    @Override
    protected void createBindings(Node node) {
        def router = new org.saur.model.Router("path": node.getPathName(), "pathRouter": node
                .hasChildren() ? StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                .capitalize(ROUTER_PACKAGE) : StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE))
                + StringUtils
                .capitalize(RESOURCE_PACKAGE));
        this.modelList.add(router)
        this.binding.putAll(["attachSubRouter": this.modelList.join("\n")])
    }

    public void resetBindings() {
        this.binding.clear();
        this.modelList.clear();
    }
}
