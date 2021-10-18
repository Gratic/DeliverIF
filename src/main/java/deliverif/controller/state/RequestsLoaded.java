package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;
import deliverif.gui.viewstate.LoadRequestsView;

public class RequestsLoaded implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.setCurrentViewState(new DisplayRequestsView(gui));
    }


}
