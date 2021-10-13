package deliverif.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Request
{
    /** List of all loaded requests */
    private static Collection<Request> requestsList;

    private Address pickupAddress;
    private Address deliveryAddress;

    private int pickupDuration;
    private int deliveryDuration;

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

    public static void loadRequestsFromFile(File requestsFile) {
        // TODO load requests
    }

    public static void addRequest(Request request) {
        requestsList.add(request);
    }

    public static Collection<Request> getRequests() {
        return requestsList;
    }
}
