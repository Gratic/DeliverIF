package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.panel.GraphicalViewPanel;
import deliverif.gui.panel.TextualViewPanel;

import javax.swing.*;
import java.awt.*;

public class LoadRequestsView extends ViewState {

    public LoadRequestsView(Gui gui) {
        super(gui);

        createGuiComponents(); // create mapView there

        // add map to Center panel
        GraphicalViewPanel graphicalViewPanel = gui.getGraphicalViewPanel();
        graphicalViewPanel.removeAll();

        graphicalViewPanel.add(
                panel, BorderLayout.CENTER
        );

        graphicalViewPanel.revalidate(); // WARN: crucial after a removeAll()
        gui.getFrame().repaint();


        // add text to East panel
        TextualViewPanel textualViewPanel = gui.getTextualViewPanel();
        textualViewPanel.removeAll();

        JLabel pleaseLoadRequestsText = new JLabel("Please load requests.");
        textualViewPanel.add(pleaseLoadRequestsText);

        textualViewPanel.revalidate();
        textualViewPanel.repaint();
    }

    protected void createGuiComponents() {
        this.panel = this.gui.getMapView();
    }
}
