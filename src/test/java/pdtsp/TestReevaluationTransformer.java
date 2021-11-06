package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

public class TestReevaluationTransformer {

    @Test
    void simpleGraphOneRequest()
    {
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

        ShortestPath dijkstra = new Dijkstra();
        ReevaluationTransformer reevaluator = new ReevaluationTransformer(g, requests, 0, dijkstra);
        reevaluator.transform();

        List<Integer> concernedIndexes = reevaluator.getConcernedIndexes();
        Graph transformedG = reevaluator.getTransformedGraph();

        List<Integer> expectedConcernedIndexes = new ArrayList<>() {{
            add(0);
            add(1);
            add(2);
        }};

        Assertions.assertNotNull(concernedIndexes);
        Assertions.assertNotNull(transformedG);

        Assertions.assertNotEquals(0, concernedIndexes.size());

        Assertions.assertEquals(expectedConcernedIndexes.size(), concernedIndexes.size());
        Assertions.assertEquals(expectedConcernedIndexes.size(), transformedG.getNbVertices());

        Assertions.assertTrue(concernedIndexes.containsAll(expectedConcernedIndexes));

        Map<Integer, List<Pair<Double,Integer>>> allShortestPaths = reevaluator.getShortestDistances();

        Assertions.assertNotNull(allShortestPaths);
        Assertions.assertEquals(concernedIndexes.size(), allShortestPaths.size());

        for(int startingPoint : concernedIndexes)
        {
            List<Pair<Double,Integer>> expectedShortestPath = dijkstra.searchShortestPathFrom(startingPoint, g);

            Assertions.assertNotNull(expectedShortestPath);
            Assertions.assertEquals(concernedIndexes.size(), expectedShortestPath.size());

            List<Pair<Double,Integer>> shortestPath = allShortestPaths.get(startingPoint);

            for(int i = 0; i < expectedShortestPath.size(); i++)
            {
                Assertions.assertEquals(expectedShortestPath.get(i).getX(), shortestPath.get(i).getX());
            }
        }

        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getBeforeToAfterIndexes().size());
        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getAfterToBeforeIndexes().size());

        for(int concernedIndex : concernedIndexes)
        {
            Assertions.assertTrue(reevaluator.getBeforeToAfterIndexes().containsKey(concernedIndex));
            int reevaluatedId = reevaluator.getBeforeToAfterIndexes().get(concernedIndex);
            Assertions.assertTrue(reevaluator.getAfterToBeforeIndexes().containsKey(reevaluatedId));
        }

        Assertions.assertNotNull(reevaluator.getTransformedRequests());
        Assertions.assertEquals(requests.size(), reevaluator.getTransformedRequests().size());

        List<Pair<Integer,Integer>> transformedRequests = reevaluator.getTransformedRequests();

        for(int i = 0; i < requests.size(); i++)
        {
            Pair<Integer, Integer> request = requests.get(i);
            Pair<Integer, Integer> transfomedRequest = transformedRequests.get(i);

            int realP = request.getX();
            int realD = request.getY();

            int reevP = reevaluator.getBeforeToAfterIndexes().get(realP);
            int reevD = reevaluator.getAfterToBeforeIndexes().get(realD);

            Pair<Integer,Integer> expectTransformedRequest = new Pair<>(reevP, reevD);
            Assertions.assertEquals(expectTransformedRequest.getX(), transfomedRequest.getX());
            Assertions.assertEquals(expectTransformedRequest.getY(), transfomedRequest.getY());
        }
    }

    @Test
    void simpleGraphTwoRequestSameNodes()
    {
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

        ShortestPath dijkstra = new Dijkstra();
        ReevaluationTransformer reevaluator = new ReevaluationTransformer(g, requests, 0, dijkstra);
        reevaluator.transform();

        List<Integer> concernedIndexes = reevaluator.getConcernedIndexes();
        Graph transformedG = reevaluator.getTransformedGraph();

        List<Integer> expectedConcernedIndexes = new ArrayList<>() {{
            add(0);
            add(1);
            add(2);
        }};

        Assertions.assertNotNull(concernedIndexes);
        Assertions.assertNotNull(transformedG);

        Assertions.assertNotEquals(0, concernedIndexes.size());

        Assertions.assertEquals(expectedConcernedIndexes.size(), concernedIndexes.size());
        Assertions.assertEquals(expectedConcernedIndexes.size(), transformedG.getNbVertices());

        Assertions.assertTrue(concernedIndexes.containsAll(expectedConcernedIndexes));

        Map<Integer, List<Pair<Double,Integer>>> allShortestPaths = reevaluator.getShortestDistances();

        Assertions.assertNotNull(allShortestPaths);
        Assertions.assertEquals(concernedIndexes.size(), allShortestPaths.size());

        for(int startingPoint : concernedIndexes)
        {
            List<Pair<Double,Integer>> expectedShortestPath = dijkstra.searchShortestPathFrom(startingPoint, g);

            Assertions.assertNotNull(expectedShortestPath);
            Assertions.assertEquals(concernedIndexes.size(), expectedShortestPath.size());

            List<Pair<Double,Integer>> shortestPath = allShortestPaths.get(startingPoint);

            for(int i = 0; i < expectedShortestPath.size(); i++)
            {
                Assertions.assertEquals(expectedShortestPath.get(i).getX(), shortestPath.get(i).getX());
            }
        }

        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getBeforeToAfterIndexes().size());
        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getAfterToBeforeIndexes().size());

        for(int concernedIndex : concernedIndexes)
        {
            Assertions.assertTrue(reevaluator.getBeforeToAfterIndexes().containsKey(concernedIndex));
            int reevaluatedId = reevaluator.getBeforeToAfterIndexes().get(concernedIndex);
            Assertions.assertTrue(reevaluator.getAfterToBeforeIndexes().containsKey(reevaluatedId));
        }

        Assertions.assertNotNull(reevaluator.getTransformedRequests());
        Assertions.assertEquals(requests.size(), reevaluator.getTransformedRequests().size());

        List<Pair<Integer,Integer>> transformedRequests = reevaluator.getTransformedRequests();

        for(int i = 0; i < requests.size(); i++)
        {
            Pair<Integer, Integer> request = requests.get(i);
            Pair<Integer, Integer> transfomedRequest = transformedRequests.get(i);

            int realP = request.getX();
            int realD = request.getY();

            int reevP = reevaluator.getBeforeToAfterIndexes().get(realP);
            int reevD = reevaluator.getAfterToBeforeIndexes().get(realD);

            Pair<Integer,Integer> expectTransformedRequest = new Pair<>(reevP, reevD);
            Assertions.assertEquals(expectTransformedRequest.getX(), transfomedRequest.getX());
            Assertions.assertEquals(expectTransformedRequest.getY(), transfomedRequest.getY());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,2",
            "0,1,3",
            "0,1,4",
            "0,1,5",
            "0,2,1",
            "0,2,3",
            "0,2,4",
            "0,2,5",
            "0,3,1",
            "0,3,2",
    })
    void threeConcernedNodesOnSixNodesGraph(String sS, String sP, String sD)
    {
        int testStartingPoint = Integer.parseInt(sS);
        int testPickupIndex = Integer.parseInt(sP);
        int testDeliveryIndex = Integer.parseInt(sD);

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

        List<Pair<Integer, Integer>> requests = new ArrayList<>();
        requests.add(new Pair<>(testPickupIndex, testDeliveryIndex));

        ShortestPath dijkstra = new Dijkstra();
        ReevaluationTransformer reevaluator = new ReevaluationTransformer(g, requests, testStartingPoint, dijkstra);
        reevaluator.transform();

        List<Integer> concernedIndexes = reevaluator.getConcernedIndexes();
        Graph transformedG = reevaluator.getTransformedGraph();

        List<Integer> expectedConcernedIndexes = new ArrayList<>() {{
            add(testStartingPoint);
            add(testPickupIndex);
            add(testDeliveryIndex);
        }};

        Assertions.assertNotNull(concernedIndexes);
        Assertions.assertNotNull(transformedG);

        Assertions.assertNotEquals(0, concernedIndexes.size());

        Assertions.assertEquals(expectedConcernedIndexes.size(), concernedIndexes.size());
        Assertions.assertEquals(expectedConcernedIndexes.size(), transformedG.getNbVertices());

        Assertions.assertTrue(concernedIndexes.containsAll(expectedConcernedIndexes));

        Map<Integer, List<Pair<Double,Integer>>> allShortestPaths = reevaluator.getShortestDistances();

        Assertions.assertNotNull(allShortestPaths);
        Assertions.assertEquals(concernedIndexes.size(), allShortestPaths.size());

        for(int startingPoint : concernedIndexes)
        {
            List<Pair<Double,Integer>> expectedShortestPath = dijkstra.searchShortestPathFrom(startingPoint, g);

            Assertions.assertNotNull(expectedShortestPath);
            Assertions.assertEquals(g.getNbVertices(), expectedShortestPath.size());

            List<Pair<Double,Integer>> shortestPath = allShortestPaths.get(startingPoint);

            for(int i = 0; i < expectedShortestPath.size(); i++)
            {
                Assertions.assertEquals(expectedShortestPath.get(i).getX(), shortestPath.get(i).getX());
            }
        }

        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getBeforeToAfterIndexes().size());
        Assertions.assertEquals(concernedIndexes.size(), reevaluator.getAfterToBeforeIndexes().size());

        for(int concernedIndex : concernedIndexes)
        {
            Assertions.assertTrue(reevaluator.getBeforeToAfterIndexes().containsKey(concernedIndex));
            int reevaluatedId = reevaluator.getBeforeToAfterIndexes().get(concernedIndex);
            Assertions.assertTrue(reevaluator.getAfterToBeforeIndexes().containsKey(reevaluatedId));
        }

        Assertions.assertNotNull(reevaluator.getTransformedRequests());
        Assertions.assertEquals(requests.size(), reevaluator.getTransformedRequests().size());

        List<Pair<Integer,Integer>> transformedRequests = reevaluator.getTransformedRequests();

        for(int i = 0; i < requests.size(); i++)
        {
            Pair<Integer, Integer> request = requests.get(i);
            Pair<Integer, Integer> transfomedRequest = transformedRequests.get(i);

            int realP = request.getX();
            int realD = request.getY();

            int reevP = reevaluator.getBeforeToAfterIndexes().get(realP);
            int reevD = reevaluator.getBeforeToAfterIndexes().get(realD);

            Pair<Integer,Integer> expectTransformedRequest = new Pair<>(reevP, reevD);
            Assertions.assertEquals(expectTransformedRequest.getX(), transfomedRequest.getX());
            Assertions.assertEquals(expectTransformedRequest.getY(), transfomedRequest.getY());
        }
    }
}
