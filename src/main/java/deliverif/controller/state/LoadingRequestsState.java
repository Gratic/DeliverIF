package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.exception.RequestsLoadException;
import deliverif.gui.Gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;

public class LoadingRequestsState implements State {
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

                controller.getTour().loadRequestsFromFile(file, controller.getCityMap());
                controller.setCurrentState(controller.requestsLoaded);
            } catch (RequestsLoadException exception) {
                exception.printStackTrace();

                // display error feedback in popup
                System.out.println("WARN: Error while loading requests!");
                JOptionPane.showMessageDialog(gui.getFrame(),
                        "Error: Problem while loading requests. " +
                                "Please make sure to select a well-formed XML requests file.",
                        "Load requests failed",
                        JOptionPane.WARNING_MESSAGE
                );

                run(controller, gui); // rerun current state. WARN: Run after popup!
            }
        } else if (option == JFileChooser.CANCEL_OPTION) {
            controller.popState();
        }
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
