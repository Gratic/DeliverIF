package deliverif.view.viewState;

import deliverif.view.View;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialView extends ViewState implements ActionListener {

    public JTextField filePathField;

    public InitialView(View view) {
        super(view);

        // vue test
        JFrame f=new JFrame();//creating instance of JFrame
        this.filePathField = new JTextField(50);
        this.filePathField.setLocation(50, 50);
        JButton b=new JButton("Load");//creating instance of JButton
        b.addActionListener(this);
        b.setBounds(130,100,100, 40);

        f.add(b);//adding button in JFrame

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible

        // test display map
        JFrame cityMapFrame = new JFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        this.view.getController().loadMapButtonClick(this.view);
    }

    // getters & setters
    public String getFilePath() {
        return this.filePathField.getText();
    }
}
