package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class ComputingTourState implements State{
    @Override
    public void run(Controller controller, Gui gui) {
        // at this point, the tour has already been completed
    }

    @Override
    public void computingTourButtonClick(Controller controller, Gui gui) {
        State.super.computingTourButtonClick(controller, gui);
        //call computing tour method

        boolean optimal=true; // TODO: branch code with computing tour algorithm
        if (!optimal){
            //boolean optimal= retour de l'appel a l'algo;
            controller.getPreviousStates().push(this);
            controller.setCurrentState(controller.tourNotOptimal);}
        if (optimal){
            //boolean optimal= retour de l'appel a l'algo;
            controller.getPreviousStates().push(this);
            controller.setCurrentState(controller.tourCompleted);}

    }

    // Call this once optimal result is reached before the timelimit that takes you to tourNotOptimal
    public void optimalTourReached(Controller controller, Gui gui){
        //go to tour completed
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.tourCompleted);
    }




}
