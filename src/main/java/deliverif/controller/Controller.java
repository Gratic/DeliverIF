package deliverif.controller;

import deliverif.controller.command.ListOfCommands;
import deliverif.controller.state.*;
import deliverif.gui.Gui;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;

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

    protected Stack<State> stateStack;

    public Controller() {
        listOfCommands = new ListOfCommands();

        cityMap = new CityMap();
        tour = new DeliveryTour();
        gui = new Gui(this);

        stateStack = new Stack<State>();

        loadingMap = new LoadingMapState();
        mapLoaded = new MapLoadedState();
        loadingRequests = new LoadingRequestsState();
        requestsLoaded = new RequestsLoadedState();

        computingTour = (State) new ComputingTourState();
        locallyModifyTour = (State) new LocallyModifyTourState();
        tourCompleted = (State) new TourCompletedState();
        generateRoadMap = (State) new GenerateRoadMapState();
        tourNotOptimal = (State) new TourNotOptimalState();
        addDeliveryRequest = (State) new AddDeliveryRequestState();
        addPickupRequest = (State) new AddPickupRequestState();
        chooseRequestToDelete = (State) new ChooseRequestToDeleteState();
        chooseAssociatedRequest = (State) new ChooseAssociatedRequestState();
        deleteRequest = (State) new DeleteRequestState();

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
        this.getCurrentState().undo(listOfCommands);
    }

    public void redo() {
        this.getCurrentState().redo(listOfCommands);
    }

    public void rightClick(Controller controller, Gui gui) {
        this.getCurrentState().rightClick(this, gui);
    }

    public void leftClick(Controller controller, Gui gui) {
        this.getCurrentState().leftClick(this, gui);
    }

    // getters & setters
    public void setCurrentState(State state) {
        this.stateStack.push(state);
        this.getCurrentState().run(this, gui);
    }


    public Stack<State> getStateStack() {
        return stateStack;
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public DeliveryTour getTour() {
        return tour;
    }

    public void loadMapButtonClick(Gui gui) {
        this.getCurrentState().loadMapButtonClick(this, gui);
    }

    public void loadRequestsButtonClick(Gui gui) {
        this.getCurrentState().loadRequestsButtonClick(this, gui);
    }

    public void computingTourButtonClick(Gui gui) {
        this.getCurrentState().computingTourButtonClick(this, gui);
    }

    public void continueComputationButtonClick(Gui gui) {
        this.getCurrentState().continueComputationButtonClick(this, gui);
    }

    public void stopComputationButtonClick(Gui gui) {
        this.getCurrentState().stopComputationButtonClick(this, gui);
    }

    public void optimalTourReached(Gui gui) {
        this.getCurrentState().optimalTourReached(this, gui);
    }

    public void generateRoadMapButtonClick(Gui gui) {
        this.getCurrentState().generateRoadMapButtonClick(this, gui);
    }

    public void addRequestButtonClick(Gui gui) {
        this.getCurrentState().addRequestButtonClick(this, gui);
    }

    public void cancelButtonClick(Gui gui) {
        this.getCurrentState().cancelButtonClick(this, gui);
    }

    public void addPickupButtonClick(Gui gui) {
        this.getCurrentState().addPickupButtonClick(this, gui);
    }

    public void addDeliveryButtonClick(Gui gui) {
        this.getCurrentState().addDeliveryButtonClick(this, gui);
    }

    public void validateDeliveryButtonClick(Gui gui) {
        this.getCurrentState().addDeliveryButtonClick(this, gui);
    }

    public void deleteButtonClick(Gui gui) {
        this.getCurrentState().deleteButtonClick(this, gui);
    }

    public void addressClick(Gui gui, boolean overlap) {
        this.getCurrentState().addressClick(this, gui, overlap);
    }

    public void requestClick(Gui gui) {
        this.getCurrentState().requestClick(this, gui);
    }

    public void deleteRequestButton(Gui gui) {
        this.getCurrentState().validateDeleteRequestButtonClick(this, gui);
    }

    public State getCurrentState() {
        return stateStack.peek();
    }

    public void popStates(int nb) {

        for (int i = 0; i < nb; i++) {
            this.stateStack.pop();
        }
        this.getCurrentState().run(this, gui);
    }

    public void popState() {
        popStates(1);
    }

    public Gui getGui() {
        return gui;
    }
}
