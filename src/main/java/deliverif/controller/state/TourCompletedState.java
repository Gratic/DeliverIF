package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class TourCompletedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {

    }

    @Override
    public void addRequestButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.addPickupRequest);

    }

    @Override
    public void deleteButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.chooseRequestToDelete);
    }

    @Override
    public void addressClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.deleteRequest);

    }

    @Override
    public void generateRoadMapButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.generateRoadMap);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
