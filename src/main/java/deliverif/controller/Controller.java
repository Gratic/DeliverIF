package deliverif.controller;

import deliverif.model.CityMap;
import deliverif.view.View;
import deliverif.controller.state.*;

public class Controller {
    
    private View view;
    private CityMap cityMap;

    public State initState;
    public State loadState;
    protected State currentState;

    public Controller() {
        view = new View(this);
        currentState = new InitState();
        cityMap = new CityMap();

        init();
    }
    
    public void init() {
        // states, solution 3
        initState = new InitState();
        currentState = initState;
        
        view.init();
    }

    // state functions
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
    public void loadMapButtonClick(View view) { currentState.loadMapButtonClick(this, view); }

    // getters & setters
    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public CityMap getCityMap() {
        return cityMap;
    }
}
