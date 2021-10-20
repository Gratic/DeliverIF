package deliverif.gui.mapview;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.*;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

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
    private final int MAP_BASE_WIDTH = 900;
    private final int MAP_BASE_HEIGHT = 900;

    private final double ICON_SIZE = 35;

    private final double MAX_ZOOM_LEVEL = 6.0;
    private final double MIN_ZOOM_LEVEL = .1;
    private final double BASE_ZOOM_LEVEL = 1.0;
    private final double ZOOM_SENSITIVITY = 0.2;
    private double zoomLevel = BASE_ZOOM_LEVEL;

    private int xTranslation = 0, yTranslation = 0;

    public MapView(CityMap map, DeliveryTour tour) {
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setBackground(new Color(100, 100, 100));
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

        BufferedImage deliveryIcon = null;
        BufferedImage pickupIcon = null;
        BufferedImage departureIcon = null;
        try {
            deliveryIcon = ImageIO.read(new File("resources/assets/icons/delivery.png"));
            pickupIcon = ImageIO.read(new File("resources/assets/icons/pickup.png"));
            departureIcon = ImageIO.read(new File("resources/assets/icons/departure.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.isMapLoaded()) {
            g.setColor(new Color(10, 10, 10));

            for (Address address : this.map.getAddresses().values()) {
                if (address.getId() == 190866513) {
                    Point point = this.latlongToXY(address.getLatitude(), address.getLongitude());
                    g.setColor(new Color(200, 0, 0));
                    g.drawChars("Home".toCharArray(), 0, 4, point.x, point.y);
                    g.setColor(new Color(10, 10, 10));
                }

                Collection<RoadSegment> segments = this.map.getSegmentsOriginatingFrom(address.getId());
                if (segments != null) {
                    for (RoadSegment segment : segments) {
                        Point startCoord = this.latlongToXY(segment.getOrigin().getLatitude(),
                                segment.getOrigin().getLongitude());
                        Point endCoord = this.latlongToXY(segment.getDestination().getLatitude(),
                                segment.getDestination().getLongitude());

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
                Point pickupPoint = this.latlongToXY(request.getPickupAddress().getLatitude(), request.getPickupAddress().getLongitude());
                Point deliveryPoint = this.latlongToXY(request.getDeliveryAddress().getLatitude(), request.getDeliveryAddress().getLongitude());


                g.drawImage(dye(pickupIcon, ColorTheme.REQUEST_PALETTE.get(i % ColorTheme.REQUEST_PALETTE.size())),
                        pickupPoint.x - (iconsWidth / 2),
                        pickupPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                g.drawImage(dye(deliveryIcon, ColorTheme.REQUEST_PALETTE.get(i % ColorTheme.REQUEST_PALETTE.size())),
                        deliveryPoint.x - (iconsWidth / 2),
                        deliveryPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                i++;
            }

            Address departureAddress = this.tour.getDepartureAddress();
            if (departureAddress != null) {
                Point point = this.latlongToXY(departureAddress.getLatitude(), departureAddress.getLongitude());


                g.drawImage(dye(departureIcon, ColorTheme.DEPARTURE_COLOR),
                        point.x - (iconsWidth / 2),
                        point.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this
                );


            }
        }
    }


    //Source: https://stackoverflow.com/a/21385150
    private static BufferedImage dye(BufferedImage image, Color color) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return dyed;
    }
    //Source end

    private Point latlongToXY(double latitude, double longitude) {
        double normalizedLatitude = (latitude - latitudeMin) / (latitudeMax - latitudeMin);
        double normalizedLongitude = (longitude - longitudeMin) / (longitudeMax - longitudeMin);

        int y = MAP_BASE_HEIGHT - (int) (normalizedLatitude * MAP_BASE_HEIGHT * zoomLevel) + yTranslation;
        int x = (int) (normalizedLongitude * MAP_BASE_WIDTH * zoomLevel) + xTranslation;
        return new Point(x, y);
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
        int wheelRotationAbs = Math.abs(e.getWheelRotation());
        if (e.getWheelRotation() > 0) { // positive is unzoom
            this.zoomView(-ZOOM_SENSITIVITY * wheelRotationAbs,
                    (int) (e.getX() * this.zoomLevel) + xTranslation,
                    (int) (e.getY() * this.zoomLevel) + yTranslation
            );
        } else if (e.getWheelRotation() < 0) { // negative is zoom
            this.zoomView(ZOOM_SENSITIVITY * wheelRotationAbs,
//                    (int) (e.getX() * this.zoomLevel) + xTranslation,
//                    (int) (e.getY() * this.zoomLevel) + yTranslation
                    (int) (e.getX() * this.zoomLevel),
                    (int) (e.getY() * this.zoomLevel)
            );
        }
        System.out.println(e.getX() + " " + e.getY());
    }

    public void zoomView(double zoomDelta, int zoomPointX, int zoomPointY) {
        double prevZoomLevel = this.zoomLevel;

        this.zoomLevel += zoomDelta;


        if (this.zoomLevel < MIN_ZOOM_LEVEL) {
            this.zoomLevel = MIN_ZOOM_LEVEL;
        } else if (this.zoomLevel > MAX_ZOOM_LEVEL) {
            this.zoomLevel = MAX_ZOOM_LEVEL;
        }

//        this.repaint();

        double absScaleDelta = (1.0 / prevZoomLevel) - (1.0 / this.zoomLevel);
        /*int zoomPointX = (this.MAP_BASE_WIDTH / 2);
        int zoomPointY = (this.MAP_BASE_HEIGHT / 2);*/

        int xOffset = (int) -(zoomPointX * absScaleDelta);
        int yOffset = (int) -(zoomPointY * absScaleDelta);


        this.translateView(xOffset, yOffset);

/*
        double absoluteZoomDelta = this.zoomLevel - prevZoomLevel;
        // double relativeZoomDelta = absoluteZoomDelta / this.zoomLevel;
        double relativeZoomDelta = this.zoomLevel / prevZoomLevel;
        System.out.println(relativeZoomDelta);
        //if(absoluteZoomDelta > 0.0) {
            Point farthestPoint = this.latlongToXY(this.latitudeMax, this.longitudeMax);
            System.out.println(absoluteZoomDelta );

            /*int widthDelta = (int) (farthestPoint.x * (absoluteZoomDelta / this.zoomLevel) / 2.0);
            int heightDelta = (int) (farthestPoint.y * (absoluteZoomDelta / this.zoomLevel) / 2.0);

            int widthDelta = (int)(farthestPoint.x * (relativeZoomDelta - 1.0));
            int heightDelta = (int)(farthestPoint.y * (relativeZoomDelta - 1.0));

            int widthDeltaAbs = (int)(farthestPoint.x * absoluteZoomDelta);
            int heightDeltaAbs = (int)(farthestPoint.y * absoluteZoomDelta);

            System.out.println(widthDelta + " " + heightDelta);
            System.out.println(widthDeltaAbs + " " + heightDeltaAbs);
            this.translateView(-widthDelta, -heightDelta);
//        }*/
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
