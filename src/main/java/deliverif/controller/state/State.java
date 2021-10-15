package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.view.View;

public interface State {
    public default void load() {};
    public default void undo() {};
    public default void redo() {};
    public default void rightClick(Controller controller, View view) {}
    public default void leftClick(Controller controller, View view) {}
    public default void buttonClick(Controller controller, View view) {}
}
