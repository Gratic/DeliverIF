package pdtsp;

import deliverif.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class is the bridge between the real world and the algorithmic world.
 * <p>
 * It is also the only class to call if there is a need for a TSP Computation.
 */
public class PDTSPWrapper {
    private final boolean DEBUG = false;

    private final List<Request> requests;
    private final List<Pair<Integer, Integer>> requestsInteger;
    private final Integer startPointInteger;

    private final Integer timeLimit;

    private ReevaluationTransformer reevaluator;
    private DistortedTransformer distorter;
    private PrecedenceTransformer precedencer;

    private PDTSP pdtsp;

    boolean isPreparationDone;

    private final BasicGraphWrapper graphWrapper;

    private final DeliveryTour deliveryTour;

    public PDTSPWrapper(CityMap cityMap, DeliveryTour deliveryTour, Integer timeLimit) {
        this.timeLimit = timeLimit;


        this.requestsInteger = new ArrayList<>();


        this.deliveryTour = deliveryTour;
        this.requests = deliveryTour.getRequests();

        Address startPoint = deliveryTour.getDepartureAddress();

        // TASK 1: Re-index long indexes into integer indexes because BasicGraph only takes integer.
        this.graphWrapper = new BasicGraphWrapper(cityMap);

        this.startPointInteger = graphWrapper.idConvert(startPoint.getId());

        // TASK 2: Building the integer graph


        // TASK 3: Compute the integer requests

        // Request order is (should be) conserved.
        for (Request requestObj : requests) {
            long longPickupNode = requestObj.getPickupAddress().getId();
            long longDeliveryNode = requestObj.getDeliveryAddress().getId();

            int integerPickupNode = graphWrapper.idConvert(longPickupNode);
            int integerDeliveryNode = graphWrapper.idConvert(longDeliveryNode);

            requestsInteger.add(new Pair<>(integerPickupNode, integerDeliveryNode));
        }

        // Finally, we can create the graph.

        isPreparationDone = false;
    }

    public void prepare() {
        if (!isPreparationDone) {
            // TASK 1: Reevaluation
            ShortestPath dijkstra = new Dijkstra();
            reevaluator = new ReevaluationTransformer(graphWrapper.getGraph(), requestsInteger, startPointInteger, dijkstra);
            reevaluator.transform();

            Graph reevaluated = reevaluator.getTransformedGraph();
            List<Pair<Integer, Integer>> reevaluatedRequests = reevaluator.getTransformedRequests();

            if (DEBUG) {
                System.out.println("reevaluated");
                System.out.println("concerned indexes=" + reevaluator.getConcernedIndexes());
                System.out.println("reevaluatedPortalIn=" + reevaluator.getBeforeToAfterIndexes());
                System.out.println("reevaluatedPortalOut=" + reevaluator.getAfterToBeforeIndexes());
                BasicGraph.printGraph(reevaluated);
            }


            // TASK 2: Distortion
            distorter = new DistortedTransformer(reevaluated, reevaluatedRequests);
            distorter.transform();

            List<Pair<Integer, Integer>> distortedRequests = distorter.getTransformedRequests();
            Map<Integer, Integer> distortedPortalOut = distorter.getAfterToBeforeIndexes();

            // TASK 3: Precedence
            precedencer = new PrecedenceTransformer(distortedPortalOut.size(), distortedRequests);
            precedencer.transform();

            isPreparationDone = true;

            if (pdtsp == null) {
                pdtsp = new BnBPDTSP();
            }
        }

    }

    public void compute() {
        if (isPreparationDone) {
            if (DEBUG) {
                System.out.println("distorted");
                BasicGraph.printGraph(distorter.getTransformedGraph());
                System.out.println("p");
                BasicGraph.printGraph(precedencer.getTransformedGraph());
            }

            pdtsp.searchSolution(timeLimit, distorter.getTransformedGraph(), precedencer.getTransformedGraph());
        }
    }

