package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MapLoaded implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.setCurrentViewState(new LoadRequestsView(gui));
    }
}
