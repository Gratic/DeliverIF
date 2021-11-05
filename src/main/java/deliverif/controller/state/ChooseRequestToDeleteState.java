package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class ChooseRequestToDeleteState implements State {


    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void addressClick(Controller controller, Gui gui, boolean overlap) {
        //TODO: Find a way to detect overlapping points
        //case delete request
        if (overlap) {
            controller.setCurrentState(controller.chooseAssociatedRequest);
        } else {
            controller.setCurrentState(controller.deleteRequest);
        }
        //case choose associated request
    }

    public void cancelButtonClick(Controller controller, Gui gui) {

        controller.popState();
    }
}
