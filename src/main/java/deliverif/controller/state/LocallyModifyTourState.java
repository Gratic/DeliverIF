package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class LocallyModifyTourState implements State {
    @Override
    public void run(Controller controller, Gui gui) {
        //local re-computation done
        controller.setCurrentState(controller.tourCompleted);
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
