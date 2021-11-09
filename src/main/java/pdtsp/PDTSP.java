package pdtsp;

public interface PDTSP {
    /**
     * Search for the smallest hamiltonian circuit's cost in <code>g</code> within <code>timeLimit</code> milliseconds
     * and obeys the precedence graph <code>p</code>.
     * Best solution found yet should be accessible through getSolution().
     * Warning: The computed tour always start from vertex 0
     *
     * @param timeLimit time before automatically stop the algorithm
     * @param g         complete graph
     * @param p         precedence graph
     */
    void searchSolution(int timeLimit, Graph g, Graph p);

    /**
     * These methods return the node at index i of the current best solution found.
     * Current because the algorithm could be stopped (timeLimit).
     * <p>
     * If index is out of bounds or solution is not found it should return -1.
     *
     * @param i index in the solution's list
     * @return the ith visited vertex in the solution computed by <code>searchSolution</code>
     * (-1 if <code>searcheSolution</code> has not been called yet, or if i < 0 or i >= g.getNbSommets())
     */
    Integer getSolution(int i);

    /**
     * Returns the cost of the current best solution found.
     * <p>
     * If the solution is yet to be found, it should return -1.
     *
     * @return the total cost of the solution computed by <code>searchSolution</code>
     * (-1 if <code>searcheSolution</code> has not been called yet).
     */
    Double getSolutionCost();

    /**
     * Returns true if a solution has been found.
     *
     * @return true if a solution has been found else false.
     */
    boolean isSolutionFound();

    /**
     * Returns true if the solution is optimal.
     * Optimal means that the current PDTSP implement cannot find a better solution.
     * i.e. It means that a better solution can exist but not in our domain of research.
     *
     * @return true if the solution found is optimal else false.
     */
    boolean isSolutionOptimal();
}
