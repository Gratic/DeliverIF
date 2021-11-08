package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;

import javax.swing.*;

public class DeleteRequestState implements State {
    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void validateDeleteRequestButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.locallyModifyTour);
    }

    @Override
    public void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {


        int option = JOptionPane.showConfirmDialog(
                gui.getFrame(),
                "You selected address n°" + request.getPickupAddress() + request.getDeliveryAddress(),
                "Request to delete",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address
            controller.getTour().deleteRequest(request);

            JOptionPane.showMessageDialog(gui.getFrame(), "Request deleted. The system will now recompute the tour.");

            // TODO add call to recompute here

            controller.setCurrentState(controller.tourCompleted);
        }
    }

    public void cancelButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.tourCompleted);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
