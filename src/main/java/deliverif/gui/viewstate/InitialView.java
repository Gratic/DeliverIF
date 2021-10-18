package deliverif.gui.viewstate;

import deliverif.gui.Gui;
import deliverif.gui.utils.Assets;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialView extends ViewState implements ActionListener {

    public JTextField mapFilePathField;
    public JTextField requestsFilePathField;

    public InitialView(Gui gui) {
        super(gui);

        // inner JPanel to be centered
        JPanel innerPanel = new JPanel();
        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 50, 15
        );
        innerPanel.setLayout(verticalFlowLayout);
        innerPanel.setPreferredSize(new Dimension(700, 400));
        innerPanel.setBackground(ColorTheme.BOX_GENERAL_BASE_BG);

        Font titleFont = Assets.expletusSans;

        // vue test
        JLabel textForMapFilePathField = new JLabel(
                "Absolute file path for XML map file:"
        );
        textForMapFilePathField.setSize(600, 30);
        textForMapFilePathField.setFont(titleFont);
        innerPanel.add(textForMapFilePathField);

        this.mapFilePathField = new JTextField(
                "/home/onyr/Documents/4if/s1/pld_agile/testzone/hello_world.xml", 50
        );
        this.mapFilePathField.setSize(600, 30);
        innerPanel.add(this.mapFilePathField);

        JLabel textForRequestsFilePathField = new JLabel(
                "Absolute file path for XML requests file:"
        );
        textForRequestsFilePathField.setSize(600, 30);
        textForRequestsFilePathField.setFont(titleFont);
        innerPanel.add(textForRequestsFilePathField);

        this.requestsFilePathField = new JTextField(
                "/home/onyr/Documents/4if/s1/pld_agile/testzone/requests.xml",50
        );
        this.requestsFilePathField.setSize(600, 30);
        innerPanel.add(this.requestsFilePathField);

        JButton loadButton=new JButton("Load");
        loadButton.addActionListener(this);
        loadButton.setFont(titleFont);
        loadButton.setSize(180, 90);
        innerPanel.add(loadButton);

        // main panel settings
        this.panel.setLayout(new GridBagLayout());
        this.panel.setPreferredSize(new Dimension(700,700));
        this.panel.add(innerPanel);
        this.panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        this.gui.getController().loadMapButtonClick(this.gui);
    }

    // getters & setters
    public String getMapFilePath() {
        return this.mapFilePathField.getText();
    }
    public String getRequestsFilePath() {
        return this.requestsFilePathField.getText();
    }
}
