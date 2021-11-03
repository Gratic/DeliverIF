package deliverif.gui.mapview;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.*;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;

import static deliverif.gui.utils.Assets.*;
import static deliverif.gui.utils.Utils.dye;

public class MapView extends JPanel implements Observer, MouseInputListener, MouseWheelListener {
    private CityMap map;
    private DeliveryTour tour;

    private double
            latitudeMin = Double.MAX_VALUE,
            latitudeMax = Double.MIN_VALUE,
            longitudeMin = Double.MAX_VALUE,
            longitudeMax = Double.MIN_VALUE;

    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 400;
    private final int MAP_BASE_SIZE = 900;

    private final double ICON_SIZE = 35;

    private final double MAX_ZOOM_LEVEL = 6.0;
    private final double MIN_ZOOM_LEVEL = .1;
    private final double BASE_ZOOM_LEVEL = 1.0;
    private final double ZOOM_SENSITIVITY = 0.2;
    private double zoomLevel = BASE_ZOOM_LEVEL;

    private int xTranslation = 0, yTranslation = 0;

    public MapView(CityMap map, DeliveryTour tour) {
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setBackground(ColorTheme.LIGHT_BASE_GREY_ALT);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        this.map = map;
        this.map.addObserver(this);
        this.recomputeMinMaxLatLong();

        this.tour = tour;
        this.tour.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        if (this.isMapLoaded()) {
            g.setColor(new Color(10, 10, 10));

            for (Address address : this.map.getAddresses().values()) {
                if (address.getId() == 190866513) {
                    Point point = this.latlongToXY(address.getCoords());
                    g.setColor(new Color(200, 0, 0));
                    g.drawChars("Home".toCharArray(), 0, 4, point.x, point.y);
                    g.setColor(new Color(10, 10, 10));
                }

                Collection<RoadSegment> segments = this.map.getSegmentsOriginatingFrom(address.getId());
                if (segments != null) {
                    for (RoadSegment segment : segments) {
                        Point startCoord = this.latlongToXY(segment.getOrigin().getCoords());
                        Point endCoord = this.latlongToXY(segment.getDestination().getCoords());

                        g.drawLine(startCoord.x, startCoord.y, endCoord.x, endCoord.y);
                    }
                }
            }
        } else {
            String message = "La carte n'est pas chargée ou vide.";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, getHeight() / 2, (getWidth() - messageWidth) / 2);
        }

