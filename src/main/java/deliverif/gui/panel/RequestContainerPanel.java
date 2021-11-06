package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import deliverif.observer.Observable;
import deliverif.observer.Observer;
import pdtsp.Pair;

import javax.swing.*;
import java.awt.*;

import static deliverif.model.EnumAddressType.DEPARTURE_ADDRESS;
import static deliverif.model.EnumAddressType.TRAVERSAL_ADDRESS;

public class RequestContainerPanel extends JPanel implements Observer {

    private final CityMap map;
    private final DeliveryTour tour;


    public RequestContainerPanel(CityMap map, DeliveryTour tour) {
        this.setBackground(ColorTheme.PANEL_2_BASE_BG);
        this.map = map;
        this.tour = tour;
        this.tour.addObserver(this);
        this.setPreferredSize(new Dimension(270, tour.getRequests().size() * 130 * 2 + 50));
        this.setLayout(new FlowLayout());
        init();
    }


    @Override
    public void update(Observable observed, Object arg) {
        this.init();
        this.repaint();
    }

    public void init() {
        removeAll();
        if (tour.getRequests().size() == 0) {
            System.out.println("No request loaded");
        } else {
            int location = 0;
            int requestNumber;
            DeparturePointPanel departurePointPanel = new DeparturePointPanel(map, tour);
            add(departurePointPanel);
            departurePointPanel.setLocation(0, location);

            location = location + departurePointPanel.getHeight() + 10;

            if (tour.getPath().size() >= 2) {
                for (Pair<EnumAddressType, Request> requestPair : tour.getAddressRequestMetadata()) {
                    if (requestPair.getX() != DEPARTURE_ADDRESS && requestPair.getX() != TRAVERSAL_ADDRESS) {
                        RequestItemPanel panel = null;
                        requestNumber = tour.getRequests().indexOf(requestPair.getY()) + 1;
                        switch (requestPair.getX()) {
                            case DELIVERY_ADDRESS -> panel = new RequestDeliveryPanel(requestPair.getY(), requestNumber, map, tour);
                            case PICKUP_ADDRESS -> panel = new RequestPickupPanel(requestPair.getY(), requestNumber, map, tour);

                        }
                        if (panel != null) {

                            panel.setLocation(0, location);
                            add(panel);
                            location = location + panel.getHeight() + 10;
                        }
                    }
                }
            } else {
                for (Request r : tour.getRequests()) {
                    requestNumber = tour.getRequests().indexOf(r) + 1;
                    RequestPickupPanel rpPanel = new RequestPickupPanel(r, requestNumber, map, tour);
                    RequestDeliveryPanel rdPanel = new RequestDeliveryPanel(r, requestNumber, map, tour);

                    rpPanel.setLocation(0, location);
                    rdPanel.setLocation(0, location += (rpPanel.getHeight() + 10));
                    location += rdPanel.getHeight() + 10;

                    add(rpPanel);
                    add(rdPanel);
                }
            }

        }
        revalidate();
    }

}