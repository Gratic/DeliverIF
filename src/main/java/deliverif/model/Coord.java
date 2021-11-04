package deliverif.model;

import pdtsp.Pair;

public class Coord extends Pair<Double, Double> {

    public Coord(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public Double lat() {
        return getX();
    }

    public Double lon() {
        return getY();
    }
}