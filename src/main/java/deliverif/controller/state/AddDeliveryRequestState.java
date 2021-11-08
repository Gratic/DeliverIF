package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import deliverif.model.RoadSegment;
import pdtsp.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddDeliveryRequestState implements State, IAddRequestState {
    private Address pickupAddress, deliveryAddress;
    private Pair<EnumAddressType, Request> addressBeforePickup;

    @Override
    public void run(Controller controller, Gui gui) {
        JOptionPane.showMessageDialog(gui.getFrame(),
                "Please choose a delivery address on the map.",
                "Delivery address choice",
                JOptionPane.INFORMATION_MESSAGE);

        this.deliveryAddress = null;
    }

    public void entryAction(Address pickupAddress, Pair<EnumAddressType, Request> addressBeforePickup) {
        this.pickupAddress = pickupAddress;
        this.addressBeforePickup = addressBeforePickup;
    }

    @Override
    public void addressClick(Controller controller, Gui gui, List<Pair<Double, Address>> addresses) {
        if(this.deliveryAddress != null) {
            return; // means we already selected an address, disable to avoid pollution when clicking on a request
        }

        Address selectedAddress = addresses.get(0).getY();

        if (addresses.size() > 1) {  // handle multiple addresses nearby
            selectedAddress = this.showAddressChoiceForm(controller, addresses);
            if(selectedAddress == null) {  // means the user cancelled the operation
                return;
            }
        }

        int option = JOptionPane.showConfirmDialog(
                gui.getFrame(),
                "You selected address n°" + selectedAddress.getId(),
                "Delivery address choice",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address
            this.deliveryAddress = selectedAddress;
            JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "Please select the request address it should be inserted after (choose the same as pickup to place it immediately after).",
                    "Preceding address choice",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    @Override
    public void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {
        if(this.deliveryAddress != null) {
            String message = this.addressBeforeMessage(new Pair<>(addressType, request));

            int option = JOptionPane.showConfirmDialog(
                    gui.getFrame(),
                    "You chose to add the delivery address directly after " + message + ".",
                    "Preceding address choice",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if(option == JOptionPane.OK_OPTION) {  // if user cancelled, allow them to select another address
                Pair<EnumAddressType, Request> predecessor = new Pair<>(addressType, request);

                ((RequestPopupDurationState) controller.requestDurationPopup).entryAction(
                        this.pickupAddress, this.addressBeforePickup, this.deliveryAddress, predecessor);
                controller.setCurrentState(controller.requestDurationPopup);
            }
        }
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
