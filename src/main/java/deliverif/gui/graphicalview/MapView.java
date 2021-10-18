package deliverif.gui.graphicalview;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.RoadSegment;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class MapView extends JPanel implements Observer {
    private CityMap map;

    private double
            latitudeMin = Double.MAX_VALUE,
            latitudeMax = Double.MIN_VALUE,
            longitudeMin = Double.MAX_VALUE,
            longitudeMax = Double.MIN_VALUE;

    private final int DEFAULT_WIDTH = 600, DEFAULT_HEIGHT = 400;

    public MapView(CityMap map) {
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setBackground(new Color(213, 213, 213));

        this.map = map;
        this.recomputeMinMaxLatLong();
        this.map.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.isMapLoaded()) {
            g.setColor(new Color(10, 10, 10));

            for (Address address : this.map.getAddresses().values()) {
                // Point point = this.latlongToXY(address.getLatitude(), address.getLongitude());
                // g.fillOval(point.x - (int) (10 / 2), point.y - (int) (10 / 2), 10, 10);

                Collection<RoadSegment> segments = this.map.getSegmentsOriginatingFrom(address.getId());
                if (segments != null) {
                    for (RoadSegment segment : segments) {
                        Point startCoord = this.latlongToXY(segment.getOrigin().getLatitude(),
                                segment.getOrigin().getLongitude());
                        Point endCoord = this.latlongToXY(segment.getDestination().getLatitude(), segment.getDestination().getLongitude());

                        g.drawLine(startCoord.x, startCoord.y, endCoord.x, endCoord.y);
                    }
                }
            }
        }
        else {
            String message = "La carte n'est pas chargée ou vide.";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, getHeight() / 2, (getWidth() - messageWidth) / 2);
        }
    }

    private Point latlongToXY(double latitude, double longitude) {
        double normalizedLatitude = (latitude - latitudeMin) / (latitudeMax - latitudeMin);
        double normalizedLongitude = (longitude - longitudeMin) / (longitudeMax - longitudeMin);

        int y = 800 - (int)(normalizedLatitude * 800);
        int x = (int)(normalizedLongitude * 800);
        return new Point(x, y);
    }

    public void setMap(CityMap map) {
        this.map = map;
        this.recomputeMinMaxLatLong();
    }

    private void recomputeMinMaxLatLong() {
        if(!this.isMapLoaded()) return;

        for (Address address : map.getAddresses().values()) {
            if (address.getLatitude() > latitudeMax) {
                latitudeMax = address.getLatitude();
            }
            if (address.getLatitude() < latitudeMin) {
                latitudeMin = address.getLatitude();
            }
            if (address.getLongitude() > longitudeMax) {
                longitudeMax = address.getLongitude();
            }
            if (address.getLongitude() < longitudeMin) {
                longitudeMin = address.getLongitude();
            }
        }
    }

    private boolean isMapLoaded() {
        return !(map == null || map.getAddresses().size() == 0);
    }

    @Override
    public void update(Observable observed, Object arg) {
        this.recomputeMinMaxLatLong();
    }
}
