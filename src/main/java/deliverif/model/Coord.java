package deliverif.model;

import deliverif.utils.Utils;
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

    public static double getCosinusAngle(Coord p1, Coord p2, Coord p3){
        double a = Utils.dist(p3, p2);
        double b = Utils.dist(p1, p2);
        double c = Utils.dist(p3, p1);

        return Math.acos(-(c*c - b*b - a*a)/(2*b*a));
    }

    public static double getVectorialProduct(Coord p1, Coord p2, Coord p3){
        Coord v1 = new Coord((p2.lon()- p1.lon()), p2.lat()- p1.lat());
        Coord v2 = new Coord((p2.lon()- p3.lon()), p2.lat()- p3.lat());

        return v1.lat()* v2.lon() - v2.lat()* v1.lon();

    }

    public static double getAngle(Coord p1, Coord p2, Coord p3){
        return getCosinusAngle(p1, p2, p3)*getVectorialProduct(p1, p2, p3)/Math.abs(getVectorialProduct(p1, p2, p3));
    }
}