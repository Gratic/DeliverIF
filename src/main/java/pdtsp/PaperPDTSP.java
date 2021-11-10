package pdtsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of "A heuristic for the pickup and delivery traveling salesman problem" paper algorithm.
 * Authors are Jacques Renaud, Fayez F. Boctor, Jamal Ouenniche.
 * https://doi.org/10.1016/S0305-0548(99)00066-0
 * <p>
 * This implementation doesn't fully respect the described algorithm. Nonetheless, tries to stick as close as possible.
 * There isn't enough coverage on internet about the 4-Opt algorithm, so this part was abandoned.
 * <p>
 * This implementation implements RunnablePDTSP. This means that this solution can also function in a thread allowing
 * multi-threading. Though, it can also work in the same thread as the one it is called.
 * <p>
 * Important parameters for the result found in the end are :
 * r which represents the number of predecessors and successors for 3-opt optimization in the first part of the algorithm.
 * alpha must be valued between [0,2].
 * withOpt is a boolean to activate or deactivate the 3 and 2-opt.
 * <p>
 * Only when ran in a thread the algorithm modify itself its parameters (r, alpha, withOpt) to find the best solution possible.
 */
public class PaperPDTSP implements RunnablePDTSP {
    private int r = 6;
    private double alpha = 1.5;
    private boolean withOpt = true;

    private volatile List<Integer> bestpath;
    private volatile Double bestcost;
    private Map<Integer, Integer> ptoD;
    private Map<Integer, Integer> dtoP;

    private Graph g;
    private Graph p;

    private volatile boolean isSolutionFound = false;
    private volatile boolean isOptimal = false;
    private volatile boolean isPaused = true;
    private volatile boolean isRunning = false;
    private volatile boolean isReady = false;
    private boolean isRunningInThread = false;

    private long startingTime;
    private int timeLimit;

    public PaperPDTSP(int timeLimit, Graph g, Graph pred) {
        this.timeLimit = timeLimit;
        this.g = g;
        this.p = pred;
    }

    @Override
    public void searchSolution(int timeLimit, Graph g, Graph p) {
        if (timeLimit <= 0) return;

        this.g = g;
        this.p = p;
        this.timeLimit = timeLimit;

        if (!isRunningInThread) {
            bestcost = Double.POSITIVE_INFINITY;
            bestpath = new ArrayList<>();
        }

        ptoD = new HashMap<>();
        dtoP = new HashMap<>();

        double maxInitCost = 0;
        int maxP = -1;
        int maxD = -1;
        for (int i = 0; i < p.getNbVertices(); i++) {
            if (p.getOutgoingArcs(i).size() == 1) {
                List<Integer> outgoingArcs = new ArrayList<>(p.getOutgoingArcs(i));
                int nodeP = outgoingArcs.get(0);

                ptoD.put(nodeP, i);
                dtoP.put(i, nodeP);

                double initCost = g.getCost(0, nodeP) + g.getCost(nodeP, i) + g.getCost(i, 0);

                if (initCost > maxInitCost) {
                    maxInitCost = initCost;
                    maxP = nodeP;
                    maxD = i;
                }
            }
        }

        List<Integer> H = new ArrayList<>();
        H.add(0);
        H.add(maxP);
        H.add(maxD);
        H.add(0);

        List<Integer> P = new ArrayList<>(ptoD.keySet());
        P.remove((Integer) maxP);

        this.startingTime = System.currentTimeMillis();

        long timeElapsed = System.currentTimeMillis() - startingTime;
        if (timeElapsed > timeLimit) {
            if (isRunningInThread)
                pause();
            else
                return;
        }
        waitResume();

        H = doubleInsertion(P, H);

        timeElapsed = System.currentTimeMillis() - startingTime;

        if (timeElapsed > timeLimit) {
            if (isRunningInThread)
                pause();
            else
                return;
        }
        waitResume();

        deleteAndReinsert(H);

        isOptimal = true;
    }

