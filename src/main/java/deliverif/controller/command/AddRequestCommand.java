package deliverif.controller.command;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import pdtsp.Pair;

public class AddRequestCommand implements Command {

    private final DeliveryTour tour;
    private final Request request;
    private final CityMap map;
    private final Pair<EnumAddressType, Request> addressBeforePickup;
    private final Pair<EnumAddressType, Request> addressBeforeDelivery;

    public AddRequestCommand(DeliveryTour tour, Request request, CityMap map,
                             Pair<EnumAddressType, Request> addressBeforePickup,
                             Pair<EnumAddressType, Request> addressBeforeDelivery) {
        this.request = request;
        this.tour = tour;
        this.map = map;
        this.addressBeforePickup = addressBeforePickup;
        this.addressBeforeDelivery = addressBeforeDelivery;
    }

    @Override
    public void doCommand() throws Exception {
        tour.addRequestRecompute(request, addressBeforePickup, addressBeforeDelivery, map);

    }

    @Override
    public void undoCommand() {
        tour.deleteRequestRecompute(request, map);
    }
}
