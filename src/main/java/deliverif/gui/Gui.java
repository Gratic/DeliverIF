package deliverif.gui;

import java.awt.*;
import java.awt.event.*;
import deliverif.controller.Controller;
import deliverif.gui.panel.ControlPanel;
import deliverif.gui.panel.GraphicalViewPanel;
import deliverif.gui.panel.MainPanel;
import deliverif.gui.panel.TextualViewPanel;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;
import deliverif.gui.viewstate.*;

import javax.swing.*;

public class Gui implements ActionListener {

    protected Controller controller;
    protected ViewState currentViewState;

    protected JFrame frame;

    protected ControlPanel controlPanel;
    protected GraphicalViewPanel graphicalViewPanel;
    protected TextualViewPanel textualViewPanel;

    protected int height = 960;
    protected int width = 1280;
    
    public Gui(Controller controller) {
        this.controller = controller;

        // create Frame and its MainPanel
        frame = new JFrame("Deliverif");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPanel());

        System.out.println(frame.getContentPane().getWidth());
        System.out.println(frame.getContentPane().getHeight());

        // create panels
        controlPanel = new ControlPanel(this);
        graphicalViewPanel = new GraphicalViewPanel();
        textualViewPanel = new TextualViewPanel();
        frame.getContentPane().add(
                controlPanel, BorderLayout.WEST
        );
        frame.getContentPane().add(
                graphicalViewPanel, BorderLayout.CENTER
        );
        frame.getContentPane().add(
                textualViewPanel, BorderLayout.EAST
        );

        //this.frame.pack(); // resize to fit components
        frame.setVisible(true);
    }

    public void init() {
        // load assets
        Assets.init();

        // set current ViewState
        this.currentViewState = new InitialView(this);

    }

    public void setCurrentViewState(ViewState currentViewState) {
        this.currentViewState = currentViewState;
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
    public JPanel getTextualViewPanel() {
        return textualViewPanel;
    }
    public JFrame getFrame() {
        return frame;
    }

}
