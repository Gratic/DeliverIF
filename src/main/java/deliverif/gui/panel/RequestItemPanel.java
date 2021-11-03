package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.model.RoadSegment;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public abstract class RequestItemPanel extends ScrollItemPanel {

    protected Request request;
    protected int requestNumber;


    public RequestItemPanel(DeliveryTour tour, Request request, int requestNumber) {
        super(tour);

        this.request = request;
        this.requestNumber = requestNumber;


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }


    public boolean selected() {
        return this.tour.isSelected(request);
    }

    public void select() {
        tour.selectRequest(request);
    }

}
