package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.LoadRequestsView;

public class MapLoadedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.getControlPanel().loadRequestsButton.setEnabled(true);
        gui.setCurrentViewState(new LoadRequestsView(gui));
    }
}