package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.controller.command.ListOfCommands;
import deliverif.gui.Gui;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import pdtsp.Pair;

import java.util.List;

public interface State {
    void run(Controller c, Gui gui);


    default void load() {
    }

    default void undo(ListOfCommands listOfCommands) {
    }

    default void redo(ListOfCommands listOfCommands) {
    }

    default void rightClick(Controller controller, Gui gui) {
    }

    default void leftClick(Controller controller, Gui gui) {
    }

    default void loadMapButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingMap);
    }

    default void loadRequestsButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.loadingRequests);
    }

    default void addRequestButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.addPickupRequest);
    }

    default void deleteButtonClick(Controller controller, Gui gui) {
        controller.setCurrentState(controller.chooseRequestToDelete);
    }

    default void computingTourButtonClick(Controller controller, Gui gui) {
    }

    default void continueComputationButtonClick(Controller controller, Gui gui) {
    }

    default void stopComputationButtonClick(Controller controller, Gui gui) {
    }

    default void optimalTourReached(Controller controller, Gui gui) {
    }

    default void generateRoadMapButtonClick(Controller controller, Gui gui) {
    }

    default void cancelButtonClick(Controller controller, Gui gui) {
    }

    default void addressClick(Controller controller, Gui gui, List<Pair<Double, Address>> addresses) {
    }

    default void requestClick(Controller controller, Gui gui, Request request, EnumAddressType addressType) {
    }

    default void validateDeleteRequestButtonClick(Controller controller, Gui gui) {
    }

    /** Visitor pattern accept() method */
    void accept(StateVisitor visitor);
}
