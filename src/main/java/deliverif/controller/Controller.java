package deliverif.controller;

import deliverif.model.CityMap;
import deliverif.view.View;
import deliverif.controller.state.*;

public class Controller {
    
    private View view;
    private State currentState;
    private CityMap cityMap;

    protected State initState;
    
    public Controller() {
        view = new View();
        currentState = new InitState();
        cityMap = new CityMap();

        init();
    }
    
    public void init() {
        // states, solution 3
        initState = new InitState();
        
        view.init();
    }

    // state functions
    public void load() {
        currentState.load();
    }
    public void undo() {
        currentState.undo();
    }
    public void redo() {
        currentState.redo();
    }
    public void rightClick(Controller controller, View view) {
        currentState.rightClick(this, view);
    }
    public void leftClick(Controller controller, View view) {
        currentState.leftClick(this, view);
    }

}
