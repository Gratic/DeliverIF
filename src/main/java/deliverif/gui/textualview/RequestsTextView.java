package deliverif.gui.textualview;

import deliverif.gui.panel.TextualViewScrollPanel;
import deliverif.gui.utils.ColorTheme;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;

public class RequestsTextView extends JPanel implements Observer {
    private final DeliveryTour tour;
    private final CityMap map;
    private TextualViewScrollPanel textualViewScrollPanel;

    public RequestsTextView(DeliveryTour tour, CityMap map) {
        this.setBackground(ColorTheme.PANEL_2_BASE_BG);
        this.map = map;
        this.tour = tour;
        this.tour.addObserver(this);
        this.setPreferredSize(new Dimension(280, 900));
        //this.setLayout(new BorderLayout());
        this.setLayout(new FlowLayout());
    }


    @Override
    public void update(Observable observed, Object arg) {
        this.repaint();
    }

    public void init() {
        if (tour.getRequests().size() == 0) {
            System.out.println("No request loaded");
        } else {
            removeAll();
            TextualViewScrollPanel textualViewScrollPanel = new TextualViewScrollPanel(map, tour);
            add(textualViewScrollPanel);
            revalidate();
            repaint();
        }
    }

}
