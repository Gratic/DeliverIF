package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class AddPickupRequestState implements State{
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void addDeliveryButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.addDeliveryRequest);
    }

    @Override
    public void cancelButtonClick(Controller controller, Gui gui) {
        State state = controller.getPreviousStates().pop();
        state = controller.getPreviousStates().pop();
        controller.setCurrentState(state);
    }
}
