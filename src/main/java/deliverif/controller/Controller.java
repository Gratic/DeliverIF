package deliverif.controller;

import deliverif.model.CityMap;
import deliverif.gui.Gui;
import deliverif.model.DeliveryTour;
import deliverif.controller.state.*;

public class Controller {
    
    private Gui gui;
    private CityMap cityMap;
    private DeliveryTour tour;

    public State initState;
    public State loadingMap;
    public State mapLoaded;
    public State loadingRequests;
    public State requestsLoaded;
    protected State currentState;

    public Controller() {
        gui = new Gui(this);
        currentState = new InitState();
        loadingMap = new LoadingMap();
        mapLoaded = new MapLoaded();
        loadingRequests = new LoadingRequests();
        requestsLoaded = new RequestsLoaded();
        cityMap = new CityMap();
        tour = new DeliveryTour();


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
    public void buttonClick(Gui gui) { currentState.buttonClick(this, gui); }
    // getters & setters
    public void setCurrentState(State state) {
        this.currentState = state;
        currentState.run(this, gui);
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public DeliveryTour getTour() {
        return tour;
    }
}
