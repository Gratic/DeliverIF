package deliverif.controller.command;

import deliverif.model.DeliveryTour;
import deliverif.model.Request;

public class DeleteRequestCommand implements Command {

    private final DeliveryTour tour;
    private final Request request;

    public DeleteRequestCommand(DeliveryTour tour, Request request) {
        this.request = request;
        this.tour = tour;
    }

    @Override
    public void doCommand() {
        tour.deleteRequestRecompute(request);
    }

    @Override
    public void undoCommand() {
        tour.addRequestRecompute(request, 0, 0);
    }
}
