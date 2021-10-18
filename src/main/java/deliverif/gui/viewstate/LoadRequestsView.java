package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.panel.GraphicalViewPanel;
import deliverif.gui.panel.TextualViewPanel;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private void createGuiComponents() {
        // create mapView
        JPanel mapView = new JPanel();
        mapView.setBackground(Color.cyan);
        //mapView.setPreferredSize(new Dimension(300, 200));
        JLabel testMapText = new JLabel("Map");
        testMapText.setPreferredSize(
                new Dimension(100, 100)
        );
        mapView.add(testMapText);

        this.panel = mapView;
    }
}
