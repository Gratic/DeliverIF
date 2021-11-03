package pdtsp;

import java.util.List;

public interface RequestsTransformer {
    /**
     * @return a collection of requests that have been reevaluated.
     */
    List<Pair<Integer, Integer>> getTransformedRequests();
}
