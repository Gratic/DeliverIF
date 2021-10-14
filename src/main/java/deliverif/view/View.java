package deliverif.view;

import javax.swing.*;
import deliverif.controller.Controller;

import java.awt.event.*;

public class View implements ActionListener {

    protected Controller controller;

    // testing purpose
    public JTextField filePathField;
    
    public View(Controller controller) {
        
        // vue test
        JFrame f=new JFrame();//creating instance of JFrame
        JTextField filePathField = new JTextField(50);
        filePathField.setLocation(50, 50);
        JButton b=new JButton("Load");//creating instance of JButton
        b.setBounds(130,100,100, 40);

        f.add(b);//adding button in JFrame  

        f.setSize(400,500);//400 width and 500 height  
        f.setLayout(null);//using no layout managers  
        f.setVisible(true);//making the frame visible

        // test display map
        JFrame cityMapFrame = new JFrame();

    }

    public void init() {
        // TODO: complete init()
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        controller.buttonClick(this);
    }
    
}
