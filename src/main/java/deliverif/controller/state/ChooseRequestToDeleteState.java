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
    public void addressClick(Controller controller, Gui gui, Address addressClicked) {
        //TODO: Find a way to detect overlapping points
        //case delete request
        controller.setCurrentState(controller.deleteRequest);
        //case choose associated request
    }

    public void cancelButtonClick(Controller controller, Gui gui) {

        controller.popState();
    }
}
