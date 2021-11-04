package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public interface State {
    public void run(Controller c, Gui gui);
    public default void load() {};
    public default void undo() {};
    public default void redo() {};
    public default void rightClick(Controller controller, Gui gui) {}
    public default void leftClick(Controller controller, Gui gui) {}

    public default void loadMapButtonClick(Controller controller, Gui gui) {}
    public default void loadRequestsButtonClick(Controller controller, Gui gui) {}
    public default void computingTourButtonClick(Controller controller, Gui gui){}
    public default void continueComputationButtonClick(Controller controller, Gui gui) {}
    public default void stopComputationButtonClick(Controller controller, Gui gui) {}
    public default void optimalTourReached(Controller controller, Gui gui) {}
    public default void generateRoadMapButtonClick(Controller controller, Gui gui) {}
    public default void addRequestButtonClick(Controller controller, Gui gui) {}
    public default void cancelButtonClick(Controller controller, Gui gui) {}
    public default void addPickupButtonClick(Controller controller, Gui gui) {}
    public default void addDeliveryButtonClick(Controller controller, Gui gui) {}
    public default void validateDeliveryButtonClick(Controller controller, Gui gui) {}
    public default void deleteButtonClick(Controller controller, Gui gui) {}
    public default void addressClick(Controller controller, Gui gui) {}
    public default void requestClick(Controller controller, Gui gui) {}
    public default void validateDeleteRequestButtonClick(Controller controller, Gui gui) {}





}
