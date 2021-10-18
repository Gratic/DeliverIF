package deliverif.model;

public class RoadSegment {
    private Address origin;
    private Address destination;

    private String name;
    private double length;

    public RoadSegment(Address origin, Address destination, String name, double length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

    public Address getOrigin() {
        return origin;
    }

    public Address getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }
}
