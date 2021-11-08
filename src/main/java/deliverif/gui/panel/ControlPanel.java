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
    public JButton addRequestButton;
    public JButton deleteRequestButton;

    public JButton undoButton;
    public JButton redoButton;

    private JPanel northPanel;
    private JPanel southPanel;

    public ControlPanel(Gui gui) {
        super(gui);
        setOpaque(true);
        setBackground(ColorTheme.PANEL_1_BASE_BG);
        setPreferredSize(
                new Dimension(70, 300)
        );
        setLayout(new BorderLayout());

        init();
    }

    private void init() {
        // layouts
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 10, 20
        );

        // sub-panels
        northPanel = new JPanel();
        northPanel.setOpaque(true);
        northPanel.setBackground(ColorTheme.PANEL_1_BASE_BG);
        northPanel.setPreferredSize(new Dimension(70, 300));
        northPanel.setLayout(verticalFlowLayout);
        add(northPanel, BorderLayout.NORTH);

        southPanel = new JPanel();
        southPanel.setOpaque(true);
        southPanel.setBackground(ColorTheme.PANEL_1_BASE_BG);
        southPanel.setPreferredSize(new Dimension(70, 200));
        southPanel.setLayout(verticalFlowLayout);
        add(southPanel, BorderLayout.SOUTH);

        // buttons
        loadMapButton = new JButton(new ImageIcon(dye(Assets.loadMapImage, Color.BLACK)));
        loadMapButton.setPreferredSize(
                new Dimension(50, 50)
        );
        loadMapButton.setToolTipText("Load Map");
        loadMapButton.addActionListener(this);
        northPanel.add(loadMapButton);

        loadRequestsButton = new JButton(new ImageIcon(dye(Assets.loadRequestImage, Color.BLACK)));
        loadRequestsButton.setPreferredSize(
                new Dimension(50, 50)
        );
        loadRequestsButton.setToolTipText("Load Requests");
        loadRequestsButton.addActionListener(this);
        loadRequestsButton.setEnabled(false);
        northPanel.add(loadRequestsButton);

        addRequestButton = new JButton(new ImageIcon(dye(Assets.addRequestImage, Color.BLACK)));
        addRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        addRequestButton.addActionListener(this);
        addRequestButton.setEnabled(false);
        northPanel.add(addRequestButton);

        deleteRequestButton = new JButton(new ImageIcon(dye(Assets.removeRequestImage, Color.BLACK)));
        deleteRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        deleteRequestButton.addActionListener(this);
        deleteRequestButton.setEnabled(false);
        northPanel.add(deleteRequestButton);

        // undo & redo
        undoButton = new JButton(new ImageIcon(dye(Assets.undoImage, Color.BLACK)));
        undoButton.setPreferredSize(
                new Dimension(50, 50)
        );
        undoButton.addActionListener(this);
        undoButton.setEnabled(false);
        southPanel.add(undoButton);

        redoButton = new JButton(new ImageIcon(dye(Assets.redoImage, Color.BLACK)));
        redoButton.setPreferredSize(
                new Dimension(50, 50)
        );
        redoButton.addActionListener(this);
        redoButton.setEnabled(false);
        southPanel.add(redoButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        if (e.getSource() == loadMapButton) {
            this.gui.getController().loadMapButtonClick(this.gui);
        } else if (e.getSource() == loadRequestsButton) {
            this.gui.getController().loadRequestsButtonClick(this.gui);
        } else if (e.getSource() == addRequestButton) {
            this.gui.getController().addRequestButtonClick(this.gui);
        } else if (e.getSource() == deleteRequestButton) {

        } else if (e.getSource() == undoButton) {
            this.gui.getController().undo();
        } else if(e.getSource() == redoButton) {
            this.gui.getController().redo();
        }

    }

}