package pdtsp;

import java.util.*;

public class DistortedTransformer implements GraphTransformer, RequestsTransformer {
    private Graph reevaluated;
    private List<Pair<Integer, Integer>> reevaluatedRequests;

    private Map<Integer, String> nodeTypes;                                     // Map of node types. Node ID's are distorted world ID.
    private Map<Integer, Integer> nodeRequests;                                  // Map of node with request. Second field is the request index in distortedRequest.
    private Map<Integer, Integer> afterToBeforeIndexes;                         // Index from distorted world to reevaluated world
    private Map<Integer, Collection<Integer>> beforeToAfterIndexes;             // Index from reevaluated world to distorted world
    private Map<Integer, Collection<Pair<Integer, Double>>> distortedWorld;     // Distorted graph, adjacency list. Pair<Node ID, Distance>
    private List<Pair<Integer, Integer>> distortedRequests;               // Each request with nodes reevaluted in the distorted graph.

    private Graph distortedGraph;

    public DistortedTransformer(Graph reevaluated, List<Pair<Integer, Integer>> reevaluatedRequests)
    {
        this.reevaluated = reevaluated;
        this.reevaluatedRequests = reevaluatedRequests;

        nodeTypes = new HashMap<>();
        nodeRequests = new HashMap<>();
        afterToBeforeIndexes = new HashMap<>();
        beforeToAfterIndexes = new HashMap<>();
        distortedWorld = new HashMap<>();
        distortedRequests = new ArrayList<>();
    }

    /**
     * Transform a reevaluated graph into a distorted graph.
     * A distorted graph is the name I use to qualify a graph that had been twisted in a way that
     * there is no overlapping between requests. I actually don't know if there will be a case when a problem
     * with overlapping will occur. But in case, this propose a general solution to the problem.
     * The notable side effect is that more nodes can be present than in the reevaluated graph.
     * Resulting in a longer TSP computation.
     */
    public void transform()
    {
        // TASK 1: Re-index every nodes AND recompute requests in the distorted world

        nodeTypes.put(0, "start");
        afterToBeforeIndexes.put(0, 0);
        beforeToAfterIndexes.put(0, new ArrayList<>(){{add(0);}});

        // Will serve to re-index nodes.
        int currentDistortedWorldIndex = 1;

        // The goal of distorted world is to have no overlapping between requests
        // So we loop on reevaluatedRequests to give each node an individual index
        for(int l = 0; l < reevaluatedRequests.size(); l++)
        {
            Pair<Integer, Integer> requestPair = reevaluatedRequests.get(l);

            int pickupPoint = requestPair.getX();
            int deliveryPoint = requestPair.getY();

            // We need to know to create the distorted request
            int distortedPickupPoint = -1;
            int distortedDeliveryPoint = -1;

            // Looping on every node of the reevaluated world.
            // Note that this requires that nodes are valued in [1, getNbVertices[.
            // O must be the starting point and we already treated that part.
            for(int i = 1; i < reevaluated.getNbVertices(); i++)
            {
                if(beforeToAfterIndexes.get(i) == null)
                {
                    beforeToAfterIndexes.put(i, new ArrayList<>());
                }

                // Classification
                if(pickupPoint == i)
                {
                    // Adding to the indexes
                    beforeToAfterIndexes.get(i).add(currentDistortedWorldIndex);
                    afterToBeforeIndexes.put(currentDistortedWorldIndex, i);

                    distortedPickupPoint = currentDistortedWorldIndex;
                    nodeTypes.put(currentDistortedWorldIndex, "pickup");
                    nodeRequests.put(currentDistortedWorldIndex, l);

                    currentDistortedWorldIndex++;
                }
                else if (deliveryPoint == i)
                {
                    // Adding to the indexes
                    beforeToAfterIndexes.get(i).add(currentDistortedWorldIndex);
                    afterToBeforeIndexes.put(currentDistortedWorldIndex, i);

                    distortedDeliveryPoint = currentDistortedWorldIndex;
                    nodeTypes.put(currentDistortedWorldIndex, "delivery");
                    nodeRequests.put(currentDistortedWorldIndex, l);

                    currentDistortedWorldIndex++;
                }
            }
            // Creating the distorted request AND adding it to the array
            Pair<Integer, Integer> distortedRequest = new Pair<>();
            distortedRequest.setX(distortedPickupPoint);
            distortedRequest.setY(distortedDeliveryPoint);

            distortedRequests.add(distortedRequest);
        }

        // TASK 2: Build the distorted world

        // We want to build the graph in a way that it is equivalent to the reevaluated world.
        // But we can have more nodes. It means that some arcs will be duplicated.
        // And between nodes that has been duplicated it exists a path which value is 0.
        // (Because we search the shortest path, and there is no distance between a node and itself)

        int nbDistortedNodes = afterToBeforeIndexes.size();

        for(int i = 0; i < nbDistortedNodes; i++)
        {
            int realPointI = afterToBeforeIndexes.get(i);

            Collection<Pair<Integer, Double>> distortedArcs = new ArrayList<>();

            for(int j = 0; j < nbDistortedNodes; j++)
            {
                // This case is not possible as the goal of TASK 1 is to UNIQUELY differentiate nodes.
                if(i == j) continue;

                int realPointJ = afterToBeforeIndexes.get(j);

                Pair<Integer, Double> distortedArc = new Pair<>();
                distortedArc.setX(j);

                // Case of a duplication of same node because there was more than one request (as a pickup or delivery point)
                // on the same node.
                if(realPointI == realPointJ)
                {
                    distortedArc.setY(0d);
                }
                else
                {
                    // If it not a duplication, we just copy the cost of the original points.
                    // Also we know that there is a cost because reevaluated is a complete graph.
                    distortedArc.setY(reevaluated.getCost(realPointI, realPointJ));
                }

                distortedArcs.add(distortedArc);
            }

            distortedWorld.put(i, distortedArcs);
        }

        distortedGraph = new BasicGraph(distortedWorld.size(), distortedWorld);
    }

    @Override
    public Graph getTransformedGraph() {
        return distortedGraph;
    }

    @Override
    public List<Pair<Integer, Integer>> getTransformedRequests() {
        return distortedRequests;
    }

    public Map<Integer, Integer> getAfterToBeforeIndexes() {
        return afterToBeforeIndexes;
    }

    public Map<Integer, Collection<Integer>> getBeforeToAfterIndexes() {
        return beforeToAfterIndexes;
    }

    public Map<Integer, String> getNodeTypes() { return nodeTypes; }

    public Map<Integer, Integer> getNodeRequests() { return nodeRequests; }

    public Integer getIndexOfRequestOfNode(Integer distortedNodeIndex)
    {
        for(int i = 0; i < distortedRequests.size(); i++)
        {
            Pair<Integer, Integer> request = distortedRequests.get(i);

            if(request.getX().equals(distortedNodeIndex) || request.getY().equals(distortedNodeIndex))
                return i;
        }

        return -1;
    }
}
