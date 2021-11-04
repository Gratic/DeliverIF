package pdtsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A basic implementation of the interface Graph.
 * All nodes are identified by a positive integer (as it uses matrices).
 * Distances between nodes must be Double.
 * Distances can be negative.
 */
public class BasicGraph implements Graph {
    private final Double[][] adjacencyMatrix;
    private final Map<Integer, Collection<Integer>> adjacencyList;
    private final Integer nbVertices;

    public BasicGraph(int nbVertices, Map<Integer, Collection<Pair<Integer, Double>>> adjacencyListWithCost) {
        this.nbVertices = nbVertices;
        this.adjacencyMatrix = new Double[nbVertices][nbVertices];
        this.adjacencyList = new HashMap<>();

        // Initializing the adjacencyMatrix
        for (int i = 0; i < nbVertices; i++) {
            for (int j = 0; j < nbVertices; j++) {
                this.adjacencyMatrix[i][j] = -1d;
            }
        }

        // Building adjacencyList and the adjacencyMatrix
        for (int i = 0; i < nbVertices; i++) {
            Collection<Integer> reachableNodes = new ArrayList<>();

            for (Pair<Integer, Double> pair : adjacencyListWithCost.get(i)) {
                Integer reachableNode = pair.getX();
                Double cost = pair.getY();
                this.adjacencyMatrix[i][reachableNode] = cost;

                reachableNodes.add(reachableNode);
            }
            this.adjacencyList.put(i, reachableNodes);
        }
    }

    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    @Override
    public Double getCost(int i, int j) {
        if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices)
            return -1d;
        return adjacencyMatrix[i][j];
    }

    @Override
    public boolean isArc(int i, int j) {
        if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices)
            return false;
        return adjacencyMatrix[i][j] != -1;
    }

    @Override
    public Collection<Integer> getOutgoingArcs(int i) {
        return adjacencyList.get(i);
    }

    public static void printGraph(Graph g) {
        System.out.println("number vertices=" + g.getNbVertices());
        for (int i = 0; i < g.getNbVertices(); i++) {
            ArrayList<Integer> outgoingArcs = new ArrayList<>(g.getOutgoingArcs(i));
            System.out.print(i + " -> [");
            for (int j = 0; j < outgoingArcs.size(); j++) {
                int node = outgoingArcs.get(j);
                String s = "[" + node + ", w=" + g.getCost(i, node) + "]";
                if (j == outgoingArcs.size() - 1) {
                    System.out.print(s);
                } else {
                    System.out.print(s + ",");
                }
            }
            System.out.println("]");

        }
    }
}
