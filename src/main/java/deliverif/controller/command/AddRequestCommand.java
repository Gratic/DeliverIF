package deliverif.controller.command;

import deliverif.model.DeliveryTour;
import deliverif.model.Request;

public class AddRequestCommand implements Command {

    private final DeliveryTour tour;
    private final Request request;

    public AddRequestCommand(DeliveryTour tour, Request request) {
        this.request = request;
        this.tour = tour;
    }

    @Override
    public void doCommand() {
        tour.addRequestRecompute(request,0,0);
    }

    @Override
    public void undoCommand() {
        tour.deleteRequestRecompute(request);
    }
}
