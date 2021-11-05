package deliverif.model;

public class Request {
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
}