        if (this.isTourLoaded()) {
            int i = 0;
            int iconsWidth = (int) (ICON_SIZE * this.zoomLevel);
            int iconsHeight = (int) (ICON_SIZE * this.zoomLevel);

            for (Request request : this.tour.getRequests()) {
                if (tour.isSelected(request)) {
                    iconsHeight *= 2;
                    iconsWidth *= 2;
                }
                Point pickupPoint = this.latlongToXY(request.getPickupAddress().getCoords());
                Point deliveryPoint = this.latlongToXY(request.getDeliveryAddress().getCoords());


                g.drawImage(dye(pickupImage, ColorTheme.REQUEST_PALETTE.get(i % ColorTheme.REQUEST_PALETTE.size())),
                        pickupPoint.x - (iconsWidth / 2),
                        pickupPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                g.drawImage(dye(deliveryImage, ColorTheme.REQUEST_PALETTE.get(i % ColorTheme.REQUEST_PALETTE.size())),
                        deliveryPoint.x - (iconsWidth / 2),
                        deliveryPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                if (tour.isSelected(request)) {
                    iconsHeight /= 2;
                    iconsWidth /= 2;
                }
                i++;
            }

            Address departureAddress = this.tour.getDepartureAddress();
            if (departureAddress != null) {
                Point point = this.latlongToXY(departureAddress.getCoords());
                if (tour.isSelected(departureAddress)) {
                    iconsHeight *= 2;
                    iconsWidth *= 2;
                }


                g.drawImage(dye(departureImage, ColorTheme.DEPARTURE_COLOR),
                        point.x - (iconsWidth / 2),
                        point.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this
                );


            }
        }
    }


    private Point latlongToXY(Coord coord) {
        Coord normalizedCoord = normalizeCoord(coord);

        int y = MAP_BASE_SIZE - (int) (normalizedCoord.lat() * MAP_BASE_SIZE * zoomLevel) + yTranslation;
        int x = (int) (normalizedCoord.lon() * MAP_BASE_SIZE * zoomLevel) + xTranslation;
        return new Point(x, y);
    }

    private Coord XYToLatLong(Point p) {
        double normalizedX = (p.x - xTranslation) / (MAP_BASE_SIZE * zoomLevel);
        double normalizedY = (MAP_BASE_SIZE - (p.y - yTranslation)) / (MAP_BASE_SIZE * zoomLevel);

        double latitude = normalizedY * (latitudeMax - latitudeMin) + latitudeMin;
        double longitude = normalizedX * (longitudeMax - longitudeMin) + longitudeMin;

        return new Coord(latitude, longitude);
    }

    private Coord normalizeCoord(Coord coord) {
        return new Coord(
                (coord.lat() - latitudeMin) / (latitudeMax - latitudeMin),
                (coord.lon() - longitudeMin) / (longitudeMax - longitudeMin)
        );
    }

    public void setMap(CityMap map) {
        this.map = map;
        this.recomputeMinMaxLatLong();
    }

    private void recomputeMinMaxLatLong() {
        if (!this.isMapLoaded()) return;

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
        return map != null && map.getAddresses().size() > 0;
    }

    private boolean isTourLoaded() {
        return tour != null && tour.getRequests().size() > 0;
    }

    @Override
    public void update(Observable observed, Object arg) {
        if (observed == this.map) {
            this.recomputeMinMaxLatLong();
            this.repaint();
        } else if (observed == this.tour) {
            this.repaint();
        }
    }

    private Point lastPointDragged = null;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.lastPointDragged != null) {
            int xMove = e.getX() - this.lastPointDragged.x;
            int yMove = e.getY() - this.lastPointDragged.y;

            this.translateView(xMove, yMove);
        }

        this.lastPointDragged = e.getPoint();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        this.lastPointDragged = null;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomView(e.getPoint(),e.getWheelRotation());
        System.out.println(e.getX() + " " + e.getY());
    }

    public void zoomView(Point zoomPoint, int rotations) {

        double prevZoom = zoomLevel;

        double zoomDelta = -ZOOM_SENSITIVITY * rotations;
        Coord zoomPointLatLong = XYToLatLong(zoomPoint);

        zoomLevel += zoomDelta;

        if (zoomLevel < MIN_ZOOM_LEVEL) {
            zoomLevel = MIN_ZOOM_LEVEL;
        } else if (zoomLevel > MAX_ZOOM_LEVEL) {
            zoomLevel = MAX_ZOOM_LEVEL;
        }

        if (zoomLevel == prevZoom) {
            return;
        }

        Coord normalizedCoord = normalizeCoord(zoomPointLatLong);

        int tY = (int) (normalizedCoord.lat() * MAP_BASE_SIZE * zoomDelta);
        int tX = (int) (-normalizedCoord.lon() * MAP_BASE_SIZE * zoomDelta);

        this.translateView(tX, tY);
    }

    public void translateView(int deltaX, int deltaY) {
        this.xTranslation += deltaX;
        this.yTranslation += deltaY;
/*
        Point farthestPoint = this.latlongToXY(this.latitudeMax, this.longitudeMax);

        // TODO this equation does not handles maps smaller than main window
        if (this.xTranslation + this.getWidth() > farthestPoint.x) {
            System.out.println("X out of bounds !");
        }
*/
        this.repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 2) {
            zoomLevel = BASE_ZOOM_LEVEL;
            yTranslation = 0;
            xTranslation = 0;
            this.repaint();
        }
        if (e.getButton() == 1) {
            Coord pos = XYToLatLong(e.getPoint());
            tour.selectElement(pos);

            this.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
