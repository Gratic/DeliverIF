package deliverif.controller;

import deliverif.model.CityMap;
import deliverif.gui.Gui;
import deliverif.controller.state.*;

public class Controller {
    
    private Gui gui;
    private CityMap cityMap;

    public State initState;
    public State loadState;
    protected State currentState;

    public Controller() {
        gui = new Gui(this);
        currentState = new InitState();
        cityMap = new CityMap();

        init();
    }
    
    public void init() {
        // states, solution 3
        initState = new InitState();
        currentState = initState;
        
        gui.init();
    }

    // state functions
    public void undo() {
        currentState.undo();
    }
    public void redo() {
        currentState.redo();
    }
    public void rightClick(Controller controller, Gui gui) {
        currentState.rightClick(this, gui);
    }
    public void leftClick(Controller controller, Gui gui) {
        currentState.leftClick(this, gui);
    }
    public void loadMapButtonClick(Gui gui) { currentState.buttonClick(this, gui); }

    // getters & setters
    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public CityMap getCityMap() {
        return cityMap;
    }
}
