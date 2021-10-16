package deliverif.view.viewstate;

import deliverif.view.View;
import deliverif.view.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;

public abstract class ViewState {

    protected View view;
    protected JPanel panel;

    public ViewState(View view) {
        this.view = view;
        this.panel = new JPanel();
        this.panel.setOpaque(true);
        this.panel.setBackground(ColorTheme.LIGHT_BASE_GREY);
        this.panel.setLocation(50, 50);
        this.panel.setLayout(null);
    }

    public JPanel getJPanel() {
        return this.panel;
    }
}
