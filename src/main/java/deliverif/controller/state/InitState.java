package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.model.CityMap;
import deliverif.view.View;
import deliverif.view.viewState.*;

import java.io.File;

public class InitState implements State {
    
    public InitState() {

    }

    public void loadMapButtonClick(Controller controller, View view) {
        // get path to file
        // TODO: Implement Visitor design pattern
        //String path = view.getCurrentViewState().ge

        // load map
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());

        // tells the model to load its data
        CityMap cityMap = controller.getCityMap();
        cityMap.loadMapFromFile(file);

        // debug test
        System.out.println("Loaded !!!");

        // change to next state
        controller.setCurrentState(controller.loadState);
    }
    
}
