package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;


public class RequestsLoadedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.getControlPanel().addRequestButton.setEnabled(false);
        gui.getControlPanel().deleteRequestButton.setEnabled(false);
        gui.setCurrentViewState(new DisplayRequestsView(gui));
        controller.getListOfCommands().clear();
    }

    @Override
    public void computingTourButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.computingTour);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
