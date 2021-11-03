package deliverif.controller.command;

import deliverif.model.DeliveryTour;
import deliverif.model.Request;

public class AddRequestCommand implements Command {

    private DeliveryTour tour;
    private Request request;

    public AddRequestCommand(DeliveryTour tour, Request request) {
        this.request = request;
        this.tour = tour;
    }

    @Override
    public void doCommand() {
        tour.addRequestRecompute(request);
    }

    @Override
    public void undoCommand() {
        tour.deleteRequestRecompute(request);
    }
}
