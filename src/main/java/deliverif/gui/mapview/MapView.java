package deliverif.gui.mapview;

import deliverif.controller.Controller;
import deliverif.controller.state.MapLoadedState;
import deliverif.gui.utils.ColorTheme;
import deliverif.model.*;
import deliverif.observer.Observable;
import deliverif.observer.Observer;
import pdtsp.Pair;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static deliverif.gui.utils.Assets.*;
import static deliverif.gui.utils.Utils.dye;

public class MapView extends JPanel implements Observer, MouseInputListener, MouseWheelListener {
    private CityMap map;
    private final DeliveryTour tour;
    private final Controller controller;

    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 400;
    private final int MAP_BASE_SIZE = 1200;

    private final double ICON_SIZE = 34;
    private final double ICON_SELECTED_MULT = 1.5;

    private final double MAX_ZOOM_LEVEL = 6.0;
    private final double MIN_ZOOM_LEVEL = .1;
    private final double BASE_ZOOM_LEVEL = 1.0;
    private final double ZOOM_SENSITIVITY = 0.2;

    private final float BASE_STREET_SIZE = 1.f;
    private final float PATH_SIZE_MULT = 1.75f;
    private final float BOTH_WAY_MULT = 1.25f;

    private final int HOVER_SIZE = 5;

    private double
            latitudeMin = Double.MAX_VALUE,
            latitudeMax = Double.MIN_VALUE,
            longitudeMin = Double.MAX_VALUE,
            longitudeMax = Double.MIN_VALUE;

    private double zoomLevel = BASE_ZOOM_LEVEL;
    private int xTranslation = 0, yTranslation = 0;

    private Address hoveredAddress;

    public MapView(Controller controller) {
        this.controller = controller;
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setBackground(ColorTheme.LIGHT_BASE_GREY_ALT);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);


        this.map = controller.getCityMap();
        this.map.addObserver(this);
        this.recomputeMinMaxLatLong();

        this.tour = controller.getTour();
        this.tour.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        float streetSize = (float) (this.zoomLevel * BASE_STREET_SIZE);
        g2d.setStroke(new BasicStroke(streetSize));

