package org.kaddiya.gravy.predefined;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeParser {
    public static Map<String, Node> parseTree(List<String> paths) {
        Map<String, Node> tree = new HashMap<String, Node>();
        for (String s : paths) {
            String[] strings = s.replaceFirst("/", "").split("/");
            Node previousNode = null;
            String temp = "";
            for (String g : strings) {
                temp = temp + "/" + g;
                if (!tree.containsKey(temp)) {
                    Node currentNode = new Node("/" + g);
                    if (tree.containsKey(temp.replace(currentNode.getPathName(), ""))) {
                        previousNode = tree.get(temp.replace(currentNode.getPathName(), ""));
                    }
                    if (previousNode != null) {
                        previousNode.addNode(currentNode, temp);
                    }
                    currentNode.setPath(temp);
                    currentNode.setParent(previousNode);
                    tree.put(temp, currentNode);
                    System.out.println(temp);
                }
            }
        }
        return tree;
    }
}
