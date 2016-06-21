package org.kaddiya.gravy.generator;

import java.util.List;
import java.util.Map;

public interface ScriptGenerator<T> {

        List<String> getScript();
        Map<String, String> getModelBinding();
    <T> void addOne(T model);
    <T> void addList(List<T> modelList);
}