    public List<Long> getPath() {
        Graph distorted = distorter.getTransformedGraph();
        Map<Integer, Integer> distortedPortalOut = distorter.getAfterToBeforeIndexes();
        Map<Integer, Integer> reevaluationPortalOut = reevaluator.getAfterToBeforeIndexes();
        List<Integer> realPathInteger = new ArrayList<>();
        List<Long> realPath = new ArrayList<>();
        Map<Integer, List<Pair<Double, Integer>>> shortestPaths = reevaluator.getShortestDistances();

        int previous = reevaluationPortalOut.get(distortedPortalOut.get(0));
        realPathInteger.add(previous);

        for (int i = 1; i < distorted.getNbVertices(); i++) {
            int node = pdtsp.getSolution(i);
            int currentNode = reevaluationPortalOut.get(distortedPortalOut.get(node));

            realPathInteger.addAll(getRealPath(previous, currentNode, shortestPaths));

            previous = currentNode;
        }

        realPathInteger.addAll(getRealPath(previous, startPointInteger, shortestPaths));

        for(int node : realPathInteger)
        {
            realPath.add(intToLong.get(node));
        }

        if (DEBUG) {
            System.out.println("Path found :");
            System.out.print("[");
            for (int i = 0; i < distorted.getNbVertices(); i++) {
                int node = pdtsp.getSolution(i);
                if (i == distorted.getNbVertices() - 1) {
                    System.out.print(node);
                } else {
                    System.out.print(node + ",");
                }
            }
            System.out.println("]");
            System.out.println("SolutionCost=" + pdtsp.getSolutionCost());
            System.out.println("Conversion in reevaluated World :");
            System.out.print("[");
            for (int i = 0; i < distorted.getNbVertices(); i++) {
                int node = pdtsp.getSolution(i);
                if (i == distorted.getNbVertices() - 1) {
                    System.out.print(distortedPortalOut.get(node));
                } else {
                    System.out.print(distortedPortalOut.get(node) + ",");
                }
            }
            System.out.println("]");

            System.out.println("Conversion in real World :");
            System.out.print("[");
            for (int i = 0; i < distorted.getNbVertices(); i++) {
                int node = pdtsp.getSolution(i);
                if (i == distorted.getNbVertices() - 1) {
                    System.out.print(reevaluationPortalOut.get(distortedPortalOut.get(node)));
                } else {
                    System.out.print(reevaluationPortalOut.get(distortedPortalOut.get(node)) + ",");
                }
            }
            System.out.println("]");

            System.out.println("Realpath:");
            System.out.print("[");
            for (int i = 0; i < realPathInteger.size(); i++) {
                int node = realPathInteger.get(i);
                if (i == realPathInteger.size() - 1) {
                    System.out.print(node);
                } else {
                    System.out.print(node + ",");
                }
            }
            System.out.println("]");
        }

        return realPath;
    }

