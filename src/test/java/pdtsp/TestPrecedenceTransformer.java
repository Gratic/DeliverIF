package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestPrecedenceTransformer {

    @Test
    void smallGraphOneRequest() {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 1d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 1d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);

        Graph g = new BasicGraph(map.size(), map);

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));

        PrecedenceTransformer precedencer = new PrecedenceTransformer(g.getNbVertices(), requests);
        precedencer.transform();

        Graph transformed = precedencer.getTransformedGraph();

        Assertions.assertNotNull(transformed);
        Assertions.assertEquals(g.getNbVertices(), transformed.getNbVertices());

        for (Pair<Integer, Integer> request : requests) {
            int p = request.getX();
            int d = request.getY();

            Assertions.assertEquals(0, transformed.getOutgoingArcs(p).size());
            Assertions.assertEquals(1, transformed.getOutgoingArcs(d).size());
        }
    }

    @Test
    void smallGraphTwoRequests() {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));
        arcs0.add(new Pair<>(3, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 1d));
        arcs0.add(new Pair<>(3, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 1d));
        arcs0.add(new Pair<>(3, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs3 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 1d));
        arcs0.add(new Pair<>(2, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs4 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 1d));
        arcs0.add(new Pair<>(2, 2d));
        arcs0.add(new Pair<>(3, 3d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);
        map.put(3, arcs3);
        map.put(4, arcs4);

        Graph g = new BasicGraph(map.size(), map);

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));
        requests.add(new Pair<>(3, 4));

        PrecedenceTransformer precedencer = new PrecedenceTransformer(g.getNbVertices(), requests);
        precedencer.transform();

        Graph transformed = precedencer.getTransformedGraph();

        Assertions.assertNotNull(transformed);
        Assertions.assertEquals(g.getNbVertices(), transformed.getNbVertices());

        for (Pair<Integer, Integer> request : requests) {
            int p = request.getX();
            int d = request.getY();

            Assertions.assertEquals(0, transformed.getOutgoingArcs(p).size());
            Assertions.assertEquals(1, transformed.getOutgoingArcs(d).size());
        }
    }
}
