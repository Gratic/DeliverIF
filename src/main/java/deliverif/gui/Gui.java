package deliverif.gui;

import deliverif.controller.Controller;
import deliverif.gui.mapview.MapView;
import deliverif.gui.panel.*;
import deliverif.gui.textualview.RequestsTextView;
import deliverif.gui.utils.Assets;
import deliverif.gui.viewstate.InitialView;
import deliverif.gui.viewstate.ViewState;

import javax.swing.*;
import java.awt.*;

public class Gui {

    protected Controller controller;
    protected ViewState currentViewState;

    protected JFrame frame;

    protected ControlPanel controlPanel;
    protected GraphicalViewPanel graphicalViewPanel;
    protected TextualViewPanel textualViewPanel;
    protected BottomPanel bottomPanel;

    private final RequestsTextView requestsTextView;

    protected MapView mapView;

    protected int height = 960;
    protected int width = 1280;

    public Gui(Controller controller) {
        this.controller = controller;

        // create Frame and its MainPanel
        frame = new JFrame("Deliverif");
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPanel());

        System.out.println(frame.getContentPane().getWidth());
        System.out.println(frame.getContentPane().getHeight());

        // create panels
        controlPanel = new ControlPanel(this);
        graphicalViewPanel = new GraphicalViewPanel();
        textualViewPanel = new TextualViewPanel();
        bottomPanel = new BottomPanel(this);
        frame.getContentPane().add(
                controlPanel, BorderLayout.WEST
        );
        frame.getContentPane().add(
                graphicalViewPanel, BorderLayout.CENTER
        );
        frame.getContentPane().add(
                textualViewPanel, BorderLayout.EAST
        );
        frame.getContentPane().add(
                bottomPanel, BorderLayout.SOUTH
        );

        mapView = new MapView(controller);

        //this.requestsTextView = new RequestsTextView(controller.getTour());
        this.requestsTextView = new RequestsTextView(controller.getTour(), controller.getCityMap());

        //this.frame.pack(); // resize to fit components
        frame.setVisible(true);
    }

    public void init() {
        // load assets
        frame.setIconImage(Assets.logoImage);

        // set current ViewState
        this.currentViewState = new InitialView(this);


    }

    public void setCurrentViewState(ViewState currentViewState) {
        this.currentViewState = currentViewState;
    }

    // getters & setters
    public Controller getController() {
        return this.controller;
    }

    public ViewState getCurrentViewState() {
        return this.currentViewState;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public GraphicalViewPanel getGraphicalViewPanel() {
        return graphicalViewPanel;
    }

    public TextualViewPanel getTextualViewPanel() {
        return textualViewPanel;
    }

    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public MapView getMapView() {
        return mapView;
    }

    public RequestsTextView getRequestsTextView() {
        return requestsTextView;
    }
}
