package pdtsp;

public interface GraphTransformer {
    /**
     * @return a graph that has been reevaluated.
     */
    Graph getTransformedGraph();
}
