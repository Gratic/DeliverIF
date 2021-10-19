package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.CityMap;
import deliverif.model.Request;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

public class LoadingRequests implements State {
    /**
     * Method that gets called when the user validates the paths given
     * at start of the app to load XML files.
     */
    public void run(Controller controller, Gui gui) {
        JFileChooser fileChooser = new JFileChooser(Path.of(System.getProperty("user.dir") + "/resources/xml").toString());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XML Files", "xml");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Open requests file");
        int option = fileChooser.showOpenDialog(gui.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.getTour().loadRequestsFromFile(file, controller.getCityMap(), controller.getTour());
                controller.setCurrentState(controller.requestsLoaded);
            } catch (Exception e) {
                e.printStackTrace();
                run(controller, gui);
            }
        } else if (option == JFileChooser.CANCEL_OPTION) {
            State state = controller.getPreviousStates().pop();
            controller.setCurrentState(state);
        }
    }
}
