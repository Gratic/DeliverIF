package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class ComputingTourState implements State {
    private boolean optimal;
    private final int TIMELIMIT = 120;

    @Override
    public void run(Controller controller, Gui gui) {
        optimal = false;
        // Start calculation

        try {
            wait(TIMELIMIT);
            computingTourButtonClick(controller, gui);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void computingTourButtonClick(Controller controller, Gui gui) {
        //Interrupt computing


        if (!optimal) {
            //boolean optimal= retour de l'appel a l'algo;
            controller.setCurrentState(controller.tourNotOptimal);
        } else {
            //boolean optimal= retour de l'appel a l'algo;
            controller.setCurrentState(controller.tourCompleted);
        }

    }

    // Call this once optimal result is reached before the timelimit that takes you to tourNotOptimal
    public void optimalTourReached(Controller controller, Gui gui) {
        //go to tour completed
        optimal = true;
        computingTourButtonClick(controller, gui);

    }


}
