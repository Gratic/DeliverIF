package deliverif.view;

import deliverif.view.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel() {
        super();
        setOpaque(true);
        setBackground(ColorTheme.GENERAL_BASE_BG);
        setLayout(new BorderLayout());
    }

}
