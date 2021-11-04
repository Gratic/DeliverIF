package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MapLoadedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.setCurrentViewState(new LoadRequestsView(gui));
    }

    @Override
    public void loadRequestsButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.loadingRequests);
    }

    @Override
    public void loadMapButtonClick(Controller controller, Gui gui) {
        State state = controller.getPreviousStates().pop();
        controller.setCurrentState(state);
    }
}
