package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.*;

public class MapLoaded implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.setCurrentViewState(new LoadRequestsView(gui));
    }

    @Override
    public void buttonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingRequests);
    }
}
