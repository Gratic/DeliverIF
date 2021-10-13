package deliverif.model;

import deliverif.observer.Observable;

import java.util.*;

public class DeliveryTour extends Observable {
    /** Path */
    private List<RoadSegment> path;

    /** Addresses on the path (departure, delivery, pickup) */
    private List<Address> pathAddresses;

    /** Metadata of addresses (basically associated request and address type) */
    private Map<Address, Map.Entry<Request, EnumAddressType>> addressRequestMetadata;

    public DeliveryTour() {
        path = new ArrayList<>();
        pathAddresses = new ArrayList<>();
        addressRequestMetadata = new HashMap<>();
    }

    public List<RoadSegment> getPath() {
        return path;
    }

    public List<Address> getPathAddresses() {
        return pathAddresses;
    }

    public Map<Address, Map.Entry<Request, EnumAddressType>> getAddressRequestMetadata() {
        return addressRequestMetadata;
    }
}
