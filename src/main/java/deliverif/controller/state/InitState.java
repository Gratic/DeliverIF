package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;


public class InitState implements State {
    
    public InitState() {

    }

    /**
     * Method that gets called when the user validates the paths given
     * at start of the app to load XML files.
     * @param controller
     * @param gui
     */


    public void buttonClick(Controller controller, Gui gui) {
        // change to next state
        controller.setCurrentState(controller.loadingMap);
    }
}
