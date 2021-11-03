package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RequestContainerPanel extends JPanel implements Observer
{

    private final CityMap map;
    private TextualViewScrollPanel textualViewScrollPanel;
    private DeliveryTour tour;

    public RequestContainerPanel( CityMap map , DeliveryTour tour) {
        this.setBackground(ColorTheme.PANEL_2_BASE_BG);
        this.map = map;
        this.tour = tour;
        this.setPreferredSize(new Dimension(270, tour.getRequests().size()*130*2+50));
        //this.setLayout(new BorderLayout());
        this.setLayout(new FlowLayout());
        init();
    }


    @Override
    public void update(Observable observed, Object arg)
    {
        this.init();
        this.repaint();
    }

    public void init()
    {
        removeAll();
        if(tour.getRequests().size()==0)
        {
            System.out.println("No request loaded");
        }else
        {
            int location = 0;
            int requestNumber=1;
            DeparturePointPanel departurePointPanel = new DeparturePointPanel(map, tour);
            add(departurePointPanel);
            departurePointPanel.setLocation(0,location);
            location = location+departurePointPanel.getHeight()+10;
            for(Request r: tour.getRequests())
            {
                RequestPickupPanel pickupPanel = new RequestPickupPanel(r,requestNumber,map, tour);
                RequestDeliveryPanel deliveryPanel = new RequestDeliveryPanel(r,requestNumber,map,tour);
                add(pickupPanel);
                add(deliveryPanel);
                pickupPanel.setLocation(0,location);
                deliveryPanel.setLocation(0,location+=(deliveryPanel.getHeight()+10));
                location=location+deliveryPanel.getHeight()+10;
                requestNumber++;


            }

        }
        revalidate();
    }

}