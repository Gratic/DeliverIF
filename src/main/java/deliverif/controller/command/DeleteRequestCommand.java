package deliverif.controller.command;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;

public class DeleteRequestCommand implements Command {

    private final DeliveryTour tour;
    private final Request request;
    private final CityMap map;

    public DeleteRequestCommand(DeliveryTour tour, Request request, CityMap map) {
        this.request = request;
        this.tour = tour;
        this.map =map;
    }

    @Override
    public void doCommand() {
        tour.deleteRequestRecompute(request, map);
    }

    @Override
    public void undoCommand() throws Exception {
        tour.addRequestRecompute(request,null , null, map);
    }
}
