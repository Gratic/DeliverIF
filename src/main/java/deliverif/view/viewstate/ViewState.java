package deliverif.view.viewstate;

import deliverif.view.View;

import javax.swing.*;
import java.awt.*;

public abstract class ViewState {

    protected View view;
    protected JPanel panel;

    public ViewState(View view) {
        this.view = view;
        this.panel = new JPanel();
        this.panel.setSize(this.view.getWidth(), this.view.getHeight());
        this.panel.setOpaque(true);
        this.panel.setBackground(Color.green);
        this.panel.setLocation(50, 50);
    }

    public JPanel getJPanel() {
        return this.panel;
    }
}
