package com.gouro.busroute;

import com.goeuro.busroute.datatstructures.DisjointSet;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Abdelrahman Mohamed Sayed on 12/12/16.
 */
public class TestDisjoint {
    @Test
    public void testingIsDisjointSetsConnected() {
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.union(1, 2);
        disjointSet.union(2, 3);
        disjointSet.union(3, 6);
        disjointSet.union(1, 4);
        disjointSet.union(4, 5);
        disjointSet.union(5, 7);
        disjointSet.union(10, 9);
        disjointSet.union(9, 8);
        assertTrue(disjointSet.connectedBy(1, 5) != -1);
        assertTrue(disjointSet.connectedBy(3, 1) != -1);
        assertTrue(disjointSet.connectedBy(6, 7) != -1);
        assertFalse(disjointSet.connectedBy(10, 1) != -1);
        assertFalse(disjointSet.connectedBy(9, 5) != -1);
        assertFalse(disjointSet.connectedBy(8, 6) != -1);
    }
}
