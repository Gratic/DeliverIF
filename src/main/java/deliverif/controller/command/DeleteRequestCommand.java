package deliverif.controller.command;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import pdtsp.Pair;

import java.util.List;

public class DeleteRequestCommand implements Command {

    private final DeliveryTour tour;
    private final Request request;
    private final CityMap map;
    private Pair<EnumAddressType, Request> addressBeforePickup;
    private Pair<EnumAddressType, Request> addressBeforeDelivery;

    public DeleteRequestCommand(DeliveryTour tour, Request request, CityMap map) {
        this.request = request;
        this.tour = tour;
        this.map =map;
        this.addressBeforePickup = null;
        this.addressBeforeDelivery = null;
    }

    @Override
    public void doCommand() {
        Pair<Pair<EnumAddressType, Request>, Pair<EnumAddressType, Request>> res = tour.deleteRequestRecompute(request, map);
        this.addressBeforePickup =  res.getX();
        this.addressBeforeDelivery = res.getY();
    }

    @Override
    public void undoCommand() throws Exception {
        tour.addRequestRecompute(request,addressBeforePickup , addressBeforeDelivery, map);
    }
}
