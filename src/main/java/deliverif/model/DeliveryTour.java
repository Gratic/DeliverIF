package deliverif.model;

import deliverif.exception.RequestsLoadException;
import deliverif.observer.Observable;
import deliverif.xml.RequestsXMLHandler;
import org.xml.sax.SAXException;
import pdtsp.Pair;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
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
     */
    private List<Pair<EnumAddressType, Request>> addressRequestMetadata;

    /**
     * List of all requests in tour
     */
    private Collection<Request> requests;

    public DeliveryTour() {
        requests = new ArrayList<>();
        path = new ArrayList<>();
        pathAddresses = new ArrayList<>();
        addressRequestMetadata = new ArrayList<>();
        departureTime = new Date();
    }

    public List<RoadSegment> getPath() {
        return path;
    }

    public List<Address> getPathAddresses() {
        return pathAddresses;
    }

    public List<Pair<EnumAddressType, Request>> getAddressRequestMetadata() {
        return addressRequestMetadata;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
        this.notifyObservers(this);
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Address getDepartureAddress() {
        if(this.pathAddresses.size() > 0) {
            return this.pathAddresses.get(0);
        }
        return null;
    }

    public void addAddress(Address addr, EnumAddressType type, Request req) {
        this.pathAddresses.add(addr);
        this.addressRequestMetadata.add(new Pair<>(type, req));
        this.notifyObservers(this);
    }

    public void addAddress(Address addr) {
        this.pathAddresses.add(addr);
        this.notifyObservers(this);
    }

    public void loadRequestsFromFile(File requestsFile, CityMap map) throws RequestsLoadException {
        try {
            this.requests.clear();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RequestsXMLHandler handler = new RequestsXMLHandler(this, map);
            parser.parse(requestsFile, handler);

            this.notifyObservers(this);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new RequestsLoadException(exception);
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
