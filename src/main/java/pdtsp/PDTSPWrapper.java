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
 * <p>
 * The basic method order calling :
 * 1) Constructor
 * 2) prepare()
 * 3) compute()
 * 4) updateDeliveryTour()
 * <p>
 * Note: this implementation makes the algorithm works on a different thread to be able
 * to pause and resume at any time or pause automatically the algorithm after a time limit.
 * This means that even though compute has "finished" the solution could be yet to be found and/or not optimal.
 * In that case, to continue the search, calling compute() is enough.
 * But, if you want to stop the algorithm and keep the current solution, you must kill the thread by calling
 * killAlgorithmThread().
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


    private final BasicGraphWrapper graphWrapper;

    private final DeliveryTour deliveryTour;

    private RunnablePDTSP pdtsp;
    private Thread pdtspThread;
    boolean isPreparationDone;

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

    /**
     * Usually the first method to be called.
     * <p>
     * Prepare everything so that there still only need to do the actual computation.
     * <p>
     * The graph must be transformed to be actually computable.
     * What is a computable graph :
     * - Starting node is 0.
     * - The graph is complete.
     */
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
                pdtsp = new PaperPDTSP(timeLimit, distorter.getTransformedGraph(), precedencer.getTransformedGraph());
                pdtspThread = new Thread(pdtsp);
                pdtspThread.start();

                while (!pdtsp.isReady()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * The actual computation.
     * <p>
     * In fact, the algorithm runs on another thread. And the code doing the actual search is in another class.
     */
    public void compute() {
        if (isPreparationDone && !isSolutionOptimal() && pdtsp.isRunning()) {
            if (DEBUG) {
                System.out.println("distorted");
                BasicGraph.printGraph(distorter.getTransformedGraph());
                System.out.println("p");
                BasicGraph.printGraph(precedencer.getTransformedGraph());
            }

            pdtsp.resume();

            while (!pdtsp.isPaused()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns a list of long corresponding to the path found by the algorithm.
     * <p>
     * A solution must be found before.
     *
     * @return a list of long corresponding to the path found.
     */
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

        for (int node : realPathInteger) {
            realPath.add(graphWrapper.idConvert(node));
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

    /**
     * Usually one of the last method.
     * <p>
     * Compute and a path must be found before.
     * <p>
     * Effectively updates the DeliveryTour object with the path found by the algorithm.
     */
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
        deliveryTour.clear(false);

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
                    deliveryTour.addAddressNoNotify(currentAddress, EnumAddressType.DEPARTURE_ADDRESS, null);
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "pickup" -> {
                    Integer requestNumber = listNodeRequest.get(i);
                    deliveryTour.addAddressNoNotify(currentAddress, EnumAddressType.PICKUP_ADDRESS, requests.get(requestNumber));
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "delivery" -> {
                    Integer requestNumber = listNodeRequest.get(i);
                    deliveryTour.addAddressNoNotify(currentAddress, EnumAddressType.DELIVERY_ADDRESS, requests.get(requestNumber));
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
                case "traversal" -> {
                    deliveryTour.addAddressNoNotify(currentAddress, EnumAddressType.TRAVERSAL_ADDRESS, null);
                    deliveryTour.getPath().add(rsCurrentToNext);
                }
            }
        }

        // We add the last address.
        deliveryTour.addAddressNoNotify(graphWrapper.getMap().getAddressById(realPath.get(realPath.size() - 1)), EnumAddressType.DEPARTURE_ADDRESS, null);
        deliveryTour.notifyObservers(deliveryTour);
    }

    /**
     * Get the real (shortest) path between to node.
     * Starting node is not included.
     * <p>
     * NOTE: this must be used with REAL WORLD node from the REAL WORLD graph (a.k.a. map).
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

    public void killAlgorithmThread() {
        pdtsp.kill();

        try {
            pdtspThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
