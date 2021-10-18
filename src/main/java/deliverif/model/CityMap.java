package deliverif.model;

import deliverif.xml.MapXMLHandler;
import deliverif.xml.RequestsXMLHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CityMap {
    /**
     * All addresses (address id => Address)
     */
    private Map<Long, Address> addresses;

    /**
     * Neighbors list (address id => Collection of RoadSegment originating from this address)
     */
    private Map<Long, Collection<RoadSegment>> segments;

    public CityMap() {
        this.addresses = new HashMap<>();
        this.segments = new HashMap<>();
    }

    public void loadMapFromFile(File mapFile) throws SAXException, ParserConfigurationException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        this.addresses = new HashMap<>();
        this.segments = new HashMap<>();
        MapXMLHandler handler = new MapXMLHandler(this);
        parser.parse(mapFile, handler);

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

    public void addSegment(RoadSegment segment) {
        long id = segment.getOrigin().getId();
        Collection<RoadSegment> roadSegments = segments.get(id);
        if (roadSegments == null) {
            segments.put(id, new ArrayList<RoadSegment>() {{
                add(segment);
            }});
        } else {
            roadSegments.add(segment);
        }
    }
}
