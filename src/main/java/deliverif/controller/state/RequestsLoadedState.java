package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;


public class RequestsLoadedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.getControlPanel().addRequestButton.setEnabled(true);
        gui.getControlPanel().deleteRequestButton.setEnabled(true);
        gui.setCurrentViewState(new DisplayRequestsView(gui));
    }

    @Override
    public void loadRequestsButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingRequests);
    }

    @Override
    public void loadMapButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingMap);
    }

    @Override
    public void computingTourButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.computingTour);
    }
}
