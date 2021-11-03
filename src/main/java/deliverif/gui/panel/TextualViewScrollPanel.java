package deliverif.gui.panel;


import deliverif.gui.utils.ColorTheme;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.model.RoadSegment;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class TextualViewScrollPanel extends JScrollPane {

    private ArrayList<Request> requests;
    private CityMap map;
    private DeliveryTour tour;
    public TextualViewScrollPanel( CityMap map, DeliveryTour tour) {
        super();
        setOpaque(true);
        this.getViewport().setBackground(ColorTheme.PANEL_2_BASE_BG);
        setPreferredSize(
                new Dimension(275, 700)
        );
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        this.map=map;
        this.tour = tour;
        init();
        repaint();
    }

    private void init() {
        RequestContainerPanel requestContainerPanel = new RequestContainerPanel(map, tour);
        this.setViewportView(requestContainerPanel);



    }
}

