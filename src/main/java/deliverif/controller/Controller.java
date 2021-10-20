package deliverif.controller;

import deliverif.model.CityMap;
import deliverif.gui.Gui;
import deliverif.model.DeliveryTour;
import deliverif.controller.state.*;

import java.util.Stack;

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
    protected Stack<State> previousStates;

    public Controller() {
        cityMap = new CityMap();
        tour = new DeliveryTour();

        gui = new Gui(this);

        currentState = new InitState();
        previousStates = new Stack<State>();

        loadingMap = new LoadingMap();
        mapLoaded = new MapLoaded();
        loadingRequests = new LoadingRequests();
        requestsLoaded = new RequestsLoaded();

        init();
    }
    
    public void init() {
        // states, solution 3
        initState = new InitState();
        setCurrentState(initState);
        
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
    // getters & setters
    public void setCurrentState(State state) {
        this.currentState = state;
        currentState.run(this, gui);
    }

    public Stack<State> getPreviousStates() {
        return previousStates;
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public DeliveryTour getTour() {
        return tour;
    }

    public void loadMapButtonClick(Gui gui) {
        this.currentState.loadMapButtonClick(this, gui);
    }

    public void loadRequestsButtonClick(Gui gui) {
        this.currentState.loadRequestsButtonClick(this, gui);
    }

}
