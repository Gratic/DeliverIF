package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;


import javax.swing.*;
import java.io.File;

public class InitState implements State {

    @Override
    public void run(Controller c, Gui gui) {
        // disable all buttons except loadMapButton
        gui.getControlPanel().loadRequestsButton.setEnabled(false);
        gui.getControlPanel().addRequestButton.setEnabled(false);
        gui.getControlPanel().deleteRequestButton.setEnabled(false);
    }

    @Override
    public void loadMapButtonClick(Controller controller, Gui gui) {
        controller.getPreviousStates().push(this);
        controller.setCurrentState(controller.loadingMap);
    }
}
