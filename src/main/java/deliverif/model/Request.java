package deliverif.model;

import deliverif.xml.RequestsXMLHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Request {
    /**
     * List of all loaded requests
     */
    private static Collection<Request> requestsList = new ArrayList<>();

    private final Address pickupAddress;
    private final Address deliveryAddress;

    private final int pickupDuration;
    private final int deliveryDuration;

    public Request(Address pickupAddress, Address deliveryAddress, int pickupDuration, int deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public static void loadRequestsFromFile(File requestsFile, CityMap map, DeliveryTour tour) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RequestsXMLHandler handler = new RequestsXMLHandler(tour, map);
            parser.parse(requestsFile, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRequest(Request request) {
        requestsList.add(request);
    }

    public static Collection<Request> getRequests() {
        return requestsList;
    }

    public static void resetRequests() {
        requestsList = new ArrayList<>();
    }
}
