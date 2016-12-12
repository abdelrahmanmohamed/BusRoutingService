package com.goeuro.busroute.datatstructures;

/**
 * Created by hhmx3422 on 12/12/16.
 */

import java.util.HashMap;

public class DisjointSet {
    private static class Node {
        Integer rank;
        Integer parent;

        Node(Integer parent, Integer rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    private final HashMap<Integer, Node> objectsToNodes = new HashMap<>();

    private Integer findSet(Integer o) {
        DisjointSet.Node node = objectsToNodes.get(o);
        if (node == null) {
            return null;
        }
        if (o != node.parent) {
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
        if (setX == setY && setX != null && setY != null) {
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
            if (nodeX.rank == nodeY.rank) {
                nodeY.rank++;
            }
        }
    }

    public boolean isConnected(Integer x, Integer y) {
        Integer ySet = findSet(y);
        Integer xSet = findSet(x);
        return (xSet == ySet) && xSet != null && ySet != null;
    }
}