    /**
     * Utility function to pause the thread when the thread is paused and still running.
     */
    private void waitResume() {
        while (isRunning && isPaused) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The new heuristic proposed in the paper.
     * Adapted to work in a pause-able environment.
     *
     * @param P List of Pickup points yet to be inserted.
     * @param H Hamiltonian Cycle
     * @return the updated Hamiltonian Cycle.
     */
    private List<Integer> doubleInsertion(List<Integer> P, List<Integer> H) {
        if (isRunningInThread && !isRunning) return null;

        long timeElapsed = System.currentTimeMillis() - startingTime;
        if (timeElapsed > timeLimit) {
            if (isRunningInThread)
                pause();
            else
                return null;
        }
        waitResume();

        // step 1 : find best place to insert
        if (P.size() == 0) return H;

        double deltaI = Double.POSITIVE_INFINITY;
        int bestKpos = -1, bestSpos = -1;
        int bestNodeP = -1, bestNodeD = -1;

        for (int nodeP : P) {
            int nodeD = ptoD.get(nodeP);
            double pdcost = g.getCost(nodeP, nodeD);

            for (int o = 0; o < H.size() - 1; o++) {
                int k = H.get(o);
                int l = H.get(o + 1);
                int s;
                int t;

                double klcost = g.getCost(k, l);

                double a = alpha * g.getCost(k, nodeP) + pdcost + (2 - alpha) * g.getCost(nodeD, l) - klcost;

                for (int o2 = o + 2; o2 < H.size() - 1; o2++) {
                    s = H.get(o2);
                    t = H.get(o2 + 1);

                    double b = alpha * (g.getCost(k, nodeP) + g.getCost(nodeP, l) - klcost) + (2 - alpha) * (g.getCost(s, nodeD) + g.getCost(nodeD, t) - g.getCost(s, t));
                    if (b < a) {
                        a = b;
                    }

                    if (a < deltaI) {
                        deltaI = a;
                        bestKpos = o;
                        bestSpos = o2;
                        bestNodeP = nodeP;
                        bestNodeD = nodeD;
                    }
                }

                if (a < deltaI) {
                    deltaI = a;
                    bestKpos = o;
                    bestNodeP = nodeP;
                    bestNodeD = nodeD;
                }
            }
        }

        // step 2 : insertion
        if (bestSpos != -1) {
            H.add(bestKpos + 1, bestNodeP);

            // on veut l'ajouter juste après bestNodeS donc +1 et on a déjà rajouté un élément donc +2
            H.add(bestSpos + 2, bestNodeD);
        } else {
            H.add(bestKpos + 1, bestNodeP);
            H.add(bestKpos + 2, bestNodeD);
        }

        P.remove((Integer) bestNodeP);

        // step 3 : 3-opt https://fr.wikipedia.org/wiki/2-opt
        // + take in consideration precedence constraints
        if (bestSpos != -1) {
            threeOpt(H, bestKpos + 1 - r, bestKpos + 1 + r + 1);
            threeOpt(H, bestSpos + 2 - r, bestSpos + 2 + r + 1);
        } else {
            threeOpt(H, bestKpos + 1 - r, bestKpos + 1 + r + 1);
        }

        H = doubleInsertion(P, H);
        return deleteAndReinsert(H);
    }

    /**
     * The second heuristic proposed by the paper.
     *
     * @param H the hamiltonian cycle
     * @return the new hamiltonian cycle
     */
    private List<Integer> deleteAndReinsert(List<Integer> H) {
        if (isRunningInThread && !isRunning) return null;

        long timeElapsed = System.currentTimeMillis() - startingTime;
        if (timeElapsed > timeLimit) {
            if (isRunningInThread)
                pause();
            else
                return null;
        }
        waitResume();

        boolean upgrade = true;
        while (upgrade) {
            timeElapsed = System.currentTimeMillis() - startingTime;
            if (timeElapsed > timeLimit) {
                if (isRunningInThread)
                    pause();
                else
                    return null;
            }
            waitResume();

            double bestCost = getSolutionCost(H);
            if (bestCost < bestcost) {
                this.bestcost = bestCost;
                this.bestpath = new ArrayList<>(H);
            }

            isSolutionFound = true;

            if (this.withOpt) {
                H = twoOpt(H, 0, H.size());
                H = threeOpt(H, 0, H.size());
            }

            for (int nodeP : ptoD.keySet()) {
                double deltaI = Double.POSITIVE_INFINITY;
                int bestKpos = -1, bestSpos = -1;
                int bestNodeP = -1, bestNodeD = -1;

                int nodeD = ptoD.get(nodeP);
                double pdcost = g.getCost(nodeP, nodeD);

                H.remove((Integer) nodeP);
                H.remove((Integer) nodeD);

                for (int o = 0; o < H.size() - 1; o++) {
                    int k = H.get(o);
                    int l = H.get(o + 1);
                    int s;
                    int t;

                    double klcost = g.getCost(k, l);
                    if (k == 0 && l == 0) klcost = 0;

                    double a = alpha * g.getCost(k, nodeP) + pdcost + (2 - alpha) * g.getCost(nodeD, l) - klcost;

                    for (int o2 = o + 2; o2 < H.size() - 1; o2++) {

                        timeElapsed = System.currentTimeMillis() - startingTime;
                        if (timeElapsed > timeLimit) {
                            if (isRunningInThread)
                                pause();
                            else
                                return null;
                        }
                        waitResume();

                        s = H.get(o2);
                        t = H.get(o2 + 1);

                        double b = alpha * (g.getCost(k, nodeP) + g.getCost(nodeP, l) - klcost) + (2 - alpha) * (g.getCost(s, nodeD) + g.getCost(nodeD, t) - g.getCost(s, t));
                        if (b < a) {
                            a = b;
                        }

                        if (a < deltaI) {
                            deltaI = a;
                            bestKpos = o;
                            bestSpos = o2;
                            bestNodeP = nodeP;
                            bestNodeD = nodeD;
                        }
                    }

                    if (a < deltaI) {
                        deltaI = a;
                        bestKpos = o;
                        bestNodeP = nodeP;
                        bestNodeD = nodeD;
                    }
                }

                if (bestSpos != -1) {
                    H.add(bestKpos + 1, bestNodeP);

                    // on veut l'ajouter juste après bestNodeS donc +1 et on a déjà rajouté un élément donc +2
                    H.add(bestSpos + 2, bestNodeD);
                } else {
                    H.add(bestKpos + 1, bestNodeP);
                    H.add(bestKpos + 2, bestNodeD);
                }
            }


            double cost = getSolutionCost(H);
            upgrade = (cost < bestCost);
        }

        double bestCost = getSolutionCost(H);
        if (bestCost < bestcost) {
            this.bestcost = bestCost;
            this.bestpath = new ArrayList<>(H);
        }

        return H;
    }

    /**
     * The 2-opt algorithm.
     * Tries to find new movements that makes a better solution until it can't, in that case it stops and return the new hamiltonian cycle.
     * <p>
     * {@see HamiltonianCycleUtils.twoOptMove}
     *
     * @param H     the hamiltonian cycle.
     * @param start starting index
     * @param end   ending index (not included)
     * @return the new hamiltonian cycle (can be untouched)
     */
    private List<Integer> twoOpt(List<Integer> H, int start, int end) {
        if (start < 0) start = 0;
        if (end > H.size()) end = H.size();

        boolean upgrade = true;
        while (upgrade) {
            upgrade = false;
            for (int i = start + 1; i < end - 1; i++) {
                int nodeI = H.get(i - 1);
                int nodeIPlus = H.get(i);

                for (int j = i + 1; j < end; j++) {
                    long timeElapsed = System.currentTimeMillis() - startingTime;
                    if (timeElapsed > timeLimit) {
                        if (isRunningInThread)
                            pause();
                        else
                            return null;
                    }
                    waitResume();

                    int nodeJ = H.get(j - 1);
                    int nodeJPlus = H.get(j);

                    if (g.getCost(nodeI, nodeIPlus) + g.getCost(nodeJ, nodeJPlus) > g.getCost(nodeI, nodeJ) + g.getCost(nodeIPlus, nodeJPlus)) {
                        // found a local optimisation
                        List<Integer> temp = new ArrayList<>(H);

                        temp = HamiltonianCycleUtils.twoOptMove(temp, i, j);

                        if (isValid(temp)) {
                            H = temp;
                        }
                    }
                }
            }
        }

        return H;
    }

    /**
     * The 3-opt algorithm.
     * Tries to find new movements that makes a better solution until it can't, in that case it stops and return the new hamiltonian cycle.
     * <p>
     * {@see HamiltonianCycleUtils.threeOptMove}
     *
     * @param H     the hamiltonian cycle.
     * @param start starting index
     * @param end   ending index (not included)
     * @return the new hamiltonian cycle (can be untouched)
     */
    private List<Integer> threeOpt(List<Integer> H, int start, int end) {
        if (start < 0) start = 0;
        if (end > H.size()) end = H.size();

        boolean upgrade = true;
        while (upgrade) {
            upgrade = false;
            for (int i = start + 1; i < end - 2; i++) {
                int nodeI = H.get(i - 1);
                int nodeIPlus = H.get(i);

                for (int j = i + 1; j < end - 1; j++) {
                    int nodeJ = H.get(j - 1);
                    int nodeJPlus = H.get(j);

                    for (int k = j + 1; k < end; k++) {
                        long timeElapsed = System.currentTimeMillis() - startingTime;
                        if (timeElapsed > timeLimit) {
                            if (isRunningInThread)
                                pause();
                            else
                                return null;
                        }
                        waitResume();

                        int nodeK = H.get(k - 1);
                        int nodeKPlus = H.get(k);

                        double baseDistance = g.getCost(nodeI, nodeIPlus) + g.getCost(nodeJ, nodeJPlus) + g.getCost(nodeK, nodeKPlus);
                        double d0 = g.getCost(nodeI, nodeJPlus) + g.getCost(nodeK, nodeIPlus) + g.getCost(nodeJ, nodeKPlus);
                        double d1 = g.getCost(nodeI, nodeK) + g.getCost(nodeJPlus, nodeIPlus) + g.getCost(nodeJ, nodeKPlus);
                        double d2 = g.getCost(nodeI, nodeJPlus) + g.getCost(nodeK, nodeJ) + g.getCost(nodeI, nodeKPlus);
                        double d3 = g.getCost(nodeI, nodeJ) + g.getCost(nodeIPlus, nodeK) + g.getCost(nodeJPlus, nodeKPlus);
                        double d4 = g.getCost(nodeI, nodeIPlus) + g.getCost(nodeJ, nodeK) + g.getCost(nodeJPlus, nodeKPlus);
                        double d5 = g.getCost(nodeI, nodeJ) + g.getCost(nodeIPlus, nodeJPlus) + g.getCost(nodeK, nodeKPlus);
                        double d6 = g.getCost(nodeI, nodeJPlus) + g.getCost(nodeK, nodeIPlus) + g.getCost(nodeJ, nodeKPlus);

                        boolean change = false;
                        List<Integer> temp = new ArrayList<>(H);
                        if (d0 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 0);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d1 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 1);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d2 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 2);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d3 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 3);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d4 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 4);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d5 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 5);
                            if (isValid(temp)) {
                                H = temp;
                                change = true;
                            }
                        }
                        if (!change && d6 < baseDistance) {
                            temp = HamiltonianCycleUtils.threeOptMove(temp, i, j, k, 6);
                            if (isValid(temp)) {
                                H = temp;
                            }
                        }
                    }
                }
            }
        }

        return H;
    }

    /**
     * Utility function to verify that the Hamiltonian cycle obeys the precedence in request.
     * Pickups must be picked up before their deliveries.
     *
     * @param temp the hamiltonian cycle to test
     * @return true if valid, else false.
     */
    private boolean isValid(List<Integer> temp) {
        Map<Integer, Boolean> pickedUp = new HashMap<>();

        if (temp.size() != g.getNbVertices() + 1) {
            return false;
        }

        for (int pickup : ptoD.keySet()) {
            pickedUp.put(pickup, false);
        }

        for (int k = 1; k < temp.size(); k++) {
            int nodeK = temp.get(k);
            boolean isDelivery = dtoP.containsKey(nodeK);
            boolean isPickup = ptoD.containsKey(nodeK);

            if (isPickup) {
                pickedUp.put(nodeK, true);
            }

            if (isDelivery) {
                int associatedPickup = dtoP.get(nodeK);
                if (!pickedUp.get(associatedPickup)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Utility function to verify the cost of a hamiltonian cycle.
     *
     * @param H the hamiltonian cycle.
     * @return the cost of H.
     */
    private double getSolutionCost(List<Integer> H) {
        double cost = 0;

        for (int i = 0; i < H.size() - 1; i++) {
            cost += g.getCost(H.get(i), H.get(i + 1));
        }

        return cost;
    }

    @Override
    public Integer getSolution(int i) {
        if (g != null && i >= 0 && i < g.getNbVertices())
            return bestpath.get(i);
        return -1;
    }

    @Override
    public Double getSolutionCost() {
        if (bestpath != null && g != null && bestpath.size() > 2) {
            double cost = 0;

            for (int i = 0; i < bestpath.size() - 1; i++) {
                cost += g.getCost(bestpath.get(i), bestpath.get(i + 1));
            }

            return cost;
        } else {
            return -1d;
        }
    }

    @Override
    public boolean isSolutionFound() {
        return isSolutionFound;
    }

    @Override
    public boolean isSolutionOptimal() {
        return isOptimal;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        startingTime = System.currentTimeMillis();
        isPaused = false;
    }

    @Override
    public void kill() {
        isRunning = false;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void run() {
        isRunningInThread = true;
        isRunning = true;
        isPaused = true;
        isOptimal = false;
        isSolutionFound = false;

        bestcost = Double.POSITIVE_INFINITY;
        bestpath = new ArrayList<>();

        isReady = true;

        long timeElapsed = System.currentTimeMillis() - startingTime;
        if (timeElapsed > timeLimit) {
            pause();
        }
        waitResume();

        this.withOpt = false;
        for (int j = 1; j < 20; j++) {
            this.r = j;
            for (double i = 0.1; i < 2; i += 0.1) {
                this.alpha = i;
                searchSolution(timeLimit, g, p);

                timeElapsed = System.currentTimeMillis() - startingTime;
                if (timeElapsed > timeLimit) {
                    pause();
                }
                waitResume();
            }
        }
        this.withOpt = true;
        for (int j = 1; j < 20; j++) {
            this.r = j;
            for (double i = 0.1; i < 2; i += 0.1) {
                this.alpha = i;
                searchSolution(timeLimit, g, p);

                timeElapsed = System.currentTimeMillis() - startingTime;
                if (timeElapsed > timeLimit) {
                    pause();
                }
                waitResume();
            }
        }

        isRunning = false;
        isPaused = true;
    }
}
