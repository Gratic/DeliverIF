package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestPaperPDTSP {
    @Test
    void smallGraphOneRequest() {
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

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 7d;

        PaperPDTSP pdtsp = new PaperPDTSP(99999999, g, pred);
        pdtsp.searchSolution(9999999, g, pred);

        Assertions.assertTrue(pdtsp.isSolutionFound());
        Assertions.assertTrue(pdtsp.isSolutionOptimal());
        Assertions.assertFalse(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertEquals(expectedSolutionCost, pdtsp.getSolutionCost());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertNotEquals(-1, pdtsp.getSolution(i));
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
        arcs1.add(new Pair<>(2, 2d));
        arcs1.add(new Pair<>(3, 0d));
        arcs1.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 2d));
        arcs2.add(new Pair<>(3, 2d));
        arcs2.add(new Pair<>(4, 0d));

        Collection<Pair<Integer, Double>> arcs3 = new ArrayList<>();
        arcs3.add(new Pair<>(0, 2d));
        arcs3.add(new Pair<>(1, 0d));
        arcs3.add(new Pair<>(2, 2d));
        arcs3.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs4 = new ArrayList<>();
        arcs4.add(new Pair<>(0, 3d));
        arcs4.add(new Pair<>(1, 2d));
        arcs4.add(new Pair<>(2, 0d));
        arcs4.add(new Pair<>(3, 2d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);
        map.put(3, arcs3);
        map.put(4, arcs4);

        Graph g = new BasicGraph(map.size(), map);

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        Collection<Pair<Integer, Double>> arcspred3 = new ArrayList<>();
        arcspred3.add(new Pair<>(4, 1d));

        Collection<Pair<Integer, Double>> arcspred4 = new ArrayList<>();

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);
        mapPred.put(3, arcspred3);
        mapPred.put(4, arcspred4);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 8d;

        PaperPDTSP pdtsp = new PaperPDTSP(99999999, g, pred);
        pdtsp.searchSolution(9999999, g, pred);

        Assertions.assertTrue(pdtsp.isSolutionFound());
        Assertions.assertTrue(pdtsp.isSolutionOptimal());
        Assertions.assertFalse(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertEquals(expectedSolutionCost, pdtsp.getSolutionCost());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertNotEquals(-1, pdtsp.getSolution(i));
        }
    }


    @Test
    void smallGraphOneRequestThreadVersion() {
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

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 7d;

        PaperPDTSP pdtsp = new PaperPDTSP(99999999, g, pred);

        Thread threadPdtsp = new Thread(pdtsp);

        Assertions.assertFalse(pdtsp.isRunning());
        threadPdtsp.start();

        while (!pdtsp.isReady()) {
        }

        Assertions.assertTrue(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertFalse(pdtsp.isSolutionFound());
        Assertions.assertFalse(pdtsp.isSolutionOptimal());

        pdtsp.resume();

        Assertions.assertTrue(pdtsp.isRunning());
        Assertions.assertFalse(pdtsp.isPaused());

        while (!pdtsp.isPaused()) {
        }

        Assertions.assertFalse(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertTrue(pdtsp.isSolutionFound());
        Assertions.assertTrue(pdtsp.isSolutionOptimal());
        Assertions.assertEquals(expectedSolutionCost, pdtsp.getSolutionCost());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertNotEquals(-1, pdtsp.getSolution(i));
        }

        pdtsp.kill();
    }

    @Test
    void smallGraphTwoRequestsThreadVersion() {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));
        arcs0.add(new Pair<>(3, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 2d));
        arcs1.add(new Pair<>(3, 0d));
        arcs1.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 2d));
        arcs2.add(new Pair<>(3, 2d));
        arcs2.add(new Pair<>(4, 0d));

        Collection<Pair<Integer, Double>> arcs3 = new ArrayList<>();
        arcs3.add(new Pair<>(0, 2d));
        arcs3.add(new Pair<>(1, 0d));
        arcs3.add(new Pair<>(2, 2d));
        arcs3.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs4 = new ArrayList<>();
        arcs4.add(new Pair<>(0, 3d));
        arcs4.add(new Pair<>(1, 2d));
        arcs4.add(new Pair<>(2, 0d));
        arcs4.add(new Pair<>(3, 2d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);
        map.put(3, arcs3);
        map.put(4, arcs4);

        Graph g = new BasicGraph(map.size(), map);

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        Collection<Pair<Integer, Double>> arcspred3 = new ArrayList<>();
        arcspred3.add(new Pair<>(4, 1d));

        Collection<Pair<Integer, Double>> arcspred4 = new ArrayList<>();

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);
        mapPred.put(3, arcspred3);
        mapPred.put(4, arcspred4);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 8d;

        PaperPDTSP pdtsp = new PaperPDTSP(99999999, g, pred);

        Thread threadPdtsp = new Thread(pdtsp);

        Assertions.assertFalse(pdtsp.isRunning());
        threadPdtsp.start();

        while (!pdtsp.isReady()) {
        }

        Assertions.assertTrue(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertFalse(pdtsp.isSolutionFound());
        Assertions.assertFalse(pdtsp.isSolutionOptimal());

        pdtsp.resume();

        Assertions.assertTrue(pdtsp.isRunning());
        Assertions.assertFalse(pdtsp.isPaused());

        while (!pdtsp.isPaused()) {
        }

        Assertions.assertFalse(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertTrue(pdtsp.isSolutionFound());
        Assertions.assertTrue(pdtsp.isSolutionOptimal());
        Assertions.assertEquals(expectedSolutionCost, pdtsp.getSolutionCost());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertNotEquals(-1, pdtsp.getSolution(i));
        }

        pdtsp.kill();
    }

    @Test
    void smallGraphOneRequestThreadVersionWithPause() {
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

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 7d;

        PaperPDTSP pdtsp = new PaperPDTSP(1, g, pred);

        Thread threadPdtsp = new Thread(pdtsp);

        Assertions.assertFalse(pdtsp.isRunning());
        threadPdtsp.start();

        while (!pdtsp.isReady()) {
        }

        Assertions.assertTrue(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertFalse(pdtsp.isSolutionFound());
        Assertions.assertFalse(pdtsp.isSolutionOptimal());

        while (!pdtsp.isSolutionOptimal()) {
            pdtsp.resume();
            while (!pdtsp.isPaused()) {
            }
        }

        Assertions.assertFalse(pdtsp.isRunning());
        Assertions.assertTrue(pdtsp.isPaused());
        Assertions.assertTrue(pdtsp.isSolutionFound());
        Assertions.assertTrue(pdtsp.isSolutionOptimal());
        Assertions.assertEquals(expectedSolutionCost, pdtsp.getSolutionCost());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertNotEquals(-1, pdtsp.getSolution(i));
        }

        pdtsp.kill();
    }

    @Test
    void smallGraphOneRequestOutOfBounds() {
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

        Map<Integer, Collection<Pair<Integer, Double>>> mapPred = new HashMap<>();
        Collection<Pair<Integer, Double>> arcspred0 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred1 = new ArrayList<>();
        Collection<Pair<Integer, Double>> arcspred2 = new ArrayList<>();
        arcspred2.add(new Pair<>(1, 1d));

        mapPred.put(0, arcspred0);
        mapPred.put(1, arcspred1);
        mapPred.put(2, arcspred2);

        Graph pred = new BasicGraph(mapPred.size(), mapPred);

        double expectedSolutionCost = 7d;

        PaperPDTSP pdtsp = new PaperPDTSP(99999999, g, pred);
        pdtsp.searchSolution(9999999, g, pred);

        Assertions.assertEquals(-1, pdtsp.getSolution(-1));
        Assertions.assertEquals(-1, pdtsp.getSolution(942));
    }

    @Test
    void noGraphOutOfBounds() {
        PaperPDTSP pdtsp = new PaperPDTSP(99999999, null, null);

        Assertions.assertEquals(-1, pdtsp.getSolution(-1));
        Assertions.assertEquals(-1, pdtsp.getSolution(942));

        Assertions.assertEquals(-1, pdtsp.getSolutionCost());
    }
}
