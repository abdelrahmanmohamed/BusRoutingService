package com.goeuro.busroute.datatstructures;

/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */

import java.util.HashMap;

public class DisjointSet {
    private final HashMap<Integer, Node> objectsToNodes = new HashMap<>();

    private Integer findSet(Integer o) {
        DisjointSet.Node node = objectsToNodes.get(o);
        if (node == null) {
            return null;
        }
        if (!o.equals(node.parent)) {
            node.parent = findSet(node.parent);
        }
        return node.parent;
    }

    private void makeSet(Integer o) {
        objectsToNodes.put(o, new Node(o, 0));
    }

    public void union(Integer x, Integer y) {
        Integer setX = findSet(x);
        Integer setY = findSet(y);
        if (setX != null && setY != null && setX.equals(setY)) {
            return;
        }
        if (setX == null) {
            makeSet(x);
            setX = findSet(x);
        }
        if (setY == null) {
            makeSet(y);
            setY = findSet(y);
        }
        Node nodeX = objectsToNodes.get(setX);
        Node nodeY = objectsToNodes.get(setY);

        if (nodeX.rank > nodeY.rank) {
            nodeY.parent = x;
        } else {
            nodeX.parent = y;
            if (nodeX.rank.equals(nodeY.rank)) {
                nodeY.rank++;
            }
        }
    }

    public int connectedBy(Integer x, Integer y) {
        Integer ySet = findSet(y);
        Integer xSet = findSet(x);
        if (xSet != null && ySet != null && xSet.equals(ySet))
            return ySet;
        else
            return -1;
    }

    private static class Node {
        Integer rank;
        Integer parent;

        Node(Integer parent, Integer rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }
}