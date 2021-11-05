package deliverif.gui.panel;

import deliverif.model.DeliveryTour;
import deliverif.model.Request;

import java.awt.*;

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
