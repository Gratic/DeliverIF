package deliverif.controller;

import deliverif.controller.command.ListOfCommands;
import deliverif.model.CityMap;
import deliverif.gui.Gui;
import deliverif.model.DeliveryTour;
import deliverif.controller.state.*;

import java.util.Stack;

public class Controller {

    private ListOfCommands listOfCommands;

    private Gui gui;
    private CityMap cityMap;
    private DeliveryTour tour;

    public State initState;
    public State loadingMap;
    public State mapLoaded;
    public State loadingRequests;
    public State requestsLoaded;
    public State computingTour;
    public State tourCompleted;
    public State generateRoadMap;
    public State tourNotOptimal;
    public State chooseRequestToDelete;
    public State locallyModifyTour;
    public State chooseAssociatedRequest;
    public State addDeliveryRequest;
    public State addPickupRequest;
    public State deleteRequest;
    /*public State locallyModifyTour;
    public State chooseAssociatedRequest;*/
    protected State currentState;
    protected Stack<State> previousStates;

    public Controller() {
        listOfCommands = new ListOfCommands();

        cityMap = new CityMap();
        tour = new DeliveryTour();
        gui = new Gui(this);

        currentState = new InitState();
        previousStates = new Stack<State>();

        loadingMap = new LoadingMapState();
        mapLoaded = new MapLoadedState();
        loadingRequests = new LoadingRequestsState();
        requestsLoaded = new RequestsLoadedState();

        computingTour= (State) new ComputingTourState();
        locallyModifyTour=(State) new LocallyModifyTourState();
        tourCompleted= (State) new TourCompletedState();
        generateRoadMap= (State) new GenerateRoadMapState();
        tourNotOptimal= (State) new TourNotOptimalState();
        addDeliveryRequest= (State) new AddDeliveryRequestState();
        addPickupRequest= (State) new AddPickupRequestState();
        chooseRequestToDelete= (State) new ChooseRequestToDeleteState();
        chooseAssociatedRequest= (State) new ChooseAssociatedRequestState();
        deleteRequest= (State) new DeleteRequestState();

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
        currentState.undo(listOfCommands);
    }
    public void redo() {
        currentState.redo(listOfCommands);
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

    public void computingTourButtonClick(Gui gui){
        this.currentState.computingTourButtonClick(this, gui);
    }
    public void continueComputationButtonClick(Gui gui) {
        this.currentState.continueComputationButtonClick(this, gui);
    }
    public void stopComputationButtonClick(Gui gui) {
        this.currentState.stopComputationButtonClick(this, gui);
    }
    public void optimalTourReached(Gui gui) {
        this.currentState.optimalTourReached(this, gui);
    }
    public void generateRoadMapButtonClick(Gui gui) {
        this.currentState.generateRoadMapButtonClick(this, gui);
    }
    public void addRequestButtonClick(Gui gui) {
        this.currentState.addRequestButtonClick(this, gui);
    }
    public void cancelButtonClick(Gui gui) {
        this.currentState.cancelButtonClick(this, gui);
    }
    public void addPickupButtonClick(Gui gui) {
        this.currentState.addPickupButtonClick(this, gui);
    }
    public void addDeliveryButtonClick(Gui gui) {
        this.currentState.addDeliveryButtonClick(this, gui);
    }
    public void validateDeliveryButtonClick(Gui gui) {
        this.currentState.addDeliveryButtonClick(this, gui);
    }
    public void deleteButtonClick(Gui gui) {
        this.currentState.deleteButtonClick(this, gui);
    }
    public void addressClick(Gui gui) {
        this.currentState.addressClick(this, gui);
    }
    public void requestClick(Gui gui) {
        this.currentState.requestClick(this, gui);
    }
    public void deleteRequestButton(Gui gui) {
        this.currentState.validateDeleteRequestButtonClick(this, gui);
    }
}
