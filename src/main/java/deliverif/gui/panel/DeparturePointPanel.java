package deliverif.gui.panel;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.RoadSegment;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

public class DeparturePointPanel extends ScrollItemPanel implements MouseListener {


    private final CityMap map;


    public DeparturePointPanel(CityMap map, DeliveryTour tour) {
        super(tour);
        this.setPreferredSize(new Dimension(270, 50));
        this.map = map;


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setColor(g);
        g.drawString("Departure Point : ", 10, 15);
        long idDeparturePointAdress = tour.getDepartureAddress().getId();
        int placeDeparture = 30;
        int intersectionsInterlign = 15;
        Collection<RoadSegment> roadSegmentsDeparture = map.getSegmentsOriginatingFrom(idDeparturePointAdress);
        ArrayList<String> segmentNamesDeparture = new ArrayList<>();
        for (RoadSegment segment : roadSegmentsDeparture) {

            if (!segmentNamesDeparture.contains(segment.getName())) {
                segmentNamesDeparture.add(segment.getName());
                g.drawString(segment.getName(), 25, placeDeparture);
                placeDeparture = placeDeparture + intersectionsInterlign;
            }

        }


    }




    public boolean selected() {
        return this.tour.isDepartureSelected();
    }

    public void select() {
        tour.selectDepartureAddress();
    }
}
