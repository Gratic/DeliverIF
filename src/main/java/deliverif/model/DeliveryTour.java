package deliverif.model;

import deliverif.observer.Observable;
import deliverif.xml.RequestsXMLHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
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

    /**
     * List of all requests in tour
     */
    private Collection<Request> requests;

    public DeliveryTour() {
        requests = new ArrayList<>();
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
        this.notifyObservers(this);
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void addAddress(Address addr, EnumAddressType type, Request req) {
        this.pathAddresses.add(addr);
        this.addressRequestMetadata.put(addr, Map.entry(req, type));
        this.notifyObservers(this);
    }

    public void addAddress(Address addr) {
        this.pathAddresses.add(addr);
        this.notifyObservers(this);
    }

    public void loadRequestsFromFile(File requestsFile, CityMap map, DeliveryTour tour) {
        try {
            this.requests.clear();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RequestsXMLHandler handler = new RequestsXMLHandler(tour, map);
            parser.parse(requestsFile, handler);

            this.notifyObservers(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRequest(Request request) {
        requests.add(request);
        this.notifyObservers(this);
    }

    public Collection<Request> getRequests() {
        return requests;
    }
}
