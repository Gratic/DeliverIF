package deliverif.view;

import deliverif.Main;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel() {
        super();
        setOpaque(true);
        setBackground(Color.gray);
        setLayout(null);
    }

    public MainPanel(int width, int height) {
        super();
        setOpaque(true);
        setBackground(Color.gray);
        setSize(width, height);
    }

}
