package pdtsp;

public interface PDTSP {
    /**
     * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
     * and obeys the precedence graph <code>p</code>.
     * (returns the best found tour whenever the time limit is reached)
     * Warning: The computed tour always start from vertex 0
     *
     * @param timeLimit time before automatically stop the algorithm
     * @param g         complete graph
     * @param p         precedence graph
     */
    void searchSolution(int timeLimit, Graph g, Graph p);

    /**
     * @param i
     * @return the ith visited vertex in the solution computed by <code>searchSolution</code>
     * (-1 if <code>searcheSolution</code> has not been called yet, or if i < 0 or i >= g.getNbSommets())
     */
    Integer getSolution(int i);

    /**
     * @return the total cost of the solution computed by <code>searchSolution</code>
     * (-1 if <code>searcheSolution</code> has not been called yet).
     */
    Double getSolutionCost();

    /**
     * @return true if a solution has been found else false.
     */
    boolean isSolutionFound();

    /**
     * @return true if the solution found is optimal else false.
     */
    boolean isSolutionOptimal();
}
