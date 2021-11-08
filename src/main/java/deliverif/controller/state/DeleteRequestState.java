package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class DeleteRequestState implements State {
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void validateDeleteRequestButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.locallyModifyTour);
    }

    public void cancelButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.tourCompleted);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
