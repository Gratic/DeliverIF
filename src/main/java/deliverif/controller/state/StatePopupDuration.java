package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

import javax.swing.*;

public class StatePopupDuration implements State {

    /*code pour la popup pris sur https://www.it-swarm-fr.com/fr/java/comment-creer-une-fenetre-popup-dans-java/940931292/*/

    public void run(Controller controller, Gui gui) {

        JTextField pickupDurationField = new JTextField("300");
        JTextField deliveryDurationField = new JTextField("300");

        Object[] message = {
                "Enter pickup duration :", pickupDurationField,
                "Enter delivery duration :", deliveryDurationField,
        };
        int option = JOptionPane.showConfirmDialog(gui.getFrame(), message,
                "Pickup and delivery durations", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String pickupDuration = pickupDurationField.getText();
            String deliveryDuration = deliveryDurationField.getText();
            controller.setCurrentState(controller.locallyModifyTour);
        } else if (option == JOptionPane.CANCEL_OPTION) {
            controller.setCurrentState(controller.tourCompleted);

        }
    }

}
