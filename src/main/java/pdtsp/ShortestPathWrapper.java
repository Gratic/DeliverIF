package pdtsp;

import deliverif.model.Address;
import deliverif.model.CityMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class allows for the easy calculation of the shortest path in a CityMap
 */
public class ShortestPathWrapper {
    private final BasicGraphWrapper g;
    private Integer startingNodeIndex;
    private final ShortestPath sP;

    public ShortestPathWrapper(CityMap map) {
        this.g = new BasicGraphWrapper(map);
        this.sP = new Dijkstra();
    }

    public List<Address> shortestPathBetween(Address origin, Address destination) {
        this.startingNodeIndex = this.g.idConvert(origin.getId());

        List<Pair<Double, Integer>> resultRaw = this.sP.searchShortestPathFrom(startingNodeIndex, g.getGraph());

        List<Address> res = new ArrayList<>();
        res.add(destination);
        int currId = g.idConvert(destination.getId());
        while (currId != g.idConvert(origin.getId())) {
            currId = resultRaw.get(currId).getY();
            res.add(g.getMap().getAddressById(g.idConvert(currId)));
        }
        Collections.reverse(res);
        return res;


    }
}
