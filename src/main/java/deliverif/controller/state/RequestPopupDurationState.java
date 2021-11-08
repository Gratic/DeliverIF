package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import pdtsp.Pair;

import javax.swing.*;

public class RequestPopupDurationState implements State, IAddRequestState {
    private Address pickupAddress, deliveryAddress;
    private Pair<EnumAddressType, Request> addressBeforePickup, addressBeforeDelivery;

    /*code pour la popup pris sur https://www.it-swarm-fr.com/fr/java/comment-creer-une-fenetre-popup-dans-java/940931292/*/

    public void run(Controller controller, Gui gui) {

        JTextField pickupDurationField = new JTextField("300");
        JTextField deliveryDurationField = new JTextField("300");

        Object[] message = {
                "Enter pickup duration :", pickupDurationField,
                "Enter delivery duration :", deliveryDurationField,
        };
        boolean keepTrying = true;

        while(keepTrying) {
            JOptionPane.showMessageDialog(gui.getFrame(), message,
                    "Choose pickup and delivery durations", JOptionPane.INFORMATION_MESSAGE);

            String pickupDurationStr = pickupDurationField.getText();
            String deliveryDurationStr = deliveryDurationField.getText();

            try {
                int pickupDuration = Integer.parseInt(pickupDurationStr);
                int deliveryDuration = Integer.parseInt(deliveryDurationStr);

                if(pickupDuration < 0 || deliveryDuration < 0) throw new Exception(); // to be caught immediately after

                int option = this.showConfirmDialog(gui, pickupDuration, deliveryDuration);
                
                if(option == JOptionPane.OK_OPTION) {
                    controller.getTour().addRequest(new Request(
                            this.pickupAddress,
                            this.deliveryAddress,
                            pickupDuration,
                            deliveryDuration
                    ));

                    JOptionPane.showMessageDialog(gui.getFrame(), "Request added. The system will now recompute the tour.");

                    // TODO add call to recompute here

                    controller.setCurrentState(controller.tourCompleted);
                    return;
                }
                else if (option == JOptionPane.CANCEL_OPTION) {
                    keepTrying = false;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(gui.getFrame(), "An error occurred: invalid values. Please retry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // user cancelled whole operation
        JOptionPane.showMessageDialog(gui.getFrame(), "Operation aborted.", "Operation aborted", JOptionPane.WARNING_MESSAGE);
        controller.setCurrentState(controller.tourCompleted);
    }


    public void entryAction(Address pickupAddress, Pair<EnumAddressType, Request> addressBeforePickup,
                            Address deliveryAddress, Pair<EnumAddressType, Request> addressBeforeDelivery) {
        this.pickupAddress = pickupAddress;
        this.addressBeforePickup = addressBeforePickup;
        this.deliveryAddress = deliveryAddress;
        this.addressBeforeDelivery = addressBeforeDelivery;
    }

    private int showConfirmDialog(Gui gui, int pickupDuration, int deliveryDuration) {
        return JOptionPane.showConfirmDialog(
                gui.getFrame(),
                "<html>The system will now add a request with the specified parameters:<br>"
                        + "<ul>"
                        + "<li>Request pickup address is @" + this.pickupAddress.getId() + "</li>"
                        + "<li>Request delivery address is @"+ this.deliveryAddress.getId() +"</li>"
                        + "<li>Request pickup address is added after " + this.addressBeforeMessage(this.addressBeforePickup) + "</li>"
                        + "<li>Request delivery address is added after " + this.addressBeforeMessage(this.addressBeforeDelivery) + "</li>"
                        + "<li>Request pickup duration is " + pickupDuration + " seconds</li>"
                        + "<li>Request delivery duration is " + deliveryDuration + " seconds</li>"
                        + "</ul>"
                        +"</html>",
                "Confirm your choices",
                JOptionPane.OK_CANCEL_OPTION
        );
    }
}
