package org.kaddiya.gravy.creator;

import java.util.List;
import java.util.Map;

public interface ScriptCreator<T> {

        List<String> getScript();
        Map<String, String> getModelBinding();
    <T> void addOne(T model);
    <T> void addList(List<T> modelList);
}
