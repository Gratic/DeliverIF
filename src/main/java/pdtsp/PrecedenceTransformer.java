package pdtsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PrecedenceTransformer implements GraphTransformer {
    private final Integer nbVertices;
    private final Collection<Pair<Integer, Integer>> requests;

    private final Map<Integer, Collection<Pair<Integer, Double>>> precedenceWorld;
    private Graph precendenceGraph;

    public PrecedenceTransformer(Integer nbVertices, Collection<Pair<Integer, Integer>> requests) {
        this.nbVertices = nbVertices;
        this.requests = requests;

        precedenceWorld = new HashMap<>();
    }

    /**
     * This transformation is simple, we take a graph and a list of request and we make an arc
     * between two nodes if there is a request that need both.
     * The arc is oriented : from delivery to pickup.
     * <p>
     * This is required for the PDTSP algorithm.
     * Because we don't want a path that deliver packages that are not yet picked up.
     */
    public void transform() {
        for (int i = 0; i < nbVertices; i++) {
            Collection<Pair<Integer, Double>> temp = new ArrayList<>();
            precedenceWorld.put(i, temp);
        }

        for (Pair<Integer, Integer> distortedRequest : requests) {
            int distortedPickup = distortedRequest.getX();
            int distortedDelivery = distortedRequest.getY();

            Pair<Integer, Double> precedenceArc = new Pair<>();
            precedenceArc.setX(distortedPickup);

            // The distance is set to 1d but in reality it DOESN'T matter. Only getX matter.
            precedenceArc.setY(1d);

            precedenceWorld.get(distortedDelivery).add(precedenceArc);
        }

        precendenceGraph = new BasicGraph(precedenceWorld.size(), precedenceWorld);
    }

    @Override
    public Graph getTransformedGraph() {
        return precendenceGraph;
    }
}
