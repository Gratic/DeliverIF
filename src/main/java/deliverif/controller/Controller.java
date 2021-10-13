package deliverif.controller;

import deliverif.view.View;
import deliverif.controller.state.*;

public class Controller {
    
    private View view;
    private State currentState;

    protected State initState;
    
    public Controller() {
        view = new View();
        init();
    }
    
    public void init() {
        currentState = new InitState();

        // states, solution 3
        initState = new InitState();
        
        view.init();
    }

    // state functions
    public void load() {
        currentState.load();
    };
    public void undo() {
        currentState.undo();
    };
    public void redo() {
        currentState.redo();
    };
    public void rightClick(Controller controller, View view) {
        currentState.rightClick(this, view);
    }
    public void leftClick(Controller controller, View view) {
        currentState.leftClick(this, view);
    }
    
    
    
}
