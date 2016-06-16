package org.kaddiya.gravy.creator

import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractScriptCreator<T> implements ScriptCreator<T> {

    protected abstract <T>void create(List<T> modelList);

    protected List<String> modelList = new ArrayList<>()
    protected LinkedHashMap<String, String> binding = new LinkedHashMap<>()

    @Override
    public List<String> getScript() {
        return this.modelList
    }

    @Override
    public <T> void addOne(T model) {
        this.addList([model]);
    }

    @Override
    public <T> void addList(List<T> modelList) {
        create(modelList)
    }

    @Override
    Map<String, String> getModelBinding() {
        return binding
    }
}
