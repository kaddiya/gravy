package org.kaddiya.gravy.predefined;

import groovy.transform.Canonical;

import java.util.HashMap;
import java.util.Map;

@Canonical
public class Node {
    private String pathName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Node> children) {
        this.children = children;
    }

    private Node parent;
    private Map<String, Node> children;

    public Node(String pathName) {
        this.pathName = pathName;
        this.children = new HashMap<String, Node>();
    }

    public Node() {
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public boolean hasChildren() {
        if (this.children != null) {
            return this.children.size() > 0;
        }
        return false;
    }

    public void addNode(Node node, String s) {
        children.put(s, node);
    }

    public String toString() {
        return pathName + " " + hasChildren() + " " + this.children.size();
    }
}
