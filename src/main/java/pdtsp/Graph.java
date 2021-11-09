package pdtsp;

import java.util.Collection;

/**
 *
 */
public interface Graph {
    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices in <code>this</code>
     */
    int getNbVertices();

    /**
     * Returns the cost between two nodes.
     * Returns -1 if there is no connection between the nodes or if one of the node is not in the graph.
     *
     * @param i source
     * @param j destination
     * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
     */
    Double getCost(int i, int j);

    /**
     * Returns if there is an arc between i and j.
     *
     * @param i source
     * @param j destination
     * @return true if <code>(i,j)</code> is an arc of <code>this</code>
     */
    boolean isArc(int i, int j);

    /**
     * Returns a list of arcs outgoing the node i.
     *
     * @param i source
     * @return a collection of reachable arcs from <code>i</code>.
     */
    Collection<Integer> getOutgoingArcs(int i);
}
