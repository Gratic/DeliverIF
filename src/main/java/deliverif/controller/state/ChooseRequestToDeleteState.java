package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class ChooseRequestToDeleteState implements State{


        @Override
        public void run(Controller controller, Gui gui) {

        }

        @Override
        public void addressClick(Controller controller, Gui gui) {
            //TODO: Find a way to detect overlapping points
            //case delete request
            controller.getPreviousStates().push(this);
            controller.setCurrentState(controller.deleteRequest);
            //case choose associated request
            controller.getPreviousStates().push(this);
            controller.setCurrentState(controller.chooseAssociatedRequest);
        }
    public void cancelButtonClick(Controller controller, Gui gui) {
        State state = controller.getPreviousStates().pop();
        controller.setCurrentState(state);
    }
}