    public void updateDeliveryTour() {
        Graph distorted = distorter.getTransformedGraph();
        Map<Integer, Integer> distortedPortalOut = distorter.getAfterToBeforeIndexes();
        Map<Integer, String> distortedNodeTypes = distorter.getNodeTypes();

        Map<Integer, Integer> reevaluationPortalOut = reevaluator.getAfterToBeforeIndexes();
        Map<Integer, List<Pair<Double, Integer>>> shortestPaths = reevaluator.getShortestDistances();

        List<String> listTypes = new ArrayList<>();
        List<Integer> listNodeRequest = new ArrayList<>();
        List<Integer> realPathInteger = new ArrayList<>();
        List<Long> realPath = new ArrayList<>();

        // TASK 1: Collecting information about each node (address)
        // Information are, type of node (EnumAddressType), and the request they are part of.

        // Starting node.
        int previous = reevaluationPortalOut.get(distortedPortalOut.get(0));
        listTypes.add("start");
        listNodeRequest.add(null);
        realPathInteger.add(previous);

        // All nodes except Starting node.
        for (int i = 1; i < distorted.getNbVertices(); i++) {
            int node = pdtsp.getSolution(i);
            String nodeType = distortedNodeTypes.get(node);
            int currentNode = reevaluationPortalOut.get(distortedPortalOut.get(node));

            List<Integer> tempRealPath = getRealPath(previous, currentNode, shortestPaths);
            for (int j = 0; j < tempRealPath.size() - 1; j++) {
                listTypes.add("traversal");
                listNodeRequest.add(null);
            }
            listTypes.add(nodeType);
            listNodeRequest.add(distorter.getIndexOfRequestOfNode(node));

            realPathInteger.addAll(tempRealPath);

            previous = currentNode;
        }

        // From last node to Starting node.
        List<Integer> tempRealPath = getRealPath(previous, startPointInteger, shortestPaths);
        for (int j = 0; j < tempRealPath.size() - 1; j++) {
            listTypes.add("traversal");
            listNodeRequest.add(null);
        }
        listTypes.add("end");
        listNodeRequest.add(null);

        realPathInteger.addAll(getRealPath(previous, startPointInteger, shortestPaths));

        // Conversion in real world node ID.
        for (Integer node : realPathInteger) {
            realPath.add(graphWrapper.idConvert(node));
        }

        // TASK 2: Updating DeliveryTour.
        deliveryTour.getPath().clear();
        deliveryTour.getPathAddresses().clear();
        deliveryTour.getAddressRequestMetadata().clear();

        for (int i = 0; i < realPath.size() - 1; i++) {
            long longCurrentNode = realPath.get(i);
            long longNextNode = realPath.get(i + 1);

            int intCurrentNode = graphWrapper.idConvert(longCurrentNode);
            int intNextNode = graphWrapper.idConvert(longNextNode);

            Address currentAddress = graphWrapper.getMap().getAddressById(longCurrentNode);
            RoadSegment rsCurrentToNext = graphWrapper.getSegment(intCurrentNode, intNextNode);

            String nodeType = listTypes.get(i);
            switch (nodeType) {
                case "start", "end" -> {
                    deliveryTour.addAddress(currentAddress, EnumAddressType.DEPARTURE_ADDRESS, null);
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "pickup" -> {
                    Integer requestNumber = listNodeRequest.get(i);
                    deliveryTour.addAddress(currentAddress, EnumAddressType.PICKUP_ADDRESS, requests.get(requestNumber));
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "delivery" -> {
                    Integer requestNumber = listNodeRequest.get(i);
                    deliveryTour.addAddress(currentAddress, EnumAddressType.DELIVERY_ADDRESS, requests.get(requestNumber));
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "traversal" -> {
                    Integer requestNumber = listNodeRequest.get(i);
                    deliveryTour.addAddress(currentAddress, EnumAddressType.TRAVERSAL_ADDRESS, null);
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
            }
        }

        // We add the last address.
        deliveryTour.addAddress(graphWrapper.getMap().getAddressById(realPath.get(realPath.size() - 1)), EnumAddressType.DEPARTURE_ADDRESS, null);
    }

    /**
     * Get the real (shortest) path between to node.
     * Starting node is not included.
     * <p>
     * NOTE: this must be used with REAL WORLD node from the REAL WORLD graph (a.k.a map).
     */
    private static List<Integer> getRealPath(Integer i, Integer j, Map<Integer, List<Pair<Double, Integer>>> shortestPaths) {
        List<Pair<Double, Integer>> result = shortestPaths.get(i);
        List<Integer> path = new ArrayList<>();

        path.add(j);

        Integer currentNode = result.get(j).getY();
        while (!currentNode.equals(i) && !currentNode.equals(-1)) {
            path.add(currentNode);
            currentNode = result.get(currentNode).getY();
        }

        Collections.reverse(path);

        return path;
    }

    public boolean isSolutionFound() {
        return pdtsp.isSolutionFound();
    }

    public boolean isSolutionOptimal() {
        return pdtsp.isSolutionOptimal();
    }

    public double getSolutionCost() {
        return pdtsp.getSolutionCost();
    }
}
