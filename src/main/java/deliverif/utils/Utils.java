package deliverif.utils;

import deliverif.model.Coord;

public class Utils {

    public static double dist(double lat1, double long1, double lat2, double long2){
        if(lat1 == lat2 && long1 == long2) {
            return 0d;
        }

        final double EARTH_RADIUS = 6371.0; //km value;

        //converting to radians
        double latPoint1 = Math.toRadians(lat1);
        double lngPoint1 = Math.toRadians(long1);
        double latPoint2 = Math.toRadians(lat2);
        double lngPoint2 = Math.toRadians(long2);

        double distance = Math.pow(Math.sin((latPoint2 - latPoint1) / 2.0), 2)
                + Math.cos(latPoint1) * Math.cos(latPoint2)
                * Math.pow(Math.sin((lngPoint2 - lngPoint1) / 2.0), 2);
        distance = 2.0 * EARTH_RADIUS * Math.asin(Math.sqrt(distance));

        return distance*1000; //m value
    }

    public static double dist(Coord coord1,Coord coord2){
        return dist(coord1.lat(), coord1.lon(), coord2.lat(), coord2.lon());
    }

}
