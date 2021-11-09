package pdtsp;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implements the famous dijkstra algorithm with a priority queue.
 * Distances between arcs must be POSITIVE !!
 * Else the algorithm won't work as intended.
 * <p>
 * WARNING NOTE: graph g must contain node indexes from [0, nbVertices[ with g.getNbVertices() vertices.
 * Else the algorithm won't work as it uses a list for result (using node indexes as indices for the list).
 */
public class Dijkstra implements ShortestPath {
    @Override
    public List<Pair<Double, Integer>> searchShortestPathFrom(Integer startingNodeIndex, Graph g) {
        List<Pair<Double, Integer>> results = new ArrayList<>(g.getNbVertices());
        PriorityQueue<Pair<Double, Integer>> priorityQueue = new PriorityQueue<>();

        // This is a little time optimisation (but a trade for space optimisation)
        // We don't need to visit twice a node.
        boolean[] visited = new boolean[g.getNbVertices()];

        // Initialization : all distances must be INFINITY, and unreachable (stated with a -1).
        for (int i = 0; i < g.getNbVertices(); i++) {
            Pair<Double, Integer> pair = new Pair<>(Double.POSITIVE_INFINITY, -1);
            results.add(pair);
            visited[i] = false;
        }

        // All distances must be INFINITY except for the starting point where distance is obviously 0.
        // We also add it to the priority queue.
        results.get(startingNodeIndex).setX(0d);
        priorityQueue.add(new Pair<>(0d, startingNodeIndex));

        while (!priorityQueue.isEmpty()) {
            Pair<Double, Integer> currentNode = priorityQueue.poll();

            // There is duplicates in the priority queue, so we can just ignore if we already visited the node.
            if (!visited[currentNode.getY()]) {
                visited[currentNode.getY()] = true;

                for (int reachableNode : g.getOutgoingArcs(currentNode.getY())) {
                    // If the node is visited it is already optimal (=> priority queue).
                    if (!visited[reachableNode]) {
                        if (results.get(currentNode.getY()).getX() + g.getCost(currentNode.getY(), reachableNode) < results.get(reachableNode).getX()) {
                            results.get(reachableNode).setX(results.get(currentNode.getY()).getX() + g.getCost(currentNode.getY(), reachableNode));
                            results.get(reachableNode).setY(currentNode.getY());
                        }

                        // We add the node to the priority queue AND we are sure that we have not visited it yet.
                        // That doesn't exclude duplication.
                        priorityQueue.add(new Pair<>(results.get(reachableNode).getX(), reachableNode));
                    }
                }
            }
        }

        return results;
    }
}
