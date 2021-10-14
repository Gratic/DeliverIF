package deliverif.view;

import javax.swing.*;
import deliverif.controller.Controller;

import java.awt.event.*;
import deliverif.view.viewState.*;

public class View implements ActionListener {

    protected Controller controller;
    protected ViewState currentViewState;
    
    public View(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        this.currentViewState = new InitialView(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        this.controller.loadMapButtonClick(this);
    }

    // getters & setters
    public Controller getController() {
        return this.controller;
    }
    public ViewState getCurrentViewState() {
        return this.currentViewState;
    }
    
}
