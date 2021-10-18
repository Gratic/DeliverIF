package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.view.View;

public class MapLoaded implements State {
    public void buttonClick(Controller controller, View view) {
        // change to next state
        controller.setCurrentState(controller.loadingRequests);
    }
}
