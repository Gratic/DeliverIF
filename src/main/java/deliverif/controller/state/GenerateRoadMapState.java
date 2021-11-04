package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;


public class GenerateRoadMapState implements State {
    @Override
    public void run(Controller controller, Gui gui) {
        //TODO: add generate roadmap comutation
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.tourCompleted);
    }


}
