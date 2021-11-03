package testPdtsp;

import org.junit.jupiter.api.Assertions;
import pdtsp.BasicGraph;
import pdtsp.Dijkstra;

import org.junit.jupiter.api.Test;
import pdtsp.Pair;
import pdtsp.ShortestPath;
import pdtsp.Graph;


import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDijkstra {

    @Test
    void simpleGraph1()
    {
        /*
                    (0)
                 2 /   \ 3
                  /  2  \
                (1)-----(2)
         */
        // Création du graphe
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

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);
        List<Pair<Double, Integer>> distFrom2 = dijkstra.searchShortestPathFrom(2, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>(){{ add(0d); add(2d); add(3d);}};
        List<Double> expectFrom1 = new ArrayList<>(){{ add(2d); add(0d); add(2d);}};
        List<Double> expectFrom2 = new ArrayList<>(){{ add(3d); add(2d); add(0d);}};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom2, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom2.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        /*
        Assertions.assertEquals(expectFrom0, distFrom0, "Erreur les résultat des distances à partir du sommet 0 ne correspondent pas !");
        Assertions.assertEquals(expectFrom1, distFrom1, "Erreur les résultat des distances à partir du sommet 1 ne correspondent pas !");
        Assertions.assertEquals(expectFrom2, distFrom2, "Erreur les résultat des distances à partir du sommet 2 ne correspondent pas !");
         */
    }

    @Test
    void simpleGraph2()
    {
        /*
                    (0)
                 2 /   \ 3
                  /  1  \
                (1)-----(2)
         */
        // Création du graphe
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<Integer, Double>(1, 2d));
        arcs0.add(new Pair<Integer, Double>(2, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<Integer, Double>(0, 2d));
        arcs1.add(new Pair<Integer, Double>(2, 1d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<Integer, Double>(0, 3d));
        arcs2.add(new Pair<Integer, Double>(1, 1d));

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
        List<Double> expectFrom0 = new ArrayList<Double>(){{ add(0d); add(2d); add(3d);}};
        List<Double> expectFrom1 = new ArrayList<Double>(){{ add(2d); add(0d); add(1d);}};
        List<Double> expectFrom2 = new ArrayList<Double>(){{ add(3d); add(1d); add(0d);}};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom2, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom2.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        /*
        Assertions.assertEquals(expectFrom0, distFrom0, "Erreur les résultat des distances à partir du sommet 0 ne correspondent pas !");
        Assertions.assertEquals(expectFrom1, distFrom1, "Erreur les résultat des distances à partir du sommet 1 ne correspondent pas !");
        Assertions.assertEquals(expectFrom2, distFrom2, "Erreur les résultat des distances à partir du sommet 2 ne correspondent pas !");
        */
    }

    @Test
    void mediumGraph1()
    {
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

        // Dijkstra
        ShortestPath dijkstra = new Dijkstra();

        List<Pair<Double, Integer>> distFrom0 = dijkstra.searchShortestPathFrom(0, g);
        List<Pair<Double, Integer>> distFrom1 = dijkstra.searchShortestPathFrom(1, g);
        List<Pair<Double, Integer>> distFrom3 = dijkstra.searchShortestPathFrom(3, g);

        // Résultats attendus
        List<Double> expectFrom0 = new ArrayList<>(){{ add(0d); add(1d); add(4d); add(2d); add(5d); add(6d);}};
        List<Double> expectFrom1 = new ArrayList<>(){{ add(1d); add(0d); add(3d); add(1d); add(4d); add(5d);}};
        List<Double> expectFrom3 = new ArrayList<>(){{ add(2d); add(1d); add(4d); add(0d); add(4d); add(5d);}};

        // Assertions
        Assertions.assertNotNull(distFrom0, "Erreur les résultat des distances à partir du sommet 0 sont null !");
        Assertions.assertNotNull(distFrom1, "Erreur les résultat des distances à partir du sommet 1 sont null !");
        Assertions.assertNotNull(distFrom3, "Erreur les résultat des distances à partir du sommet 2 sont null !");

        Assertions.assertNotEquals(0, distFrom0.size(), "Erreur les résultat des distances à partir du sommet 0 sont vides !");
        Assertions.assertNotEquals(0, distFrom1.size(), "Erreur les résultat des distances à partir du sommet 1 sont vides !");
        Assertions.assertNotEquals(0, distFrom3.size(), "Erreur les résultat des distances à partir du sommet 2 sont vides !");

        /*
        Assertions.assertEquals(expectFrom0, distFrom0, "Erreur les résultat des distances à partir du sommet 0 ne correspondent pas !");
        Assertions.assertEquals(expectFrom1, distFrom1, "Erreur les résultat des distances à partir du sommet 1 ne correspondent pas !");
        Assertions.assertEquals(expectFrom3, distFrom3, "Erreur les résultat des distances à partir du sommet 2 ne correspondent pas !");
         */
    }

}
