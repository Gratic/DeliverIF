package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;

public class InitState implements State {

    @Override
    public void run(Controller c, Gui gui) {

    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
