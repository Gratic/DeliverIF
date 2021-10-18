package deliverif.model;

import deliverif.observer.Observable;

import java.util.*;

public class DeliveryTour extends Observable {
    /**
     * Path
     */
    private List<RoadSegment> path;
    private Date departureTime;

    /**
     * Addresses on the path (departure, delivery, pickup)
     */
    private List<Address> pathAddresses;

    /**
     * Metadata of addresses (basically associated request and address type)
     * Departure
     */
    private Map<Address, Map.Entry<Request, EnumAddressType>> addressRequestMetadata;

    public DeliveryTour() {
        path = new ArrayList<>();
        pathAddresses = new ArrayList<>();
        addressRequestMetadata = new HashMap<>();
        departureTime = new Date();
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

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void addAddress(Address addr, EnumAddressType type, Request req) {
        this.pathAddresses.add(addr);
        this.addressRequestMetadata.put(addr, Map.entry(req, type));
    }

    public void addAddress(Address addr) {
        this.pathAddresses.add(addr);
    }


}
