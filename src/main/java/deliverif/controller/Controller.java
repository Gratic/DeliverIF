package deliverif.controller;

import deliverif.controller.command.ListOfCommands;
import deliverif.controller.state.*;
import deliverif.gui.Gui;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.controller.state.*;
import deliverif.roadmap.Roadmap;
import deliverif.model.*;
import pdtsp.Pair;

import java.util.List;
import java.util.Stack;

public class Controller {



    private final Gui gui;
    private final CityMap cityMap;
    private final DeliveryTour tour;

    public State initState;
    public State loadingMap;
    public State mapLoaded;
    public State loadingRequests;
    public State requestsLoaded;
    public State requestDurationPopup;
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

    private Roadmap roadmap;

    private ListOfCommands listOfCommands;

    private Stack<State> stateStack;

    public Controller() {
        listOfCommands = new ListOfCommands();

        cityMap = new CityMap();
        tour = new DeliveryTour();
        gui = new Gui(this);

        stateStack = new Stack<>();

        initState = new InitState();
        loadingMap = new LoadingMapState();
        mapLoaded = new MapLoadedState();
        loadingRequests = new LoadingRequestsState();
        requestsLoaded = new RequestsLoadedState();

        computingTour = new ComputingTourState();
        locallyModifyTour = new LocallyModifyTourState();
        tourCompleted = new TourCompletedState();
        generateRoadMap = new GenerateRoadMapState();
        tourNotOptimal = new TourNotOptimalState();
        addDeliveryRequest = new AddDeliveryRequestState();
        addPickupRequest = new AddPickupRequestState();
        chooseRequestToDelete = new ChooseRequestToDeleteState();
        chooseAssociatedRequest = new ChooseAssociatedRequestState();
        deleteRequest = new DeleteRequestState();
        requestDurationPopup = new RequestPopupDurationState();

        roadmap = new Roadmap(this);


        init();
    }

    public void init() {
        // states, solution 3

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
        this.gui.getBottomPanel().updateState(this.getCurrentState());
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

    public void generateRoadmap(Gui gui){
        this.getCurrentState().generateRoadMapButtonClick(this, gui);
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

    public void deleteButtonClick(Gui gui) {
        this.getCurrentState().deleteButtonClick(this, gui);
    }

    public void addressClick(Gui gui, List<Pair<Double, Address>> addresses) {
        this.getCurrentState().addressClick(this, gui, addresses);
    }

    public void requestClick(Gui gui, Request request, EnumAddressType addressType) {
        this.getCurrentState().requestClick(this, gui, request, addressType);
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

    public Roadmap getRoadmap() {
        return roadmap;
    }
}
