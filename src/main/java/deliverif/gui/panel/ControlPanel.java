package deliverif.gui.panel;

import deliverif.gui.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    public ControlPanel() {
        super();
        setOpaque(true);
        setBackground(ColorTheme.PANEL_1_BASE_BG);
        setPreferredSize(
                new Dimension(70, 300)
        );

        // layout
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 10, 20
        );
        setLayout(verticalFlowLayout);

        init();
    }

    private void init() {
        JButton loadMapButton = new JButton("Load Map");
        loadMapButton.setPreferredSize(
                new Dimension(50, 50)
        );
        add(loadMapButton);

        JButton loadRequestsButton = new JButton("Load Requests");
        loadRequestsButton.setPreferredSize(
                new Dimension(0, 50)
        );
        add(loadRequestsButton);

        JButton addRequestButton = new JButton("Add request");
        addRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        add(addRequestButton);

        JButton deleteRequestButton = new JButton("Delete request");
        deleteRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        add(deleteRequestButton);
    }

}