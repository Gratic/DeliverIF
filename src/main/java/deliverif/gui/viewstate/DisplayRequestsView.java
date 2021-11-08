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

        textualViewPanel = gui.getTextualViewPanel();
        textualViewPanel.removeAll();
        JLabel pleaseLoadRequestsText = new JLabel("Compute a tour");
        textualViewPanel.add(pleaseLoadRequestsText);
        computeButton = new JButton("Compute");
        textualViewPanel.add(computeButton);
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

    private void createGuiComponents() {
        // base panel
        panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(ColorTheme.LIGHT_BASE_GREY);
        panel.setLocation(50, 50);
        panel.setLayout(null);

        // inner JPanel to be centered
        JPanel innerPanel = new JPanel();
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 50, 15
        );
        innerPanel.setLayout(verticalFlowLayout);
        innerPanel.setPreferredSize(new Dimension(700, 400));
        innerPanel.setBackground(ColorTheme.BOX_GENERAL_BASE_BG);

        Font titleFont = Assets.expletusSans;

        JLabel welcomeLabel = new JLabel(
                "You win"
        );
        welcomeLabel.setSize(600, 30);
        welcomeLabel.setFont(titleFont);
        innerPanel.add(welcomeLabel);

        // main panel settings
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(700, 700));
        panel.add(innerPanel);
        panel.setVisible(true);

        panel = this.gui.getMapView();
        panel.repaint();
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
