package pdtsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Branch'n'Bound algorithm implementation for PDTSP.
 *
 * @deprecated takes too long.
 */
public class BnBPDTSP implements RunnablePDTSP {
    private static final boolean DEBUG = false;

    private volatile boolean threadReady = false;
    private volatile boolean runningInThread = false;
    private volatile boolean running = false;
    private volatile boolean paused = true;

    private volatile boolean isSolutionFound = false;
    private volatile boolean isOptimal = false;

    private volatile Integer[] bestSol;
    private volatile Double bestSolCost;

    protected Graph g;
    protected Graph pred;
    private int timeLimit;
    private volatile long startTime;

    public BnBPDTSP(int timeLimit, Graph g, Graph pred) {
        this.timeLimit = timeLimit;
        this.g = g;
        this.pred = pred;
    }

    @Override
    public void searchSolution(int timeLimit, Graph g, Graph pred) {
        if (timeLimit <= 0) return;

        this.timeLimit = timeLimit;

        this.g = g;
        this.pred = pred;
        bestSol = new Integer[g.getNbVertices()];
        Collection<Integer> unvisited = new ArrayList<>(g.getNbVertices() - 1);
        for (int i = 1; i < g.getNbVertices(); i++) unvisited.add(i);
        Collection<Integer> visited = new ArrayList<>(g.getNbVertices());
        visited.add(0);
        bestSolCost = Double.POSITIVE_INFINITY;

        startTime = System.currentTimeMillis();
        branchAndBound(0, unvisited, visited, 0d);

        isOptimal = true;
    }

    @Override
    public Integer getSolution(int i) {
        if (g != null && i >= 0 && i < g.getNbVertices())
            return bestSol[i];
        return -1;
    }

    @Override
    public Double getSolutionCost() {
        if (g != null)
            return bestSolCost;
        return -1d;
    }

    public boolean isSolutionFound() {
        return isSolutionFound;
    }

    public boolean isSolutionOptimal() {
        return isOptimal;
    }

    /**
     * Method that gives a heuristic.
     *
     * @param currentVertex the current vertex
     * @param unvisited     a list of nodes still unvisited
     * @param g             the graph with cost
     * @return a estimated value
     */
    protected Double bound(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        double minCost = Double.POSITIVE_INFINITY;

        for (int i : unvisited) {
            minCost = (g.getCost(currentVertex, i) != -1 ? Math.min(minCost, g.getCost(currentVertex, i)) : minCost);
            for (int j : unvisited) {
                if (i == j) continue;
                minCost = (g.getCost(i, j) != -1 ? Math.min(minCost, g.getCost(i, j)) : minCost);
            }
            minCost = (g.getCost(i, currentVertex) != -1 ? Math.min(minCost, g.getCost(i, currentVertex)) : minCost);
        }

        return minCost * unvisited.size();
    }

    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

    /**
     * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     *
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @param visited       the sequence of vertices that have been already visited (including currentVertex)
     * @param currentCost   the cost of the path corresponding to <code>visited</code>
     */
    private void branchAndBound(int currentVertex, Collection<Integer> unvisited,
                                Collection<Integer> visited, Double currentCost) {
        if (runningInThread && !running) return;

        if (DEBUG) System.out.println("currentCost=" + currentCost);

        long timeElapsed = System.currentTimeMillis() - startTime;

        if (timeElapsed > timeLimit) {
            if (DEBUG) System.out.println("TIMELIMIT !");

            if (runningInThread)
                pause();
            else
                return;
        }

        while (running && paused) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (unvisited.size() == 0) {
            if (g.isArc(currentVertex, 0)) { // Si on peut revenir au début
                if (currentCost + g.getCost(currentVertex, 0) < bestSolCost) { // si le cout est meilleur que bestSolCost
                    if (DEBUG) System.out.println("A solution has been found !");

                    visited.toArray(bestSol);
                    bestSolCost = currentCost + g.getCost(currentVertex, 0);
                    isSolutionFound = true;
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited, g) < bestSolCost) {
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()) {
                Integer nextVertex = it.next();
                if (DEBUG) System.out.println("nextVertex=" + nextVertex);
                visited.add(nextVertex);
                unvisited.remove(nextVertex);

                // si precedence non respecté juste continue
                if (visited.containsAll(pred.getOutgoingArcs(nextVertex))) {
                    if (DEBUG) {
                        System.out.println("Branching !");
                        System.out.println(visited);
                        System.out.println(unvisited);
                    }

                    branchAndBound(nextVertex, unvisited, visited,
                            currentCost + g.getCost(currentVertex, nextVertex));

                } else {
                    if (DEBUG) {
                        System.out.println("Cutted because precedence !");
                        System.out.println(visited);
                        System.out.println(unvisited);
                    }
                }

                visited.remove(nextVertex);
                unvisited.add(nextVertex);
            }
        }
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        startTime = System.currentTimeMillis();
        paused = false;
    }

    @Override
    public void kill() {
        running = false;
    }

    @Override
    public boolean isReady() {
        return threadReady;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void run() {
        runningInThread = true;
        running = true;
        paused = true;
        isOptimal = false;

        threadReady = true;

        searchSolution(timeLimit, g, pred);

        running = false;
        paused = true;
    }
}
