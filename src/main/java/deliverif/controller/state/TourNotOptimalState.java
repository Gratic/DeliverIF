package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;

public class TourNotOptimalState implements State{
    @Override
    public void run(Controller controller, Gui gui) {

    }

    public void continueComputationButtonClick(Controller controller, Gui gui){
        State.super.continueComputationButtonClick(controller, gui);
        //call computing tour method
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.computingTour);
    }

    public void stopComputationButtonClick(Controller controller, Gui gui){
        State.super.stopComputationButtonClick(controller, gui);
        //go to tour completed
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.tourCompleted);
    }

}
