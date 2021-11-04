package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class LocallyModifyTourState implements State{
    @Override
    public void run(Controller controller, Gui gui) {
        //local re-computation done
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.tourCompleted);
    }
}
