package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestDijkstra {

    @Test
    void simpleGraph1() {
        /*
                    (0)
                 2 /   \ 3
                  /  2  \
                (1)-----(2)
         */
        // Création du graphe
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 2d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);

        Graph g = new BasicGraph(map.size(), map);

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);
        List<Pair<Double, Integer>> distFrom2 = dijkstra.searchShortestPathFrom(2, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>() {{
            add(0d);
            add(2d);
            add(3d);
        }};
        List<Double> expectFrom1 = new ArrayList<>() {{
            add(2d);
            add(0d);
            add(2d);
        }};
        List<Double> expectFrom2 = new ArrayList<>() {{
            add(3d);
            add(2d);
            add(0d);
        }};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom2, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom2.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        List<List<Pair<Double, Integer>>> results = new ArrayList<>();
        List<List<Double>> expectedResults = new ArrayList<>();

        expectedResults.add(expectFrom0);
        expectedResults.add(expectFrom1);
        expectedResults.add(expectFrom2);

        results.add(distFrom0);
        results.add(distFrom1);
        results.add(distFrom2);

        Assertions.assertEquals(expectedResults.size(), results.size());

        for(int i = 0; i < expectedResults.size(); i++)
        {
            Assertions.assertEquals(expectedResults.get(i).size(), results.get(i).size());

            for(int j = 0; j < expectedResults.get(i).size(); j++)
            {
                Assertions.assertEquals(expectedResults.get(i).get(j), results.get(i).get(j).getX());
            }
        }
    }

    @Test
    void simpleGraph2() {
        /*
                    (0)
                 2 /   \ 3
                  /  1  \
                (1)-----(2)
         */
        // Création du graphe
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

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);
        List<Pair<Double, Integer>> distFrom2 = dijkstra.searchShortestPathFrom(2, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>() {{
            add(0d);
            add(2d);
            add(3d);
        }};
        List<Double> expectFrom1 = new ArrayList<>() {{
            add(2d);
            add(0d);
            add(1d);
        }};
        List<Double> expectFrom2 = new ArrayList<>() {{
            add(3d);
            add(1d);
            add(0d);
        }};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom2, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom2.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        List<List<Pair<Double, Integer>>> results = new ArrayList<>();
        List<List<Double>> expectedResults = new ArrayList<>();

        expectedResults.add(expectFrom0);
        expectedResults.add(expectFrom1);
        expectedResults.add(expectFrom2);

        results.add(distFrom0);
        results.add(distFrom1);
        results.add(distFrom2);

        Assertions.assertEquals(expectedResults.size(), results.size());

        for(int i = 0; i < expectedResults.size(); i++)
        {
            Assertions.assertEquals(expectedResults.get(i).size(), results.get(i).size());

            for(int j = 0; j < expectedResults.get(i).size(); j++)
            {
                Assertions.assertEquals(expectedResults.get(i).get(j), results.get(i).get(j).getX());
            }
        }
    }

    @Test
    void mediumGraph1() {
        // Création du graphe
        Map<Integer, Collection<Pair<Integer, Double>>> arcs = new HashMap<>();

        Collection<Pair<Integer, Double>> arc0 = new ArrayList<>();
        arc0.add(new Pair<>(1, 1d));
        arc0.add(new Pair<>(3, 2d));

        Collection<Pair<Integer, Double>> arc1 = new ArrayList<>();
        arc1.add(new Pair<>(0, 1d));
        arc1.add(new Pair<>(2, 3d));
        arc1.add(new Pair<>(3, 1d));

        Collection<Pair<Integer, Double>> arc2 = new ArrayList<>();
        arc2.add(new Pair<>(1, 3d));
        arc2.add(new Pair<>(4, 1d));
        arc2.add(new Pair<>(5, 3d));

        Collection<Pair<Integer, Double>> arc3 = new ArrayList<>();
        arc3.add(new Pair<>(0, 2d));
        arc3.add(new Pair<>(1, 1d));
        arc3.add(new Pair<>(4, 4d));

        Collection<Pair<Integer, Double>> arc4 = new ArrayList<>();
        arc4.add(new Pair<>(2, 1d));
        arc4.add(new Pair<>(3, 4d));
        arc4.add(new Pair<>(5, 1d));

        Collection<Pair<Integer, Double>> arc5 = new ArrayList<>();
        arc5.add(new Pair<>(2, 3d));
        arc5.add(new Pair<>(4, 1d));

        arcs.put(0, arc0);
        arcs.put(1, arc1);
        arcs.put(2, arc2);
        arcs.put(3, arc3);
        arcs.put(4, arc4);
        arcs.put(5, arc5);

        Graph g = new BasicGraph(arcs.size(), arcs);

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);
        List<Pair<Double, Integer>> distFrom2 = dijkstra.searchShortestPathFrom(2, g);
        List<Pair<Double, Integer>> distFrom3 = dijkstra.searchShortestPathFrom(3, g);
        List<Pair<Double, Integer>> distFrom4 = dijkstra.searchShortestPathFrom(4, g);
        List<Pair<Double, Integer>> distFrom5 = dijkstra.searchShortestPathFrom(5, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>() {{
            add(0d);
            add(1d);
            add(4d);
            add(2d);
            add(5d);
            add(6d);
        }};
        List<Double> expectFrom1 = new ArrayList<>() {{
            add(1d);
            add(0d);
            add(3d);
            add(1d);
            add(4d);
            add(5d);
        }};
        List<Double> expectFrom2 = new ArrayList<>() {{
            add(4d);
            add(3d);
            add(0d);
            add(4d);
            add(1d);
            add(2d);
        }};
        List<Double> expectFrom3 = new ArrayList<>() {{
            add(2d);
            add(1d);
            add(4d);
            add(0d);
            add(4d);
            add(5d);
        }};
        List<Double> expectFrom4 = new ArrayList<>() {{
            add(5d);
            add(4d);
            add(1d);
            add(4d);
            add(0d);
            add(1d);
        }};
        List<Double> expectFrom5 = new ArrayList<>() {{
            add(6d);
            add(5d);
            add(2d);
            add(5d);
            add(1d);
            add(0d);
        }};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom2, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom3, "Erreur les résultat des distances à partir du sommet 2 sont null !");
        Assertions.assertNotNull(distFrom4, "Erreur les résultat des distances à partir du sommet 2 sont null !");
        Assertions.assertNotNull(distFrom5, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom2.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");
        Assertions.assertNotEquals(0, distFrom3.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");
        Assertions.assertNotEquals(0, distFrom4.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");
        Assertions.assertNotEquals(0, distFrom5.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        List<List<Pair<Double, Integer>>> results = new ArrayList<>();
        List<List<Double>> expectedResults = new ArrayList<>();

        expectedResults.add(expectFrom0);
        expectedResults.add(expectFrom1);
        expectedResults.add(expectFrom2);
        expectedResults.add(expectFrom3);
        expectedResults.add(expectFrom4);
        expectedResults.add(expectFrom5);

        results.add(distFrom0);
        results.add(distFrom1);
        results.add(distFrom2);
        results.add(distFrom3);
        results.add(distFrom4);
        results.add(distFrom5);

        Assertions.assertEquals(expectedResults.size(), results.size());

        for(int i = 0; i < expectedResults.size(); i++)
        {
            Assertions.assertEquals(expectedResults.get(i).size(), results.get(i).size());

            for(int j = 0; j < expectedResults.get(i).size(); j++)
            {
                Assertions.assertEquals(expectedResults.get(i).get(j), results.get(i).get(j).getX());
            }
        }
    }

    @Test
    void nonConnectedGraph()
    {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();

        map.put(0, arcs0);
        map.put(1, arcs1);

        Graph g = new BasicGraph(map.size(), map);

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>() {{
            add(0d);
            add(2d);
        }};
        List<Double> expectFrom1 = new ArrayList<>() {{
            add(Double.POSITIVE_INFINITY);
            add(0d);
        }};

        List<List<Pair<Double, Integer>>> results = new ArrayList<>();
        List<List<Double>> expectedResults = new ArrayList<>();

        expectedResults.add(expectFrom0);
        expectedResults.add(expectFrom1);

        results.add(distFrom0);
        results.add(distFrom1);

        Assertions.assertEquals(expectedResults.size(), results.size());

        for(int i = 0; i < expectedResults.size(); i++)
        {
            Assertions.assertEquals(expectedResults.get(i).size(), results.get(i).size());

            for(int j = 0; j < expectedResults.get(i).size(); j++)
            {
                Assertions.assertEquals(expectedResults.get(i).get(j), results.get(i).get(j).getX());
            }
        }
    }

    @Test
    void oneNodeGraph()
    {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();

        map.put(0, arcs0);

        Graph g = new BasicGraph(map.size(), map);

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>() {{
            add(0d);
        }};

        List<List<Pair<Double, Integer>>> results = new ArrayList<>();
        List<List<Double>> expectedResults = new ArrayList<>();

        expectedResults.add(expectFrom0);

        results.add(distFrom0);

        Assertions.assertEquals(expectedResults.size(), results.size());

        for(int i = 0; i < expectedResults.size(); i++)
        {
            Assertions.assertEquals(expectedResults.get(i).size(), results.get(i).size());

            for(int j = 0; j < expectedResults.get(i).size(); j++)
            {
                Assertions.assertEquals(expectedResults.get(i).get(j), results.get(i).get(j).getX());
            }
        }
    }

}
