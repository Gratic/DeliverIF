package pdtsp;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.Request;
import deliverif.model.RoadSegment;

import java.util.*;

/**
 * This class allows for the easy calculation of the shortest path in a CityMap
 */
public class ShortestPathWrapper {
    private BasicGraphWrapper g;
    private Integer startingNodeIndex;
    private ShortestPath sP;

    public ShortestPathWrapper(CityMap map) {
        this.g = new BasicGraphWrapper(map);
        this.sP = new Dijkstra();
    }

    public List<Pair<Double, Long>> searchShortestPathFrom(Address departureAddress) {
        this.startingNodeIndex = this.g.idConvert(departureAddress.getId());

        List<Pair<Double, Integer>> resultRaw = this.sP.searchShortestPathFrom(startingNodeIndex, g.getGraph());

        List<Pair<Double, Long>> result = new ArrayList<>(g.getGraph().getNbVertices());

        for (Pair<Double, Integer> p : resultRaw) {
            result.add(new Pair<>(p.getX(), this.g.idConvert(p.getY())));
        }

        return result;


    }
}
