package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;

public class LoadingMap implements State {
    /**
     * Method that gets called when the user validates the paths given
     * at start of the app to load XML files.
     */
    public void run(Controller controller, Gui gui) {
        JFileChooser fileChooser = new JFileChooser(Path.of(System.getProperty("user.dir") + "/resources/xml").toString());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XML Files", "xml");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Open map file");
        int option = fileChooser.showOpenDialog(gui.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.getTour().getRequests().clear();
                controller.getTour().notifyObservers(controller.getTour());
                controller.getCityMap().loadMapFromFile(file);
                controller.setCurrentState(controller.mapLoaded);
            } catch (SAXException saxeException) {

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
