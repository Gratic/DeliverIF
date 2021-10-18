package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;

public abstract class ViewState {

    protected Gui gui;
    protected JPanel panel;

    public ViewState(Gui gui) {
        this.gui = gui;
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
