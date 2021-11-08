package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;

public class GraphicalViewPanel extends JPanel {

    public GraphicalViewPanel() {
        super();
        setOpaque(true);
        setBackground(ColorTheme.BOX_GENERAL_BASE_BG);
        setBorder(BorderFactory.createLineBorder(ColorTheme.PANEL_2_BASE_BG));
        setLayout(new BorderLayout());
    }
}
