package deliverif.gui.panel;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.model.RoadSegment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class RequestPickupPanel extends RequestItemPanel {

    private final Request request;
    private final CityMap map;
    private final int requestNumber;
    private final DeliveryTour tour;

    public RequestPickupPanel(Request request, int requestNumber, CityMap map, DeliveryTour tour) {
        super(tour, request, requestNumber);
        this.request = request;
        this.map = map;
        this.requestNumber = requestNumber;
        this.tour = tour;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setColor(g);

        long idPickupAdress = request.getPickupAddress().getId();


        int placeRequest = 20;

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g.drawString("Pickup Request " + requestNumber, 10, placeRequest);

        int intersectionsInterlign = 15;
        int titleInterlign = 18;


        g.setFont(new Font("TimesRoman", Font.BOLD, 11));

        int placePickup = placeRequest + titleInterlign;

        g.drawString("Road Intersections : ", 20, placePickup);

        Collection<RoadSegment> roadSegmentsPickup = map.getSegmentsOriginatingFrom(idPickupAdress);
        ArrayList<String> segmentNamesPickup = new ArrayList<>();

        for (RoadSegment segment : roadSegmentsPickup) {

            if (!segmentNamesPickup.contains(segment.getName())) {
                segmentNamesPickup.add(segment.getName());
                g.setFont(new Font("TimesRoman", Font.PLAIN, 11));
                g.drawString(segment.getName(), 25, placePickup = placePickup + intersectionsInterlign);

            }

        }
        int placeDuration = placePickup + titleInterlign;
        g.setFont(new Font("TimesRoman", Font.BOLD, 11));
        g.drawString("Pickup Duration : ", 20, placeDuration);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        g.drawString(String.valueOf(request.getPickupDuration()), 25, placeDuration + 20);

        g.drawString("seconds", 50, placeDuration + 20);


    }


}
