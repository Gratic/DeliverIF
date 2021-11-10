package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.panel.GraphicalViewPanel;
import deliverif.gui.panel.TextualViewPanel;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;
import deliverif.model.DeliveryTour;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayRequestsView extends ViewState implements ActionListener, Observer {

    private final JButton computeButton;
    private TextualViewPanel textualViewPanel;
    private JLabel notOrdered;
    public DisplayRequestsView(Gui gui) {
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

        gui.getController().getTour().addObserver(this);


        // add text to East panel
        ////

        textualViewPanel = gui.getTextualViewPanel();
        textualViewPanel.removeAll();
        JLabel pleaseLoadRequestsText = new JLabel("Compute a tour");
        textualViewPanel.add(pleaseLoadRequestsText);
        computeButton = new JButton("Compute");
        textualViewPanel.add(computeButton);
        computeButton.setFocusPainted(false);
        computeButton.setContentAreaFilled(false);
        computeButton.addActionListener(this);

        //TextualViewScrollPanel textualViewScrollPanel = new TextualViewScrollPanel();
        if (gui.getController().getTour().getPath().size()<2){
            notOrdered = new JLabel("Request not yet ordered (Please compute a tour)");
            textualViewPanel.add(notOrdered);
        }
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
        if (e.getSource() == this.computeButton) {
            this.gui.getController().computingTourButtonClick(gui);
        }
    }

    @Override
    public void update(Observable observed, Object arg) {
        if (((DeliveryTour)observed).getPath().size()>=2){
            textualViewPanel.remove(notOrdered);
            textualViewPanel.revalidate();
            textualViewPanel.repaint();
        }
    }

    public JButton getComputeButton() {
        return computeButton;
    }
}
