package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;

public class TextualViewPanel extends JPanel {

    public TextualViewPanel() {
        super();
        setOpaque(true);
        setBackground(ColorTheme.GENERAL_BASE_BG);
        setBorder(BorderFactory.createLineBorder(ColorTheme.PANEL_2_BASE_BG));
        setPreferredSize(
                new Dimension(400, 400)
        );
        // layout
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 30, 30

        );
        setLayout(verticalFlowLayout);

    }


}
