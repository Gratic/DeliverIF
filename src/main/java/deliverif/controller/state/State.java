package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.controller.command.ListOfCommands;
import deliverif.gui.Gui;

public interface State {
    public void run(Controller c, Gui gui);
    public default void load() {};
    public default void undo(ListOfCommands listOfCommands) {};
    public default void redo(ListOfCommands listOfCommands) {};
    public default void rightClick(Controller controller, Gui gui) {}
    public default void leftClick(Controller controller, Gui gui) {}

    public default void loadMapButtonClick(Controller controller, Gui gui) {}
    public default void loadRequestsButtonClick(Controller controller, Gui gui) {}

    public default void addRequestButtonClick(Controller controller, Gui gui) {}
    public default void deleteButtonClick(Controller controller, Gui gui) {}
}
