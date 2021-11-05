package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

import javax.swing.*;

public class TourNotOptimalState implements State {
    @Override
    public void run(Controller controller, Gui gui) {
        String[] options = {"Yes, find a better tour", "No. This tour is acceptable."};
        int result = JOptionPane.showOptionDialog(
                gui.getFrame(),
                "The tour is not optimal. Do you want to continue the computation ?",
                "Swing Tester",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //no custom icon
                options,  //button titles
                options[1]); //default button

        if (result == JOptionPane.YES_OPTION) {
            //TODO continuer le calcul du tour
        } else if (result == JOptionPane.NO_OPTION) {
            //TODO aller dans l'état tour completed

        }
    }

    public void continueComputationButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.computingTour);
    }

    public void stopComputationButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.tourCompleted);
    }

}
