package deliverif.model;

import deliverif.exception.MapLoadingException;
import deliverif.observer.Observable;
import deliverif.xml.MapXMLHandler;
import org.xml.sax.SAXException;
import pdtsp.Pair;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CityMap extends Observable {
    /**
     * All addresses (address id => Address)
     */
    private final Map<Long, Address> addresses;

    /**
     * Neighbors list (address id => Collection of RoadSegment originating from this address)
     */
    private final Map<Long, Collection<RoadSegment>> segments;

    public CityMap() {
        this.addresses = new HashMap<>();
        this.segments = new HashMap<>();
    }

    public void loadMapFromFile(File mapFile) throws MapLoadingException {
        try {
            this.addresses.clear();
            this.segments.clear();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            MapXMLHandler handler = new MapXMLHandler(this);
            parser.parse(mapFile, handler);

            this.notifyObservers(this);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new MapLoadingException(exception);
        }
    }

    public Map<Long, Address> getAddresses() {
        return addresses;
    }


    public Address getAddressById(long id) {
        return this.addresses.get(id);
    }

    public Map<Long, Collection<RoadSegment>> getSegments() {
        return segments;
    }

    public Collection<RoadSegment> getSegmentsOriginatingFrom(long addressId) {
        return this.segments.get(addressId);
    }

    public RoadSegment findSegment(long originAddressId, long destinationAddressId) {
        Collection<RoadSegment> segments = this.segments.get(originAddressId);
        if (segments == null) return null;

        for (RoadSegment segment : this.segments.get(originAddressId)) {
            if (segment.getDestination().getId() == destinationAddressId) {
                return segment;
            }
        }
        return null;
    }

    public void addSegment(RoadSegment segment) {
        long id = segment.getOrigin().getId();
        Collection<RoadSegment> roadSegments = segments.get(id);
        if (roadSegments == null) {
            segments.put(id, new ArrayList<>() {{
                add(segment);
            }});
        } else {
            roadSegments.add(segment);
        }
    }

    public Pair<Double, Address> getClosestAddressFrom(Coord pos) {


        return getClosestAddressesForm(pos, Double.MAX_VALUE).get(0);
    }

    public List<Pair<Double, Address>> getClosestAddressesForm(Coord pos, double maxDist) {
        List<Pair<Double, Address>> res = new ArrayList<>();
        for (Address add : addresses.values()) {
            res.add(new Pair<>(add.dist(pos), add));
        }
        res.sort(Pair::compareTo);
        int index = 0;
        for (Pair<Double, Address> p : res) {
            if (p.getX() > maxDist) {
                break;
            }
            index++;
        }
        return res.subList(0, index);
    }


}