        if (this.isMapLoaded()) {
            g.setColor(new Color(10, 10, 10));

            for (Address address : this.map.getAddresses().values()) {
                if (address.getId() == 190866513) {
                    Point point = this.latlongToXY(address.getCoords());
                    g.setColor(new Color(200, 0, 0));
                    g.drawChars("Home".toCharArray(), 0, 4, point.x, point.y);
                    g.setColor(new Color(10, 10, 10));
                }

                if (hoveredAddress != null && address == hoveredAddress) {
                    Point pos = latlongToXY(address.getCoords());
                    int hoverSize = (int) (HOVER_SIZE * this.zoomLevel);
                    g.fillOval(pos.x - hoverSize / 2, pos.y - hoverSize / 2, hoverSize, hoverSize);
                }
                Collection<RoadSegment> segments = this.map.getSegmentsOriginatingFrom(address.getId());
                if (segments != null) {
                    for (RoadSegment segment : segments) {
                        // check if road is one-way only
                        if (this.map.findSegment(segment.getDestination().getId(), segment.getOrigin().getId()) == null) {
                            g2d.setStroke(new BasicStroke(streetSize));
                        } else {
                            g2d.setStroke(new BasicStroke(streetSize * BOTH_WAY_MULT));
                        }

                        Point startCoord = this.latlongToXY(segment.getOrigin().getCoords());
                        Point endCoord = this.latlongToXY(segment.getDestination().getCoords());

                        g2d.drawLine(startCoord.x, startCoord.y, endCoord.x, endCoord.y);
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
                    iconsHeight *= ICON_SELECTED_MULT;
                    iconsWidth *= ICON_SELECTED_MULT;
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
                    iconsHeight /= ICON_SELECTED_MULT;
                    iconsWidth /= ICON_SELECTED_MULT;
                }
                i++;
            }

            Address departureAddress = this.tour.getDepartureAddress();
            if (departureAddress != null) {
                Point point = this.latlongToXY(departureAddress.getCoords());
                if (tour.isDepartureSelected()) {
                    iconsHeight *= ICON_SELECTED_MULT;
                    iconsWidth *= ICON_SELECTED_MULT;
                }

                g.drawImage(dye(departureImage, ColorTheme.DEPARTURE_COLOR),
                        point.x - (iconsWidth / 2),
                        point.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this
                );
            }

            // Display tour path
            if (this.tour.getPath().size() >= 2) {
                g.setColor(Color.RED);
                for (int j = 0; j < this.tour.getPath().size() - 1; j++) {
                    RoadSegment segment = this.tour.getPath().get(j);

                    Point segStart = this.latlongToXY(segment.getOrigin().getCoords());
                    Point segEnd = this.latlongToXY(segment.getDestination().getCoords());

                    Point printDelta = new Point(
                            segEnd.x - segStart.x < 0 ? 1 : -1,
                            segEnd.y - segStart.y < 0 ? 1 : -1
                    );

                    Point arrowTip = new Point(
                            segStart.x + (int) ((segEnd.x - segStart.x) * 0.8),
                            segStart.y + (int) ((segEnd.y - segStart.y) * 0.8)
                    );

                    Point segNormal = new Point(
                            segStart.x - (int) ((segEnd.y - segStart.y) * 0.5),
                            segStart.y + (int) ((segEnd.x - segStart.x) * 0.5)
                    );

                    Point arrowVec = new Point(
                            segNormal.x - arrowTip.x,
                            segNormal.y - arrowTip.y
                    );
                    double arrowVecNorm = Math.sqrt(arrowVec.x * arrowVec.x + arrowVec.y * arrowVec.y);

                    Point arrowBase = new Point(
                            arrowTip.x + (int) ((segNormal.x - arrowTip.x) / arrowVecNorm * 3 * this.zoomLevel),
                            arrowTip.y + (int) ((segNormal.y - arrowTip.y) / arrowVecNorm * 3 * this.zoomLevel)
                    );

                    g2d.setStroke(new BasicStroke((streetSize * PATH_SIZE_MULT)));
                    g2d.drawLine(segStart.x + printDelta.x, segStart.y + printDelta.y, segEnd.x + printDelta.x, segEnd.y + printDelta.y);
                    g2d.drawLine(arrowBase.x, arrowBase.y, arrowTip.x, arrowTip.y);
                }
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
        return tour != null && tour.getRequests().size() > 0; // TODO warning, tour could have no requests at the beginning
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

        if (this.isMapLoaded()) {
            Pair<Double, Address> closestAddr = map.getClosestAddressFrom(XYToLatLong(e.getPoint()));  // add dist if wished

            if (closestAddr.getX() < 50.) {
                hoveredAddress = closestAddr.getY();
            } else {
                hoveredAddress = null;
            }
            repaint();
        } else {
            hoveredAddress = null;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomView(e.getPoint(), e.getWheelRotation());
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

        this.repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            zoomLevel = BASE_ZOOM_LEVEL;
            yTranslation = 0;
            xTranslation = 0;
            this.repaint();
        }

        if (SwingUtilities.isLeftMouseButton(e)) { // left click on map
            // trigger controller address event
            List<Pair<Double, Address>> closestAddresses = map.getClosestAddressesFrom(XYToLatLong(e.getPoint()), 50.);
            for (Pair<Double, Address> p : closestAddresses) {
                System.out.println(p.getY().getId() + " - " + p.getX());
            }

            if (closestAddresses.size() > 0) {  // don't call addressClick if no addresses were actually clicked
                controller.addressClick(controller.getGui(), closestAddresses);  // TODO choose address here

                // TODO if an address was chosen, don't execute request selection path
            }

            // check request selection / controller event
            if (this.isTourLoaded()) {  // request highlighting
                Coord pos = XYToLatLong(e.getPoint());
                List<Pair<Pair<EnumAddressType, Request>, Double>> requests = tour.getClosestRequestsFrom(pos, 50.);
                Pair<EnumAddressType, Request> selectedRequest = null;

                if (requests.size() > 1) {
                    selectedRequest = this.showRequestSelectionForm(requests);
                }
                else if (requests.size() == 1) {
                    selectedRequest = requests.get(0).getX();
                }

                if(selectedRequest != null) {
                    if(selectedRequest.getX() == EnumAddressType.DEPARTURE_ADDRESS) {
                        tour.selectDepartureAddress();
                    }
                    else {
                        tour.selectRequest(selectedRequest.getY());
                    }

                    controller.requestClick(controller.getGui(), selectedRequest.getY(), selectedRequest.getX());
                }

                this.repaint();
            }

        }
    }

    /**
     * Opens a popup that allows the user to choose a request among multiple nearby requests
     * @return null if the user cancelled the operation, the selected request address otherwise
     */
    private Pair<EnumAddressType, Request> showRequestSelectionForm(List<Pair<Pair<EnumAddressType, Request>, Double>> requests) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.add(new JLabel("There are several request addresses nearby. Please select one among those or retry:"));
        List<JRadioButton> choices = new ArrayList<>();
        ButtonGroup radioGroup = new ButtonGroup();

        for (Pair<Pair<EnumAddressType, Request>, Double> request : requests) {
            String message;
            switch (request.getX().getX()) {
                case DELIVERY_ADDRESS -> {
                    message = "Delivery request address (" + String.format("%.2f", request.getY()) + "m away)";
                }
                case PICKUP_ADDRESS -> {
                    message = "Pickup request address (" + String.format("%.2f", request.getY()) + "m away)";
                }
                case DEPARTURE_ADDRESS -> {
                    message = "Departure address (" + String.format("%.2f", request.getY()) + "m away)";
                }
                default -> {
                    message = "Entrée erronée";
                }
            }

            JRadioButton button = new JRadioButton(message);
            radioGroup.add(button);
            choices.add(button);
        }

        int option = JOptionPane.showConfirmDialog(this,
                optionPanel, "Choose a request address", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            int i = 0;
            for(JRadioButton button : choices) {
                if(button.isSelected()) {
                    return requests.get(i).getX();
                }
                i++;
            }
        }
        return null;
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
