package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;


import javax.swing.*;
import java.io.File;

public class InitState implements State {

    @Override
    public void run(Controller c, Gui gui) {

    }

    /**
     * Method that gets called when the user validates the paths given
     * at start of the app to load XML files.
     *
     * @param controller Controller of the application
     * @param gui       View of the application
     */


    public void buttonClick(Controller controller, Gui gui) {
        // change to next state
        controller.setCurrentState(controller.loadingMap);
    }
}
