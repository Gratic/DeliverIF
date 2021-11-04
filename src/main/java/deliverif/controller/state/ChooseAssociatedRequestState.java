package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class ChooseAssociatedRequestState implements State{
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void requestClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.deleteRequest);
    }

    @Override
    public void cancelButtonClick(Controller controller, Gui gui) {
        State state = controller.getPreviousStates().pop();
        state = controller.getPreviousStates().pop();
        controller.setCurrentState(state);
    }
}
