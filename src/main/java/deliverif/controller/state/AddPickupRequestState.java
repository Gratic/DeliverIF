package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import deliverif.model.RoadSegment;
import pdtsp.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddPickupRequestState implements State, IAddRequestState {
    private Address pickupAddress;

    @Override
    public void run(Controller controller, Gui gui) {
        int option = JOptionPane.showConfirmDialog(gui.getFrame(),
                "Please choose a pickup address on the map.",
                "Pickup address choice",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.CANCEL_OPTION) {
            controller.popState();
        } else {
            if (controller.getGui().getCurrentViewState() instanceof DisplayRequestsView) {
                ((DisplayRequestsView) controller.getGui().getCurrentViewState()).getComputeButton().setEnabled(false);
            }
        }
        this.pickupAddress = null;
    }

    @Override
    public void addressClick(Controller controller, Gui gui, List<Pair<Double, Address>> addresses) {
        if(this.pickupAddress != null) {
            return; // means we already selected an address, disable to avoid pollution when clicking on a request
        }

        Address selectedAddress = addresses.get(0).getY();

        if (addresses.size() > 1) {  // handle multiple addresses nearby
            selectedAddress = this.showAddressChoiceForm(controller, addresses);
            if(selectedAddress == null) {  // means the user cancelled the operation
                return;
            }
        }

        System.out.println("Selected address: " + selectedAddress.getLatitude() + " " + selectedAddress.getLongitude() + " " + selectedAddress.getId());

        int option = JOptionPane.showConfirmDialog(
                gui.getFrame(),
                "You selected address n°" + selectedAddress.getId(),
                "Pickup address choice",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address
            this.pickupAddress = selectedAddress;
            JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "Please select the request address it should be inserted after.",
                    "Preceding address choice",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    @Override
    public void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {
        if(this.pickupAddress != null) {
            String message = this.addressBeforeMessage(new Pair<>(addressType, request));

            int option = JOptionPane.showConfirmDialog(
                    gui.getFrame(),
                    "You chose to add the pickup address directly after " + message + ".",
                    "Preceding address choice",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if(option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address
                Pair<EnumAddressType, Request> predecessor = new Pair<>(addressType, request);

                ((AddDeliveryRequestState) controller.addDeliveryRequest).entryAction(this.pickupAddress, predecessor);
                controller.setCurrentState(controller.addDeliveryRequest);
            }
        }
    }
}
