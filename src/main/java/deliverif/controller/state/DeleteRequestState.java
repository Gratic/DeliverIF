package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.controller.command.AddRequestCommand;
import deliverif.controller.command.Command;
import deliverif.controller.command.DeleteRequestCommand;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;

import javax.swing.*;

public class DeleteRequestState implements State {
    @Override
    public void run(Controller controller, Gui gui) {
        int option = JOptionPane.showConfirmDialog(gui.getFrame(),
                "Please choose a request to delete on the map.",
                "Request choice",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.CANCEL_OPTION) {
            controller.popState();
        } else {
            if (controller.getGui().getCurrentViewState() instanceof DisplayRequestsView) {
                ((DisplayRequestsView) controller.getGui().getCurrentViewState()).getComputeButton().setEnabled(false);
            }
        }
    }

    @Override
    public void validateDeleteRequestButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.locallyModifyTour);
    }

    @Override
    public void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {

        if (addressType == EnumAddressType.DEPARTURE_ADDRESS){
            JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "Cannot delete departure address.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        else {
            int option = JOptionPane.showConfirmDialog(
                    gui.getFrame(),
                    "You selected address n°" + (controller.getTour().getRequests().indexOf(request) + 1),
                    "Request to delete",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address

                Command command = new DeleteRequestCommand(
                        controller.getTour(), request, controller.getCityMap()
                );

                try {
                    controller.getListOfCommands().add(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (controller.getTour().getRequests().size() == 0){
            controller.setCurrentState(controller.mapLoaded);
        }
        else {
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
