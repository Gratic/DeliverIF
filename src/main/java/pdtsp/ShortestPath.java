package pdtsp;

import java.util.List;

public interface ShortestPath {
    /**
     * @param startingNodeIndex the index of the starting node in the graph g
     * @param g the graph
     * @return A list of pair of all reachable node from startingNodeIndex, first element must be the distance, second element the node to get from to have this distance (recursive).
     */
    List<Pair<Double, Integer>> searchShortestPathFrom(Integer startingNodeIndex, Graph g);
}
