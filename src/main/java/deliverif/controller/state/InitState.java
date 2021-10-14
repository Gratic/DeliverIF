package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.model.CityMap;
import deliverif.view.View;

import java.io.File;

public class InitState implements State {
    
    public InitState() {

    }

    public void loadMapButtonClick(Controller controller, View view) {
        // get path to file
        String path = view.filePathField.getText();

        // load map
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());

        // tells the model to load its data
        CityMap cityMap = controller.getCityMap();
        cityMap.loadMapFromFile(file);

        // change to next state
        controller.setCurrentState(controller.loadState);
    }
    
}
