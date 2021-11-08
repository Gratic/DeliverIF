package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import deliverif.model.DeliveryTour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ScrollItemPanel extends JPanel implements MouseListener {
    protected Color textColor;
    protected DeliveryTour tour;


    public ScrollItemPanel(DeliveryTour tour) {
        super();
        setOpaque(true);
        setBackground(ColorTheme.PANEL_1_BASE_BG);
        setPreferredSize(
                new Dimension(360, 125)
        );
        // layout
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 30, 30
        );
        setLayout(verticalFlowLayout);
        addMouseListener(this);
        textColor = ColorTheme.TEXT_COLOR;
        this.tour = tour;
        setVisible(true);


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    protected void setColor(Graphics g) {
        if (selected()) {
            g.setColor(ColorTheme.TEXT_COLOR_HIGHLIGHTED);
        } else {
            g.setColor(ColorTheme.TEXT_COLOR);
        }
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


    protected abstract boolean selected();

    protected abstract void select();


}
