package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class MapLoaded implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingRequests);
    }


}
