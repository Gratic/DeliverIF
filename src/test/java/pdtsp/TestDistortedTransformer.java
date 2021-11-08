package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestDistortedTransformer {

    @Test
    void simpleCompleteGraphOneRequest() {
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

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));

        DistortedTransformer distorter = new DistortedTransformer(g, requests);
        distorter.transform();

        Map<Integer, Collection<Integer>> beforeToAfter = distorter.getBeforeToAfterIndexes();
        Map<Integer, Integer> afterToBefore = distorter.getAfterToBeforeIndexes();
        Map<Integer, Integer> nodeRequests = distorter.getNodeRequests();
        Graph distorted = distorter.getTransformedGraph();
        List<Pair<Integer, Integer>> distortedRequests = distorter.getTransformedRequests();

        Assertions.assertNotNull(beforeToAfter);
        Assertions.assertEquals(g.getNbVertices(), beforeToAfter.size());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertTrue(afterToBefore.containsValue(i));
        }


        Assertions.assertNotNull(distorted);

        int expectedNumberOfNodes = 1 + 2 * requests.size();

        Assertions.assertEquals(expectedNumberOfNodes, distorted.getNbVertices());

        Map<String, Integer> typeCount = new HashMap<>();
        for (String type : distorter.getNodeTypes().values()) {
            typeCount.putIfAbsent(type, 0);
            typeCount.put(type, typeCount.get(type) + 1);
        }

        Assertions.assertEquals(3, typeCount.size());

        Assertions.assertEquals(1, typeCount.get("start"));
        Assertions.assertEquals(requests.size(), typeCount.get("pickup"));
        Assertions.assertEquals(requests.size(), typeCount.get("delivery"));

        Assertions.assertNotNull(distortedRequests);
        Assertions.assertEquals(requests.size(), distortedRequests.size());

        for (int i = 0; i < requests.size(); i++) {
            Pair<Integer, Integer> distortedRequest = distortedRequests.get(i);
            Pair<Integer, Integer> request = requests.get(i);

            int distortedP = distortedRequest.getX();
            int distortedD = distortedRequest.getY();

            int realP = afterToBefore.get(distortedP);
            int realD = afterToBefore.get(distortedD);

            int expectedP = request.getX();
            int expectedD = request.getY();

            Assertions.assertEquals(expectedP, realP);
            Assertions.assertEquals(expectedD, realD);

            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedP));
            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedD));

            Assertions.assertEquals(i, nodeRequests.get(distortedP));
            Assertions.assertEquals(i, nodeRequests.get(distortedD));
        }

        Assertions.assertEquals(-1, distorter.getIndexOfRequestOfNode(999999999));
    }

    // TODO: change in DistortedTransformer getIndexOfRequestOfNode to use getNodeRequests !!
    @Test
    void simpleCompleteGraphTwoRequestsOverlapping() {
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

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));
        requests.add(new Pair<>(2, 1));

        DistortedTransformer distorter = new DistortedTransformer(g, requests);
        distorter.transform();

        Map<Integer, Collection<Integer>> beforeToAfter = distorter.getBeforeToAfterIndexes();
        Map<Integer, Integer> afterToBefore = distorter.getAfterToBeforeIndexes();
        Map<Integer, Integer> nodeRequests = distorter.getNodeRequests();
        Graph distorted = distorter.getTransformedGraph();
        List<Pair<Integer, Integer>> distortedRequests = distorter.getTransformedRequests();

        Assertions.assertNotNull(beforeToAfter);
        Assertions.assertEquals(g.getNbVertices(), beforeToAfter.size());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertTrue(afterToBefore.containsValue(i));
        }


        Assertions.assertNotNull(distorted);

        int expectedNumberOfNodes = 1 + 2 * requests.size();

        Assertions.assertEquals(expectedNumberOfNodes, distorted.getNbVertices());

        Map<String, Integer> typeCount = new HashMap<>();
        for (String type : distorter.getNodeTypes().values()) {
            typeCount.putIfAbsent(type, 0);
            typeCount.put(type, typeCount.get(type) + 1);
        }

        Assertions.assertEquals(3, typeCount.size());

        Assertions.assertEquals(1, typeCount.get("start"));
        Assertions.assertEquals(requests.size(), typeCount.get("pickup"));
        Assertions.assertEquals(requests.size(), typeCount.get("delivery"));

        Assertions.assertNotNull(distortedRequests);
        Assertions.assertEquals(requests.size(), distortedRequests.size());

        for (int i = 0; i < requests.size(); i++) {
            Pair<Integer, Integer> distortedRequest = distortedRequests.get(i);
            Pair<Integer, Integer> request = requests.get(i);

            int distortedP = distortedRequest.getX();
            int distortedD = distortedRequest.getY();

            int realP = afterToBefore.get(distortedP);
            int realD = afterToBefore.get(distortedD);

            int expectedP = request.getX();
            int expectedD = request.getY();

            Assertions.assertEquals(expectedP, realP);
            Assertions.assertEquals(expectedD, realD);

            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedP));
            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedD));

            Assertions.assertEquals(i, nodeRequests.get(distortedP));
            Assertions.assertEquals(i, nodeRequests.get(distortedD));
        }

        Assertions.assertEquals(-1, distorter.getIndexOfRequestOfNode(999999999));
    }

    @Test
    void smallCompleteGraphTwoRequestsNoOverlapping() {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));
        arcs0.add(new Pair<>(3, 2d));
        arcs0.add(new Pair<>(4, 3d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 2d));
        arcs1.add(new Pair<>(3, 2d));
        arcs1.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 2d));
        arcs2.add(new Pair<>(3, 3d));
        arcs2.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs3 = new ArrayList<>();
        arcs3.add(new Pair<>(0, 3d));
        arcs3.add(new Pair<>(1, 2d));
        arcs3.add(new Pair<>(2, 3d));
        arcs3.add(new Pair<>(4, 2d));

        Collection<Pair<Integer, Double>> arcs4 = new ArrayList<>();
        arcs4.add(new Pair<>(0, 3d));
        arcs4.add(new Pair<>(1, 2d));
        arcs4.add(new Pair<>(2, 2d));
        arcs4.add(new Pair<>(3, 3d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);
        map.put(3, arcs3);
        map.put(4, arcs4);

        Graph g = new BasicGraph(map.size(), map);

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));
        requests.add(new Pair<>(3, 4));

        DistortedTransformer distorter = new DistortedTransformer(g, requests);
        distorter.transform();

        Map<Integer, Collection<Integer>> beforeToAfter = distorter.getBeforeToAfterIndexes();
        Map<Integer, Integer> afterToBefore = distorter.getAfterToBeforeIndexes();
        Map<Integer, Integer> nodeRequests = distorter.getNodeRequests();
        Graph distorted = distorter.getTransformedGraph();
        List<Pair<Integer, Integer>> distortedRequests = distorter.getTransformedRequests();

        Assertions.assertNotNull(beforeToAfter);
        Assertions.assertEquals(g.getNbVertices(), beforeToAfter.size());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertTrue(afterToBefore.containsValue(i));
        }


        Assertions.assertNotNull(distorted);

        int expectedNumberOfNodes = 1 + 2 * requests.size();

        Assertions.assertEquals(expectedNumberOfNodes, distorted.getNbVertices());

        Map<String, Integer> typeCount = new HashMap<>();
        for (String type : distorter.getNodeTypes().values()) {
            typeCount.putIfAbsent(type, 0);
            typeCount.put(type, typeCount.get(type) + 1);
        }

        Assertions.assertEquals(3, typeCount.size());

        Assertions.assertEquals(1, typeCount.get("start"));
        Assertions.assertEquals(requests.size(), typeCount.get("pickup"));
        Assertions.assertEquals(requests.size(), typeCount.get("delivery"));

        Assertions.assertNotNull(distortedRequests);
        Assertions.assertEquals(requests.size(), distortedRequests.size());

        for (int i = 0; i < requests.size(); i++) {
            Pair<Integer, Integer> distortedRequest = distortedRequests.get(i);
            Pair<Integer, Integer> request = requests.get(i);

            int distortedP = distortedRequest.getX();
            int distortedD = distortedRequest.getY();

            int realP = afterToBefore.get(distortedP);
            int realD = afterToBefore.get(distortedD);

            int expectedP = request.getX();
            int expectedD = request.getY();

            Assertions.assertEquals(expectedP, realP);
            Assertions.assertEquals(expectedD, realD);

            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedP));
            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedD));

            Assertions.assertEquals(i, nodeRequests.get(distortedP));
            Assertions.assertEquals(i, nodeRequests.get(distortedD));
        }

        Assertions.assertEquals(-1, distorter.getIndexOfRequestOfNode(999999999));
    }

    @Test
    void smallCompleteGraphTwoRequestsOneNodeOverlapping() {
        Map<Integer, Collection<Pair<Integer, Double>>> map = new HashMap<>();
        Collection<Pair<Integer, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1, 2d));
        arcs0.add(new Pair<>(2, 3d));
        arcs0.add(new Pair<>(3, 2d));

        Collection<Pair<Integer, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0, 2d));
        arcs1.add(new Pair<>(2, 2d));
        arcs1.add(new Pair<>(3, 2d));

        Collection<Pair<Integer, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0, 3d));
        arcs2.add(new Pair<>(1, 2d));
        arcs2.add(new Pair<>(3, 3d));

        Collection<Pair<Integer, Double>> arcs3 = new ArrayList<>();
        arcs3.add(new Pair<>(0, 3d));
        arcs3.add(new Pair<>(1, 2d));
        arcs3.add(new Pair<>(2, 3d));

        map.put(0, arcs0);
        map.put(1, arcs1);
        map.put(2, arcs2);
        map.put(3, arcs3);

        Graph g = new BasicGraph(map.size(), map);

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(1, 2));
        requests.add(new Pair<>(2, 3));

        DistortedTransformer distorter = new DistortedTransformer(g, requests);
        distorter.transform();

        Map<Integer, Collection<Integer>> beforeToAfter = distorter.getBeforeToAfterIndexes();
        Map<Integer, Integer> afterToBefore = distorter.getAfterToBeforeIndexes();
        Map<Integer, Integer> nodeRequests = distorter.getNodeRequests();
        Graph distorted = distorter.getTransformedGraph();
        List<Pair<Integer, Integer>> distortedRequests = distorter.getTransformedRequests();

        Assertions.assertNotNull(beforeToAfter);
        Assertions.assertEquals(g.getNbVertices(), beforeToAfter.size());

        for (int i = 0; i < g.getNbVertices(); i++) {
            Assertions.assertTrue(afterToBefore.containsValue(i));
        }


        Assertions.assertNotNull(distorted);

        int expectedNumberOfNodes = 1 + 2 * requests.size();

        Assertions.assertEquals(expectedNumberOfNodes, distorted.getNbVertices());

        Map<String, Integer> typeCount = new HashMap<>();
        for (String type : distorter.getNodeTypes().values()) {
            typeCount.putIfAbsent(type, 0);
            typeCount.put(type, typeCount.get(type) + 1);
        }

        Assertions.assertEquals(3, typeCount.size());

        Assertions.assertEquals(1, typeCount.get("start"));
        Assertions.assertEquals(requests.size(), typeCount.get("pickup"));
        Assertions.assertEquals(requests.size(), typeCount.get("delivery"));

        Assertions.assertNotNull(distortedRequests);
        Assertions.assertEquals(requests.size(), distortedRequests.size());

        for (int i = 0; i < requests.size(); i++) {
            Pair<Integer, Integer> distortedRequest = distortedRequests.get(i);
            Pair<Integer, Integer> request = requests.get(i);

            int distortedP = distortedRequest.getX();
            int distortedD = distortedRequest.getY();

            int realP = afterToBefore.get(distortedP);
            int realD = afterToBefore.get(distortedD);

            int expectedP = request.getX();
            int expectedD = request.getY();

            Assertions.assertEquals(expectedP, realP);
            Assertions.assertEquals(expectedD, realD);

            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedP));
            Assertions.assertEquals(i, distorter.getIndexOfRequestOfNode(distortedD));

            Assertions.assertEquals(i, nodeRequests.get(distortedP));
            Assertions.assertEquals(i, nodeRequests.get(distortedD));
        }

        Assertions.assertEquals(-1, distorter.getIndexOfRequestOfNode(999999999));
    }
}
