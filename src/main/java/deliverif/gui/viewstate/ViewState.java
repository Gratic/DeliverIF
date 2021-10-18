package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.panel.GuiPanel;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;

public abstract class ViewState {

    protected Gui gui;
    protected JPanel panel;

    public ViewState(Gui gui) {
        this.gui = gui;
    }
}
