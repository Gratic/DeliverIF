package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class AddDeliveryRequestState implements State {
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void validateDeliveryButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.locallyModifyTour);
    }

    @Override
    public void cancelButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.tourCompleted);
    }
}
