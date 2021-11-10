package pdtsp;

import java.util.*;

/**
 * The goal in this transformation is to simplify the graph by keeping only the concerned nodes.
 * Also, this graph makes arc between every concerned nodes with a weight equal to the cost of the shortest path between
 * the two.
 * <p>
 * Plus change the index of the starting node to 0 as it is required for the PDTSP algorithm to work.
 * <p>
 * A concerned node is a node in a request or the starting node.
 * <p>
 * If there is n requests there is at most 2n+1 nodes (1 is the starting node).
 */
public class ReevaluationTransformer implements GraphTransformer, RequestsTransformer {
    private final List<Pair<Integer, Integer>> requests;
    private final Graph map;
    private final int startPoint;
    private final ShortestPath shortestPathAlgorithm;

    private final Map<Integer, List<Pair<Double, Integer>>> shortestDistances;
    private final Map<Integer, Collection<Pair<Integer, Double>>> reevaluatedWorld;
    private final List<Integer> concernedIndexes;
    private final Map<Integer, Integer> afterToBeforeIndexes;
    private final Map<Integer, Integer> beforeToAfterIndexes;
    private final List<Pair<Integer, Integer>> reevaluatedRequests;

    private Graph reevaluationGraph;

    public ReevaluationTransformer(Graph map, List<Pair<Integer, Integer>> requests, int startPoint, ShortestPath shortestPathAlgorithm) {
        this.map = map;
        this.requests = requests;
        this.startPoint = startPoint;
        this.shortestPathAlgorithm = shortestPathAlgorithm;

        shortestDistances = new HashMap<>();
        reevaluatedWorld = new HashMap<>();
        reevaluatedRequests = new ArrayList<>();
        concernedIndexes = new ArrayList<>();
        afterToBeforeIndexes = new HashMap<>();
        beforeToAfterIndexes = new HashMap<>();
    }

    /**
     * Transform the whole map in only the nodes that interest us (referred as concerned Nodes).
     * Nodes that interest us are :
     * - starting node
     * - nodes that are concerned by request (as pickup or delivery point)
     * We need to trim the number of nodes to cut down the time it takes for the TSP computation.
     * But the main benefit is that it will be simpler to implement a TSP algorithm.
     * <p>
     * Also this step will put the starting node as node 0.
     * This is required for the TSP to run.
     */
    public void transform() {
        // TASK 1: Filter concerned nodes.

        for (Pair<Integer, Integer> request : requests) {
            if (request.getX() != startPoint) concernedIndexes.add(request.getX());
            if (request.getY() != startPoint) concernedIndexes.add(request.getY());
        }

        // We make elements unique as the same node can be in multiple requests.
        Set<Integer> set = new LinkedHashSet<>(concernedIndexes);
        concernedIndexes.clear();
        concernedIndexes.addAll(set);

        // TASK 2: Re-index all concerned nodes and starting node

        // Putting the starting node as node 0.
        afterToBeforeIndexes.put(0, startPoint);
        beforeToAfterIndexes.put(startPoint, 0);

        int reevaluationIndex = 1;
        for (int index : concernedIndexes) {
            afterToBeforeIndexes.put(reevaluationIndex, index);
            beforeToAfterIndexes.put(index, reevaluationIndex);
            reevaluationIndex++;
        }

        // TASK 3: Reevaluating requests
        // Order is (should be) conserved.
        for (Pair<Integer, Integer> request : requests) {
            Pair<Integer, Integer> reevaluatedRequest = new Pair<>();
            reevaluatedRequest.setX(beforeToAfterIndexes.get(request.getX()));
            reevaluatedRequest.setY(beforeToAfterIndexes.get(request.getY()));
            reevaluatedRequests.add(reevaluatedRequest);
        }

        // Adding back startingPoint because it will simplify further operations.
        concernedIndexes.add(startPoint);

        // TASK 4: Find all shortest distances between each concernedNodes.

        // We put results in a map.
        for (int concernedIndex : concernedIndexes) {
            shortestDistances.put(concernedIndex, shortestPathAlgorithm.searchShortestPathFrom(concernedIndex, map));
        }

        // TASK 5: Building the reevaluated world

        for (int realWorldIndexI : beforeToAfterIndexes.keySet()) {
            int reevaluatedWorldIndexI = beforeToAfterIndexes.get(realWorldIndexI);

            reevaluatedWorld.put(reevaluatedWorldIndexI, new ArrayList<>());

            for (int realWorldIndexJ : beforeToAfterIndexes.keySet()) {
                int reevaluatedWorldIndexJ = beforeToAfterIndexes.get(realWorldIndexJ);
                if (reevaluatedWorldIndexI == reevaluatedWorldIndexJ) continue;

                Pair<Integer, Double> arc = new Pair<>();
                arc.setX(reevaluatedWorldIndexJ);
                arc.setY(shortestDistances.get(realWorldIndexI).get(realWorldIndexJ).getX());
                reevaluatedWorld.get(reevaluatedWorldIndexI).add(arc);
            }
        }

        reevaluationGraph = new BasicGraph(reevaluatedWorld.size(), reevaluatedWorld);
    }

    @Override
    public Graph getTransformedGraph() {
        return reevaluationGraph;
    }

    @Override
    public List<Pair<Integer, Integer>> getTransformedRequests() {
        return reevaluatedRequests;
    }

    public List<Integer> getConcernedIndexes() {
        return concernedIndexes;
    }

    public Map<Integer, Integer> getAfterToBeforeIndexes() {
        return afterToBeforeIndexes;
    }

    public Map<Integer, Integer> getBeforeToAfterIndexes() {
        return beforeToAfterIndexes;
    }

    public Map<Integer, List<Pair<Double, Integer>>> getShortestDistances() {
        return shortestDistances;
    }


}
