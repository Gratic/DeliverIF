package pdtsp;

import java.util.Collection;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	int getNbVertices();

	/**
	 * @param i source
	 * @param j destination
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	Double getCost(int i, int j);

	/**
	 * @param i source
	 * @param j destination
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	boolean isArc(int i, int j);

	/**
	 * @param i source
	 * @return a collection of reachable arcs from <code>i</code>.
	 */
	Collection<Integer> getOutgoingArcs(int i);
}
