package deliverif.gui.panel;

import deliverif.gui.Gui;

import javax.swing.*;

public abstract class GuiPanel extends JPanel {

    public Gui gui;

    public GuiPanel(Gui gui) {
        this.gui = gui;
    }
}
