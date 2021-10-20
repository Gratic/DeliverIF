package deliverif.gui.panel;

import deliverif.gui.Gui;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static deliverif.gui.utils.Utils.dye;

public class ControlPanel extends GuiPanel implements ActionListener {

    public JButton loadMapButton;
    public JButton loadRequestsButton;
    JButton addRequestButton;
    JButton deleteRequestButton;

    public ControlPanel(Gui gui) {
        super(gui);
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
        loadMapButton = new JButton(new ImageIcon(dye(Assets.loadMapImage, Color.BLACK)));
        loadMapButton.setPreferredSize(
                new Dimension(50, 50)
        );
        loadMapButton.setToolTipText("Load Map");
        loadMapButton.addActionListener(this);
        add(loadMapButton);

        loadRequestsButton = new JButton(new ImageIcon(dye(Assets.loadRequestImage, Color.BLACK)));
        loadRequestsButton.setPreferredSize(
                new Dimension(50, 50)
        );
        loadRequestsButton.setToolTipText("Load Requests");
        loadRequestsButton.addActionListener(this);
//        loadRequestsButton.setEnabled(false);
        add(loadRequestsButton);

        addRequestButton = new JButton("Add request");
        addRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        addRequestButton.addActionListener(this);
        add(addRequestButton);

        deleteRequestButton = new JButton("Delete request");
        deleteRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        deleteRequestButton.addActionListener(this);
        add(deleteRequestButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        if(e.getSource() == loadMapButton) {
            this.gui.getController().loadMapButtonClick(this.gui);
        }
        else if (e.getSource()== loadRequestsButton){
            this.gui.getController().loadRequestsButtonClick(this.gui);
        }
        else if (e.getSource() == addRequestButton){

        }
        else if (e.getSource()== deleteRequestButton){

        }

    }

}