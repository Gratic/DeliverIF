package deliverif.gui.panel;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.model.RoadSegment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class RequestDeliveryPanel extends RequestItemPanel {
    private final CityMap map;


    public RequestDeliveryPanel(Request request, int requestNumber, CityMap map, DeliveryTour tour) {
        super(tour, request, requestNumber);
        this.map = map;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setColor(g);


        long idDeliveryAdress = request.getDeliveryAddress().getId();


        int placeRequest = 10;


        int intersectionsInterlign = 15;
        int titleInterlign = 18;


        g.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        g.drawString("Request " + requestNumber + " - Delivery : ", 10, placeRequest);

        placeRequest = placeRequest + titleInterlign;

        g.drawString("Road Intersections : ", 20, placeRequest);
        int placeDelivery = placeRequest + 20;
        ArrayList<String> segmentNamesDelivery = new ArrayList<>();
        Collection<RoadSegment> roadSegmentsDelivery = map.getSegmentsOriginatingFrom(idDeliveryAdress);
        for (RoadSegment segment : roadSegmentsDelivery) {

            if (!segmentNamesDelivery.contains(segment.getName())) {
                segmentNamesDelivery.add(segment.getName());
                g.drawString(segment.getName(), 25, placeDelivery);
                placeDelivery = placeDelivery + intersectionsInterlign;
            }

        }

        int placeDuration = placeDelivery;
        g.drawString("Delivery Duration : ", 20, placeDuration);
        g.drawString(String.valueOf(request.getDeliveryDuration()), 25, placeDuration + 20);
        g.drawString("seconds", 50, placeDuration + 20);


    }


}
