package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class TourNotOptimalState implements State {
    @Override
    public void run(Controller controller, Gui gui) {

    }

    public void continueComputationButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.computingTour);
    }

    public void stopComputationButtonClick(Controller controller, Gui gui) {

        controller.setCurrentState(controller.tourCompleted);
    }

}
