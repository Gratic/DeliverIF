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

public class ViewTourComputed extends ViewState implements ActionListener {

    private final JButton generateRoadmapButton;

    public ViewTourComputed(Gui gui) {
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
        ////
        /*
        TextualViewPanel textualViewPanel = gui.getTextualViewPanel();
        textualViewPanel.removeAll();

        JLabel pleaseLoadRequestsText = new JLabel("Compute a tour");
        textualViewPanel.add(pleaseLoadRequestsText);
        JButton computeButton = new JButton("Compute");
        textualViewPanel.add(computeButton);
        textualViewPanel.add(
                this.gui.getRequestsTextView()
        );

        textualViewPanel.revalidate();
        textualViewPanel.repaint();
        */

        TextualViewPanel textualViewPanel = gui.getTextualViewPanel();
        textualViewPanel.removeAll();
        JLabel generateRoadmap = new JLabel("Generate the roadmap");
        textualViewPanel.add(generateRoadmap);
        generateRoadmapButton = new JButton("Generate");
        textualViewPanel.add(generateRoadmapButton);
        generateRoadmapButton.addActionListener(this);

        //TextualViewScrollPanel textualViewScrollPanel = new TextualViewScrollPanel();
        this.gui.getRequestsTextView().init();
        textualViewPanel.add(
                //textualViewScrollPanel
                this.gui.getRequestsTextView()
        );

        textualViewPanel.revalidate();
        textualViewPanel.repaint();
        ////
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.generateRoadmapButton) {
            this.gui.getController().generateRoadMapButtonClick(gui);
        }
    }
}
