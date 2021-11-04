package deliverif.model;

import deliverif.utils.Utils;

import java.util.Objects;

public class Address {
    private final long id;
    private final Coord coords;

    public Address(long id, double latitude, double longitude) {
        this.id = id;
        this.coords = new Coord(latitude, longitude);
    }

    public Address(long id, Coord coords) {
        this.id = id;
        this.coords = coords;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return coords.lat();
    }

    public double getLongitude() {
        return coords.lon();
    }

    public Coord getCoords() {
        return coords;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static double dist(Address a1, Address a2) {
        return Utils.dist(a1.getLatitude(), a1.getLongitude(), a2.getLatitude(), a2.getLongitude());

    }

    public double dist(Coord coords) {
        return Utils.dist(this.coords, coords);
    }
}
