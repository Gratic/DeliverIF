package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.observer.Observable;
import deliverif.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ScrollItemPanel extends JPanel implements MouseListener,Observer {
    protected Color textColor;
    protected DeliveryTour tour;



    public ScrollItemPanel(DeliveryTour tour) {
        super();
        setOpaque(true);
        setBackground(ColorTheme.LIGHT_BASE_GREY);
        setPreferredSize(
                new Dimension(270, 125)
        );
        // layout
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 10, 20
        );
        setLayout(verticalFlowLayout);
        addMouseListener(this);
        textColor = Color.WHITE;
        this.tour = tour;
        this.tour.addObserver(this);
        setVisible(true);


    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    public void mouseClicked(MouseEvent me) {
        select();
    }

    public void mouseEntered(MouseEvent me) {

    }

    public void mouseExited(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {

    }

    public void mouseReleased(MouseEvent me) {

    }

    public void mouseDragged(MouseEvent me) {

    }

    public void mouseMoved(MouseEvent me) {
    }
    
    @Override
    public void update(Observable observed, Object arg){
        if(selected())
        {
            textColor = Color.ORANGE;
        }else
        {
            textColor = Color.WHITE;
        }
        repaint();
    }

    protected abstract boolean selected();
    protected abstract void select();


}
