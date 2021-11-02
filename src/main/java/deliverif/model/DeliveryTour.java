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
     * Departure
     */
    private Map<Address, Map.Entry<Request, EnumAddressType>> addressRequestMetadata;

    /**
     * List of all requests in tour
     */
    private Collection<Request> requests;

    /**
     * Index of the currently selected element:
     * -2 -> none
     * -1 -> departureAddress,
     * >=0 -> index of the request in the arraylist
     */

    private int selectedElement;

    public DeliveryTour() {
        requests = new ArrayList<>();
        path = new ArrayList<>();
        pathAddresses = new ArrayList<>();
        addressRequestMetadata = new HashMap<>();
        departureTime = new Date();
        selectedElement = -2;
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

    public Address getDepartureAddress() {
        if (this.pathAddresses.size() > 0) {
            return this.pathAddresses.get(0);
        }
        return null;
    }

    public boolean isSelected(Request request) {
        if (selectedElement >= 0) {
            return ((ArrayList<Request>) requests).get(selectedElement).equals(request);
        } else {
            return false;
        }
    }

    public boolean isSelected(Address address) {
        return selectedElement == -1;
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

    public void loadRequestsFromFile(File requestsFile, CityMap map, DeliveryTour tour) throws RequestsLoadException {
        try {
            this.requests.clear();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RequestsXMLHandler handler = new RequestsXMLHandler(tour, map);
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

    private Pair<Double, Integer> getClosestRequest(double latitude, double longitude) {

        int closest = 0;
        double minDist = Integer.MAX_VALUE;
        int i = 0;
        for (Request request : requests) {
            if (request.getDeliveryAddress().dist(latitude, longitude) < minDist) {
                closest = i;
                minDist = request.getDeliveryAddress().dist(latitude, longitude);
            }
            if (request.getPickupAddress().dist(latitude, longitude) < minDist) {
                closest = i;
                minDist = request.getPickupAddress().dist(latitude, longitude);
            }
            i++;
        }


        return new Pair<>(minDist, closest);
    }

    private final static double THRESHOLD = 50d;

    private void toggleSelect(int n) {
        selectedElement = selectedElement != n ? n : -2;
    }

    public void selectElement(double latitude, double longitude, double threshold) {
        System.out.println(selectedElement);
        Pair<Double, Integer> res = getClosestRequest(latitude, longitude);
        double distDeparture = Double.MAX_VALUE;
        Address departure = getDepartureAddress();
        if (departure != null) {
            distDeparture = departure.dist(latitude, longitude);
        }

        if (distDeparture < res.getX() && distDeparture < threshold) {
            toggleSelect(-1);
        } else if (res.getX() < threshold) {
            toggleSelect(res.getY());
        }
        System.out.println(selectedElement);
    }

    public void selectElement(double latitude, double longitude) {
        selectElement(latitude, longitude, THRESHOLD);
    }

    public void selectDepartureAddress() {
        toggleSelect(-1);
    }

    public void selectRequest(Request request) {
        int index = ((ArrayList<Request>) requests).indexOf(request);
        if ((index < 0)) {
            System.err.println("Request not present");
        } else {
            toggleSelect(index);
        }
    }
}
