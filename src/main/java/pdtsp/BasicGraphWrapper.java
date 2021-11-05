package pdtsp;

import deliverif.model.CityMap;
import deliverif.model.RoadSegment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the bridge between the model representation of a map and the algorithmic one
 */
public class BasicGraphWrapper {


    private final Map<Long, Integer> longToInt;
    private final Map<Integer, Long> intToLong;
    private final Graph graph;
    private final CityMap cityMap;
    private final RoadSegment[][] roadSegmentsMatrix;

    public BasicGraphWrapper(CityMap map) {


        this.longToInt = new HashMap<>();
        this.intToLong = new HashMap<>();
        Map<Integer, Collection<Pair<Integer, Double>>> cityMapInteger = new HashMap<>();


        this.cityMap = map;

        // TASK 1: Re-index long indexes into integer indexes because BasicGraph only takes integer.
        int currentIntegerIndex = 0;

        for (Long key : cityMap.getAddresses().keySet()) {
            longToInt.put(key, currentIntegerIndex);
            intToLong.put(currentIntegerIndex, key);
            currentIntegerIndex++;
        }

        this.roadSegmentsMatrix = new RoadSegment[longToInt.size()][longToInt.size()];

        // TASK 2: Building the integer graph
        for (Long longNodeIndex : cityMap.getAddresses().keySet()) {
            int integerOriginNodeIndex = longToInt.get(longNodeIndex);

            // Pair<node ID, distance>
            Collection<Pair<Integer, Double>> arcs = new ArrayList<>();

            Collection<RoadSegment> outgoingRoadSegments = cityMap.getSegments().get(longNodeIndex);

            if (outgoingRoadSegments != null) {
                for (RoadSegment rs : outgoingRoadSegments) {
                    int integerDestinationNodeIndex = longToInt.get(rs.getDestination().getId());
                    double roadSegmentLength = rs.getLength();

                    roadSegmentsMatrix[integerOriginNodeIndex][integerDestinationNodeIndex] = rs;
                    arcs.add(new Pair<>(integerDestinationNodeIndex, roadSegmentLength));
                }
            }


            cityMapInteger.put(integerOriginNodeIndex, arcs);
        }


        this.graph = new BasicGraph(cityMapInteger.size(), cityMapInteger);

    }

    public CityMap getMap() {
        return cityMap;
    }

    public Graph getGraph() {
        return graph;
    }

    public Integer idConvert(long id) {
        return longToInt.get(id);
    }

    public long idConvert(Integer id) {
        return intToLong.get(id);
    }

    public RoadSegment getSegment(int from, int to) {
        return roadSegmentsMatrix[from][to];
    }
}
