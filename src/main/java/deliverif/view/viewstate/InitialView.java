package deliverif.view.viewstate;

import deliverif.view.View;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialView extends ViewState implements ActionListener {

    public JTextField mapFilePathField;
    public JTextField requestsFilePathField;

    public InitialView(View view) {
        super(view);

        // vue test
        JLabel textForMapFilePathField = new JLabel(
                "Absolute file path for XML map file:"
        );
        textForMapFilePathField.setSize(300, 30);
        textForMapFilePathField.setLocation(50, 20);
        this.panel.add(textForMapFilePathField);

        this.mapFilePathField = new JTextField(
                "/home/onyr/Documents/4if/s1/pld_agile/testzone/hello_world.xml", 50
        );
        this.mapFilePathField.setLocation(50, 50);
        this.mapFilePathField.setSize(300, 30);
        this.panel.add(this.mapFilePathField);

        JLabel textForRequestsFilePathField = new JLabel(
                "Absolute file path for XML requests file:"
        );
        textForRequestsFilePathField.setSize(300, 30);
        textForRequestsFilePathField.setLocation(50, 100);
        this.panel.add(textForRequestsFilePathField);

        this.requestsFilePathField = new JTextField(
                "/home/onyr/Documents/4if/s1/pld_agile/testzone/requests.xml",50
        );
        this.requestsFilePathField.setLocation(50, 130);
        this.requestsFilePathField.setSize(300, 30);
        this.panel.add(this.requestsFilePathField);

        JButton b=new JButton("Load");
        b.addActionListener(this);
        b.setBounds(100,180,100, 40);
        this.panel.add(b);

        this.panel.setSize(400,500);
        this.panel.setLayout(null);
        this.panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        this.view.getController().loadMapButtonClick(this.view);
    }

    // getters & setters
    public String getMapFilePath() {
        return this.mapFilePathField.getText();
    }
    public String getRequestsFilePath() {
        return this.requestsFilePathField.getText();
    }
}
