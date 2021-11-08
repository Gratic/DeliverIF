package deliverif.model;

import java.util.Objects;

public class RoadSegment {
    private final Address origin;
    private final Address destination;

    private final String name;
    private final double length;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadSegment segment = (RoadSegment) o;
        return Double.compare(segment.length, length) == 0 && Objects.equals(origin, segment.origin) && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, name, length);
    }
}
