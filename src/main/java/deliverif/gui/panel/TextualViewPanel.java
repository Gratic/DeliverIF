package deliverif.gui.panel;

        import deliverif.gui.utils.ColorTheme;
        import javax.swing.*;
        import java.awt.*;

public class TextualViewPanel extends JPanel {

    public TextualViewPanel() {
        super();
        setOpaque(true);
        setBackground(ColorTheme.PANEL_2_BASE_BG);
        setPreferredSize(
                new Dimension(300, 300)
        );

        // layout
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 10, 20
        );
        setLayout(verticalFlowLayout);

        init();
    }

    private void init() {
        JLabel panelTitle = new JLabel(
                "Textual View (tmp)"
        );
        panelTitle.setSize(200, 30);
        add(panelTitle);
    }
}
