package deliverif.model;

import deliverif.utils.Utils;

import java.util.Objects;

public class Address {
    private final long id;
    private final double latitude;
    private final double longitude;

    public Address(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public static double dist(Address a1, Address a2){
        return Utils.dist(a1.getLatitude(),a1.getLongitude(),a2.getLatitude(),a2.getLongitude());

    }

    public double dist(double latitude, double longitude){
        return Utils.dist(this.latitude,this.longitude,latitude,longitude);
    }
}
