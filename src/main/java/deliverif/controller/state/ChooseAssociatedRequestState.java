package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;

public class ChooseAssociatedRequestState implements State {
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {
        controller.setCurrentState(controller.deleteRequest);
    }

    @Override
    public void cancelButtonClick(Controller controller, Gui gui) {
        controller.popStates(2);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
