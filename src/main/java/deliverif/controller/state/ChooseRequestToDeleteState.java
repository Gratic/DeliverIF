package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.Address;
import pdtsp.Pair;

import java.util.List;

public class ChooseRequestToDeleteState implements State {


    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void addressClick(Controller controller, Gui gui, List<Pair<Double, Address>> addresses) {
        //TODO: Find a way to detect overlapping points
        //case delete request
        if (addresses.size() > 1) {
            controller.setCurrentState(controller.chooseAssociatedRequest);
        } else {
            controller.setCurrentState(controller.deleteRequest);
        }
        //case choose associated request
    }

    public void cancelButtonClick(Controller controller, Gui gui) {

        controller.popState();
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
