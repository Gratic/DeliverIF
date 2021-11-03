package testPdtsp;

import org.junit.jupiter.api.Assertions;
import pdtsp.BasicGraph;

import org.junit.jupiter.api.Test;
import pdtsp.Pair;
import pdtsp.Graph;

import java.util.*;

public class TestBasicGraph {

    @Test
    void simpleGraph1() {
        /*
                    (0)
                 2 /   \ 3
                  /  2  \
                (1)-----(2)
         */
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<Integer, Double>(1, 2d));
        arcs0.add(new Pair<Integer, Double>(2, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<Integer, Double>(0, 2d));
        arcs1.add(new Pair<Integer, Double>(2, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<Integer, Double>(0, 3d));
        arcs2.add(new Pair<Integer, Double>(1, 2d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);

        Graph g = new BasicGraph(map.size(), map);

        Assertions.assertEquals(3, g.getNbVertices());

        for(int i = 0; i < g.getNbVertices(); i++)
        {
            Collection<Integer> outgoingArcs = g.getOutgoingArcs(i);

            Assertions.assertNotNull(outgoingArcs);
            Assertions.assertEquals(2, outgoingArcs.size());
        }

        /* Arcs corrects */
        Assertions.assertEquals(2, g.getCost(0, 1));
        Assertions.assertEquals(3, g.getCost(0, 2));
        Assertions.assertEquals(2, g.getCost(1, 0));
        Assertions.assertEquals(2, g.getCost(1, 2));
        Assertions.assertEquals(3, g.getCost(2, 0));
        Assertions.assertEquals(2, g.getCost(2, 1));

        Assertions.assertTrue(g.isArc(0, 1));
        Assertions.assertTrue(g.isArc(0, 2));
        Assertions.assertTrue(g.isArc(1, 0));
        Assertions.assertTrue(g.isArc(1, 2));
        Assertions.assertTrue(g.isArc(2, 0));
        Assertions.assertTrue(g.isArc(2, 1));

        /* Arc incorrects */
        Assertions.assertEquals(-1, g.getCost(0, 0));
        Assertions.assertEquals(-1, g.getCost(1, 1));
        Assertions.assertEquals(-1, g.getCost(2, 2));

        Assertions.assertFalse(g.isArc(0, 0));
        Assertions.assertFalse(g.isArc(1, 1));
        Assertions.assertFalse(g.isArc(2, 2));
    }

    @Test
    void mediumGraph1() {
        // Création du graphe
        Map<Integer, Collection<Pair<Integer, Double>>> arcs = new HashMap<>();

        Collection<Pair<Integer, Double>> arc0 = new ArrayList<>();
        arc0.add(new Pair<Integer, Double>(1,1d));
        arc0.add(new Pair<Integer, Double>(3,2d));

        Collection<Pair<Integer, Double>> arc1 = new ArrayList<>();
        arc1.add(new Pair<Integer, Double>(0,1d));
        arc1.add(new Pair<Integer, Double>(2,3d));
        arc1.add(new Pair<Integer, Double>(3,1d));

        Collection<Pair<Integer, Double>> arc2 = new ArrayList<>();
        arc2.add(new Pair<Integer, Double>(1,3d));
        arc2.add(new Pair<Integer, Double>(4,1d));
        arc2.add(new Pair<Integer, Double>(5,3d));

        Collection<Pair<Integer, Double>> arc3 = new ArrayList<>();
        arc3.add(new Pair<Integer, Double>(0,2d));
        arc3.add(new Pair<Integer, Double>(1,1d));
        arc3.add(new Pair<Integer, Double>(4,4d));

        Collection<Pair<Integer, Double>> arc4 = new ArrayList<>();
        arc4.add(new Pair<Integer, Double>(2,1d));
        arc4.add(new Pair<Integer, Double>(3,4d));
        arc4.add(new Pair<Integer, Double>(5,1d));

        Collection<Pair<Integer, Double>> arc5 = new ArrayList<>();
        arc5.add(new Pair<Integer, Double>(2,3d));
        arc5.add(new Pair<Integer, Double>(4,1d));

        arcs.put(0, arc0);
        arcs.put(1, arc1);
        arcs.put(2, arc2);
        arcs.put(3, arc3);
        arcs.put(4, arc4);
        arcs.put(5, arc5);

        Graph g = new BasicGraph(arcs.size(), arcs);

        Assertions.assertEquals(6, g.getNbVertices());

        Assertions.assertEquals(2, g.getOutgoingArcs(0).size());
        Assertions.assertEquals(3, g.getOutgoingArcs(1).size());
        Assertions.assertEquals(3, g.getOutgoingArcs(2).size());
        Assertions.assertEquals(3, g.getOutgoingArcs(3).size());
        Assertions.assertEquals(3, g.getOutgoingArcs(4).size());
        Assertions.assertEquals(2, g.getOutgoingArcs(5).size());

        Assertions.assertTrue(g.isArc(0, 1));
        Assertions.assertTrue(g.isArc(0, 3));

        Assertions.assertTrue(g.isArc(1, 0));
        Assertions.assertTrue(g.isArc(1, 2));
        Assertions.assertTrue(g.isArc(1, 3));

        Assertions.assertTrue(g.isArc(2, 1));
        Assertions.assertTrue(g.isArc(2, 4));
        Assertions.assertTrue(g.isArc(2, 5));

        Assertions.assertTrue(g.isArc(3, 0));
        Assertions.assertTrue(g.isArc(3, 1));
        Assertions.assertTrue(g.isArc(3, 4));

        Assertions.assertTrue(g.isArc(4, 2));
        Assertions.assertTrue(g.isArc(4, 3));
        Assertions.assertTrue(g.isArc(4, 5));

        Assertions.assertTrue(g.isArc(5, 2));
        Assertions.assertTrue(g.isArc(5, 4));

        Assertions.assertEquals(1, g.getCost(0, 1));
        Assertions.assertEquals(2, g.getCost(0, 3));

        Assertions.assertEquals(1, g.getCost(1, 0));
        Assertions.assertEquals(3, g.getCost(1, 2));
        Assertions.assertEquals(1, g.getCost(1, 3));

        Assertions.assertEquals(3, g.getCost(2, 1));
        Assertions.assertEquals(1, g.getCost(2, 4));
        Assertions.assertEquals(3, g.getCost(2, 5));

        Assertions.assertEquals(2, g.getCost(3, 0));
        Assertions.assertEquals(1, g.getCost(3, 1));
        Assertions.assertEquals(4, g.getCost(3, 4));

        Assertions.assertEquals(1, g.getCost(4, 2));
        Assertions.assertEquals(4, g.getCost(4, 3));
        Assertions.assertEquals(1, g.getCost(4, 5));

        Assertions.assertEquals(3, g.getCost(5, 2));
        Assertions.assertEquals(1, g.getCost(5, 4));
    }
}
