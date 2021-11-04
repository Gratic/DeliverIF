package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;

public class InitialView extends ViewState {

    public InitialView(Gui gui) {
        super(gui);

        createGuiComponents();
        gui.getGraphicalViewPanel().add(
                panel, BorderLayout.CENTER
        );
        gui.getGraphicalViewPanel().repaint();
    }

    private void createGuiComponents() {
        // base panel
        panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(ColorTheme.LIGHT_BASE_GREY);

        // inner JPanel to be centered
        JPanel innerPanel = new JPanel();
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 50, 15
        );
        innerPanel.setLayout(verticalFlowLayout);
        innerPanel.setPreferredSize(new Dimension(700, 400));
        innerPanel.setBackground(ColorTheme.BOX_GENERAL_BASE_BG);

        Font titleFont = Assets.expletusSans;

        JLabel welcomeLabel = new JLabel(
                "Please choose a map to load."
        );
        welcomeLabel.setSize(600, 30);
        welcomeLabel.setFont(titleFont);
        innerPanel.add(welcomeLabel);

        // main panel settings
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(700, 700));
        panel.add(innerPanel);
        panel.setVisible(true);
    }
}
