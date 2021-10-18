package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.model.CityMap;
import deliverif.view.View;
import deliverif.view.viewstate.InitialView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoadingRequests implements State{
    /**
     * Method that gets called when the user validates the paths given
     * at start of the app to load XML files.
     * @param controller
     * @param view
     */
    public void buttonClick(Controller controller, View view) {
        // get path to file
        // PROB: The actual class currentViewState instance is lost
        //String path = view.getCurrentViewState().getText();
        InitialView initialView = (InitialView)(view.getCurrentViewState());
        String requestsPath = initialView.getRequestsFilePath();

        // debug test
        System.out.println("-> InitState");
        // /home/onyr/Documents/4if/s1/pld_agile/testzone/hello_world.xml
        System.out.println("requests path: " + requestsPath);

        // load map
        ClassLoader classLoader = getClass().getClassLoader();
        File requestsFile = new File(requestsPath);

        // debug test: real all content of the file
        printAllLinesOfFile(requestsFile);

        // change to next state
        controller.setCurrentState(controller.requestsLoaded);
    }

    /**
     * This method is mainly here for debug purpose.
     * It displays to the terminal all the lines of the given file.
     * This ensure that the file is correctly loaded.
     * @param file
     */
    private void printAllLinesOfFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine = null;
            do {
                currentLine = reader.readLine();
                System.out.println(currentLine);
            } while(currentLine != null);
            reader.close();
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }

}
