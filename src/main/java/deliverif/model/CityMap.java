package deliverif.model;

import java.io.File;
import java.util.*;

public class CityMap {
    /** All addresses (address id => Address) */
    private Map<Long, Address> addresses;

    /** Neighbors list (address id => Collection of RoadSegment originating from this address) */
    private Map<Long, Collection<RoadSegment>> segments;

    public CityMap() {
        this.addresses = new HashMap<>();
        this.segments = new HashMap<>();
    }

    public void loadMapFromFile(File mapFile) {
        // TODO map loading
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
}
