package deliverif.gui.mapview;

import deliverif.controller.Controller;
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
import java.nio.file.Path;
import java.util.List;
import java.util.*;

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
    private final double BASE_ZOOM_LEVEL = 1.0;
    private final double ZOOM_SENSITIVITY = 0.2;

    private final float BASE_STREET_SIZE = 1.f;
    private final float PATH_SIZE_MULT = 4.f;
    private final float BOTH_WAY_MULT = 1.25f;
    private final float PATH_ARROW_THICKNESS = 3.f;

    private final int HOVER_SIZE = 5;
    private final double ADDRESS_SELECTION_THRESHOLD = 20.;
    private final double REQUEST_SELECTION_THRESHOLD = 50.;

    private double
            latitudeMin = Double.MAX_VALUE,
            latitudeMax = Double.MIN_VALUE,
            longitudeMin = Double.MAX_VALUE,
            longitudeMax = Double.MIN_VALUE;

    private double zoomLevel = BASE_ZOOM_LEVEL;
    private int xTranslation = 0, yTranslation = 0;

    private boolean isMapClickable = false;
    private Address hoveredAddress;

    private Map<Request, Color> requestColorMap = new HashMap<>();
    private Map<RoadSegment, Integer> pathOverlapCounts = new HashMap<>();

    enum PathColorMode {
        RED,
        REQUEST_COLOR,
        GRADIENT
    }
    private PathColorMode colorMode = PathColorMode.GRADIENT;

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
        this.requestColorMap.clear();
        this.pathOverlapCounts.clear();

        Graphics2D g2d = (Graphics2D) g;
        float streetSize = (float) (this.zoomLevel * BASE_STREET_SIZE);
        g2d.setStroke(new BasicStroke(streetSize));

        if (this.isMapLoaded()) {
            g.setColor(new Color(10, 10, 10));

            Hashtable<Point, List> roadNames = new Hashtable<>();
            List<String> uniqueRoadNames = new ArrayList<>();

            for (Address address : this.map.getAddresses().values()) {

                // Display address hover
                if (this.isMapClickable && hoveredAddress != null && address == hoveredAddress) {
                    Point pos = latlongToXY(address.getCoords());
                    int hoverSize = (int) (HOVER_SIZE * this.zoomLevel);
                    g.fillOval(pos.x - hoverSize / 2, pos.y - hoverSize / 2, hoverSize, hoverSize);
                }

                // Display segments originating from this address
                Collection<RoadSegment> segments = this.map.getSegmentsOriginatingFrom(address.getId());

                if (hoveredAddress != null) {
                    Collection<RoadSegment> roadNameSegments = this.map.getSegmentsOriginatingFrom(hoveredAddress.getId());

                    if (roadNameSegments != null) {
                        for (RoadSegment roadNameSegment : roadNameSegments) {
                            Point startCoord = this.latlongToXY(roadNameSegment.getOrigin().getCoords());
                            Point endCoord = this.latlongToXY(roadNameSegment.getDestination().getCoords());
                            Point center = new Point(startCoord.x + ((endCoord.x - startCoord.x) / 2), startCoord.y + ((endCoord.y - startCoord.y) / 2));
                            String roadName = roadNameSegment.getName();
                            int len = roadName.length();
                            double deg = Math.toDegrees(Math.atan2(center.y - endCoord.y, center.x - endCoord.x) + Math.PI);
                            if ((deg > 90) && (deg < 270)) {
                                deg += 180;
                            }
                            double angle = Math.toRadians(deg);
                            center.setLocation(center.x - (len) / 2, center.y - (int)(20 * this.zoomLevel));

                            List<String> roadNameDetails = new ArrayList<>();
                            roadNameDetails.add(String.valueOf(angle));
                            roadNameDetails.add(roadName);

                            if (!uniqueRoadNames.contains(roadName)) {
                                uniqueRoadNames.add(roadName);
                                roadNames.put(center, roadNameDetails);
                            }
                        }
                    }
                }

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

            // Road names rendering
            int fontSize = g.getFont().getSize();
            String fontName = g.getFont().getName();
            Font myFont = new Font(fontName, Font.PLAIN, (int) ( fontSize * this.zoomLevel));
            g.setFont(myFont);
            for (Map.Entry<Point, List> entry : roadNames.entrySet()) {
                Double angle = Double.valueOf(String.valueOf(entry.getValue().get(0)));
                int centerX = entry.getKey().x;
                int centerY = entry.getKey().y;

                ((Graphics2D) g).rotate(angle, centerX, centerY);

                g.drawString(String.valueOf(entry.getValue().get(1)), centerX, centerY);

                ((Graphics2D) g).rotate(-angle, centerX, centerY);
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

            // Display request icons
            for (Request request : this.tour.getRequests()) {
                if (tour.isSelected(request)) {
                    iconsHeight *= ICON_SELECTED_MULT;
                    iconsWidth *= ICON_SELECTED_MULT;
                }
                Point pickupPoint = this.latlongToXY(request.getPickupAddress().getCoords());
                Point deliveryPoint = this.latlongToXY(request.getDeliveryAddress().getCoords());
                Color requestColor = ColorTheme.REQUEST_PALETTE.get(i % ColorTheme.REQUEST_PALETTE.size());

                g.drawImage(dye(pickupImage, requestColor),
                        pickupPoint.x - (iconsWidth / 2),
                        pickupPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                g.drawImage(dye(deliveryImage, requestColor),
                        deliveryPoint.x - (iconsWidth / 2),
                        deliveryPoint.y - (iconsHeight),
                        iconsWidth,
                        iconsHeight,
                        this);

                this.requestColorMap.put(request, requestColor);

                if (tour.isSelected(request)) {
                    iconsHeight /= ICON_SELECTED_MULT;
                    iconsWidth /= ICON_SELECTED_MULT;
                }
                i++;
            }

            // Display departure address icon
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
                requestColorMap.put(null, ColorTheme.DEPARTURE_COLOR);
            }

            // Display tour path
            if (this.tour.getPath().size() >= 2) {
                String fontName = g.getFont().getName();
                Font myFont = new Font(fontName, Font.PLAIN, 20);
                g.setFont(myFont);

                g.setColor(new Color(255, 255, 0));
                g.drawString("Départ", 10, 20);
                g.setColor(new Color(255, 0, 255));
                g.drawString("Arrivée", 10, 50);

                int nextRqAddressIndex = this.tour.nextRequestAddressIndex(0);
                Color color = ColorTheme.DEPARTURE_COLOR;
                Point prevEnd = null;

                for (int j = 1; j < this.tour.getPathAddresses().size(); j++) {  // address 0 is always departure
                    Address currentAddress = this.tour.getPathAddresses().get(j);
                    Pair<EnumAddressType, Request> addrMetadata = this.tour.getAddressRequestMetadata().get(j);

                    RoadSegment segment = this.tour.getPath().get(j - 1);

                    if(colorMode == PathColorMode.REQUEST_COLOR) {
                        g.setColor(color);
                    }
                    else if(colorMode == PathColorMode.GRADIENT) {
                        double ratio = ((double)j / this.tour.getPathAddresses().size());
                        int colorValue = 255 - (int) (ratio * 255);
                        int reverseColorValue = 255 - colorValue;
                        g.setColor(new Color(255, colorValue, reverseColorValue));
                    }
                    else {
                        g.setColor(Color.RED);
                    }

                    if (j == nextRqAddressIndex) {  // color update will apply on next segment (this is intended)
                        color = requestColorMap.get(addrMetadata.getY());
                        nextRqAddressIndex = this.tour.nextRequestAddressIndex(nextRqAddressIndex);
                    }

                    int overlap = getOverlap(segment);

                    Point segStart = this.latlongToXY(segment.getOrigin().getCoords());
                    Point segEnd = this.latlongToXY(segment.getDestination().getCoords());

                    boolean bothWayRoad = this.map.findSegment(segment.getDestination().getId(), segment.getOrigin().getId()) != null;
                    int deltaAbs = (int) ((bothWayRoad ? 0.5 + overlap : overlap) * streetSize * PATH_SIZE_MULT);
                    Point printDelta = new Point(
                            segEnd.x - segStart.x < 0 ? deltaAbs : -deltaAbs,
                            segEnd.y - segStart.y < 0 ? deltaAbs : -deltaAbs
                    );
                    segStart.x += printDelta.x;
                    segStart.y += printDelta.y;
                    segEnd.x += printDelta.x;
                    segEnd.y += printDelta.y;

                    if (prevEnd == null) {
                        prevEnd = new Point(segEnd);
                    }

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
                            arrowTip.x + (int) ((segNormal.x - arrowTip.x) / arrowVecNorm * 4 * this.zoomLevel),
                            arrowTip.y + (int) ((segNormal.y - arrowTip.y) / arrowVecNorm * 4 * this.zoomLevel)
                    );

                    g2d.setStroke(new BasicStroke((float) (streetSize * PATH_SIZE_MULT / zoomLevel), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    g2d.drawPolyline(new int[]{prevEnd.x, segStart.x, segEnd.x}, new int[]{prevEnd.y, segStart.y, segEnd.y}, 3);
                    prevEnd = new Point(segEnd);
                    g2d.setStroke(new BasicStroke(PATH_ARROW_THICKNESS));
                    g2d.drawLine(arrowBase.x, arrowBase.y, arrowTip.x, arrowTip.y);

                    addOverlap(segment);

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

            if (closestAddr.getX() < ADDRESS_SELECTION_THRESHOLD) {
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
    }

    public void zoomView(Point zoomPoint, int rotations) {

        double prevZoom = zoomLevel;

        double zoomDelta = -ZOOM_SENSITIVITY * rotations;
        Coord zoomPointLatLong = XYToLatLong(zoomPoint);

        // Handle max unzoom
        int mapSize = (int) (MAP_BASE_SIZE * (this.zoomLevel + zoomDelta));
        if (mapSize < this.getWidth() * 0.7
                && mapSize < this.getHeight() * 0.7
                && rotations > 0) {  // rotations > 0 means unzoom
            return;
        }

        zoomLevel += zoomDelta;

        if (zoomLevel > MAX_ZOOM_LEVEL) {
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
            if (this.isMapClickable) {
                List<Pair<Double, Address>> closestAddresses = map.getClosestAddressesFrom(XYToLatLong(e.getPoint()), ADDRESS_SELECTION_THRESHOLD);
                for (Pair<Double, Address> p : closestAddresses) {
                    System.out.println(p.getY().getId() + " - " + p.getX());
                }

                Address clickedAddress = null;

                if (closestAddresses.size() >= 1) {
                    clickedAddress = closestAddresses.get(0).getY();
                }

                if (clickedAddress != null) { // don't call addressClick if no addresses were actually clicked/chosen
                    controller.addressClick(controller.getGui(), clickedAddress);
                }
            }

            // check request selection / controller event
            if (this.isTourLoaded()) {  // request highlighting
                Coord pos = XYToLatLong(e.getPoint());
                List<Pair<Double, Pair<EnumAddressType, Request>>> requests = tour.getClosestRequestsFrom(pos, REQUEST_SELECTION_THRESHOLD);
                Pair<EnumAddressType, Request> selectedRequest = null;

                if (requests.size() > 1) {
                    selectedRequest = this.showRequestSelectionForm(requests);
                } else if (requests.size() == 1) {
                    selectedRequest = requests.get(0).getY();
                }

                if (selectedRequest != null) {
                    if (selectedRequest.getX() == EnumAddressType.DEPARTURE_ADDRESS) {
                        tour.selectDepartureAddress();
                    } else {
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
     *
     * @return null if the user cancelled the operation, the selected request address otherwise
     */
    private Pair<EnumAddressType, Request> showRequestSelectionForm(List<Pair<Double, Pair<EnumAddressType, Request>>> requests) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.add(new JLabel("There are several request addresses nearby. Please select one among those or retry:"));
        List<JRadioButton> choices = new ArrayList<>();
        ButtonGroup radioGroup = new ButtonGroup();

        for (Pair<Double, Pair<EnumAddressType, Request>> request : requests) {
            String message;
            switch (request.getY().getX()) {
                case DELIVERY_ADDRESS -> {
                    message = "Delivery request address (" + String.format("%.2f", request.getX()) + "m away)";
                }
                case PICKUP_ADDRESS -> {
                    message = "Pickup request address (" + String.format("%.2f", request.getX()) + "m away)";
                }
                case DEPARTURE_ADDRESS -> {
                    message = "Departure address (" + String.format("%.2f", request.getX()) + "m away)";
                }
                default -> {
                    message = "Entrée erronée";
                }
            }

            JRadioButton button = new JRadioButton(message);
            radioGroup.add(button);
            choices.add(button);
            optionPanel.add(button);
        }

        int option = JOptionPane.showConfirmDialog(this,
                optionPanel, "Choose a request address", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int i = 0;
            for (JRadioButton button : choices) {
                if (button.isSelected()) {
                    return requests.get(i).getY();
                }
                i++;
            }
        }
        return null;
    }

    /**
     * Opens a popup that allows the user to choose an address among multiple nearby addresses
     *
     * @return null if the user cancelled the operation, the selected request address otherwise
     */
    @Deprecated
    private Address showAddressSelectionForm(List<Pair<Double, Address>> addresses) {
        JPanel optionPanel = new JPanel();
        optionPanel.add(new JLabel("There are several addresses nearby. Please select one of those, or try again:"));
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        List<JRadioButton> choices = new ArrayList<>();
        ButtonGroup radioGroup = new ButtonGroup();

        // populate radio buttons
        for (Pair<Double, Address> addressPair : addresses) {
            Address address = addressPair.getY();

            String nearMessage = this.createAddressRoadsNearbyText(controller, address);
            JRadioButton radio = new JRadioButton(
                    "<html><p style='width: 400px'>" + String.format("%.2f", addressPair.getX()) + "m away:<br>" + nearMessage + "</p></html>"
            );

            if (choices.isEmpty()) { // means this is the first address of the list
                radio.setSelected(true);
            }

            choices.add(radio);
            radioGroup.add(radio);
            optionPanel.add(radio);
        }

        int option = JOptionPane.showConfirmDialog(
                controller.getGui().getFrame(), optionPanel, "Multiple possible addresses", JOptionPane.OK_CANCEL_OPTION
        );
        if (option == JOptionPane.OK_OPTION) {
            int i = 0;
            for (JRadioButton button : choices) {
                if (button.isSelected()) {
                    return addresses.get(i).getY();
                }
                i++;
            }
        }

        return null;
    }

    private String createAddressRoadsNearbyText(Controller controller, Address address) {
        Collection<RoadSegment> segments = controller.getCityMap().getSegmentsOriginatingFrom(address.getId());
        StringBuilder nearMessage = new StringBuilder("near<br>");
        for (RoadSegment segment : segments) {
            // &#09 is the tab character
            nearMessage.append("&#09").append(segment.getName()).append("<br>");
        }

        return nearMessage.toString();
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

    public void setMapClickable(boolean clickable) {
        this.isMapClickable = clickable;
    }

    private int getOverlap(RoadSegment segment) {
        RoadSegment reverse = map.findSegment(segment.getDestination().getId(), segment.getOrigin().getId());
        int res = 0;
        if (reverse != null) {
            res = pathOverlapCounts.getOrDefault(reverse, res);
        }

        res += pathOverlapCounts.getOrDefault(segment, 0);
        return res;
    }

    private void addOverlap(RoadSegment segment) {
        if (pathOverlapCounts.containsKey(segment)) {
            pathOverlapCounts.replace(segment, pathOverlapCounts.get(segment) + 1);
        } else {
            pathOverlapCounts.put(segment, 1);
        }
    }

}
