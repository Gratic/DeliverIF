package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.controller.command.ListOfCommands;
import deliverif.gui.Gui;
import deliverif.gui.viewstate.DisplayRequestsView;
import deliverif.gui.viewstate.ViewTourComputed;
import deliverif.model.Address;
import pdtsp.Pair;

import java.util.List;

public class TourCompletedState implements State {

    @Override
    public void run(Controller controller, Gui gui) {
        gui.getControlPanel().addRequestButton.setEnabled(true);
        gui.getControlPanel().deleteRequestButton.setEnabled(true);
        gui.setCurrentViewState(new ViewTourComputed(gui));
        gui.getMapView().setMapClickable(true);  // TODO will need to move this towards edit states, then put it to false here
    }


/*    @Override
    public void addressClick(Controller controller, Gui gui, List<Pair<Double, Address>> addresses) {
        controller.setCurrentState(controller.deleteRequest);
    }*/

    @Override
    public void generateRoadMapButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.generateRoadMap);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
