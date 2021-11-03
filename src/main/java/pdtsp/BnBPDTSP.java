package pdtsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// TODO: Possibility to stop and continue the computation.
public class BnBPDTSP implements PDTSP {
    private static final boolean DEBUG = false;

    private Integer[] bestSol;
    protected Graph g;
    protected Graph pred;
    private Double bestSolCost;
    private int timeLimit;
    private long startTime;

    private boolean computationStarted = false;
    private Collection<Integer> unvisited;
    private Collection<Integer> visited;
    private boolean isSolutionFound = false;
    private boolean isOptimal = false;

    // TODO: Working stop/resume
    @Override
    public void searchSolution(int timeLimit, Graph g, Graph pred) {
        if(timeLimit <= 0) return;

        this.timeLimit = timeLimit;

        if(!computationStarted)
        {
            computationStarted = true;
            this.g = g;
            this.pred = pred;
            bestSol = new Integer[g.getNbVertices()];
            unvisited = new ArrayList<>(g.getNbVertices()-1);
            for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
            visited = new ArrayList<>(g.getNbVertices());
            visited.add(0);
            bestSolCost = Double.POSITIVE_INFINITY;
        }

        startTime = System.currentTimeMillis();

        branchAndBound(0, unvisited, visited, 0d);
    }

    @Override
    public Integer getSolution(int i) {
        if (g != null && i>=0 && i<g.getNbVertices())
            return bestSol[i];
        return -1;
    }

    @Override
    public Double getSolutionCost() {
        if (g != null)
            return bestSolCost;
        return -1d;
    }

    public boolean isSolutionFound() { return isSolutionFound; }

    public boolean isSolutionOptimal() { return isOptimal; }

    /**
     * Method that gives a heuristic.
     * @param currentVertex the current vertex
     * @param unvisited a list of nodes still unvisited
     * @param g the graph with cost
     * @return a estimated value
     */
    protected Double bound(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        Double minCost = Double.POSITIVE_INFINITY;

        for(int i : unvisited)
        {
            minCost = (g.getCost(currentVertex, i) != -1 ? Math.min(minCost, g.getCost(currentVertex, i)) : minCost);
        }

        return minCost * unvisited.size();
    }

    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

    /**
     * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     * @param currentVertex the last visited vertex
     * @param unvisited the set of vertex that have not yet been visited
     * @param visited the sequence of vertices that have been already visited (including currentVertex)
     * @param currentCost the cost of the path corresponding to <code>visited</code>
     */
    private void branchAndBound(int currentVertex, Collection<Integer> unvisited,
                                Collection<Integer> visited, Double currentCost){
        if(DEBUG) System.out.println("currentCost=" + currentCost);
        if (System.currentTimeMillis() - startTime > timeLimit)
        {
            if(DEBUG) System.out.println("TIMELIMIT !");
            return;
        }
        if (unvisited.size() == 0){
            if (g.isArc(currentVertex,0)){ // Si on peut revenir au début
                if (currentCost+g.getCost(currentVertex,0) < bestSolCost){ // si le cout est meilleur que bestSolCost
                    if(DEBUG) System.out.println("A solution has been found !");
                    visited.toArray(bestSol);
                    bestSolCost = currentCost+g.getCost(currentVertex,0);
                    isSolutionFound = true;
                }
            }
        } else if (currentCost+bound(currentVertex,unvisited, g) < bestSolCost){
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()){
                Integer nextVertex = it.next();
                if(DEBUG) System.out.println("nextVertex=" + nextVertex);
                visited.add(nextVertex);
                unvisited.remove(nextVertex);

                // si precedence non respecté juste continue
                if(visited.containsAll(pred.getOutgoingArcs(nextVertex)))
                {
                    if(DEBUG)
                    {
                        System.out.println("Branching !");
                        System.out.println(visited);
                        System.out.println(unvisited);
                    }

                    branchAndBound(nextVertex, unvisited, visited,
                            currentCost+g.getCost(currentVertex, nextVertex));

                    if (System.currentTimeMillis() - startTime > timeLimit)
                    {
                        if(DEBUG) System.out.println("TIMELIMIT !");
                        return;
                    }
                }
                else
                {
                    if(DEBUG)
                    {
                        System.out.println("Cutted because precedence !");
                        System.out.println(visited);
                        System.out.println(unvisited);
                    }
                }

                visited.remove(nextVertex);
                unvisited.add(nextVertex);
            }
            if(currentVertex == 0 && !it.hasNext())
            {
                if(DEBUG) System.out.println("Algorithm is done");
                isOptimal = true;
            }
        }
    }
}
