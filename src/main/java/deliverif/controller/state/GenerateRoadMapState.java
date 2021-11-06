package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.exception.RequestsLoadException;
import deliverif.gui.Gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;


public class GenerateRoadMapState implements State {
    @Override
    public void run(Controller controller, Gui gui) {
        JFileChooser fileChooser = new JFileChooser(Path.of(System.getProperty("user.dir") + "/resources/roadmap").toString());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Roadmap Files", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Save roadmap");
        fileChooser.setSelectedFile(new File("roadmap.txt"));
        int option = fileChooser.showSaveDialog(gui.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.getRoadmap().WriteInFile(file);
        }
        controller.popState();
    }


}
