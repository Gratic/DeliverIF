package deliverif.gui.viewstate;

import deliverif.gui.Gui;

import javax.swing.*;

public abstract class ViewState {

    protected Gui gui;
    protected JPanel panel;

    public ViewState(Gui gui) {
        this.gui = gui;
    }
}
