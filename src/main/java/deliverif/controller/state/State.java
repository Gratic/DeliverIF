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
    public default void buttonClick(Controller controller, Gui gui) {}
}
