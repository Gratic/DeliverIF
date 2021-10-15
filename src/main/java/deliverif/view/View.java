package deliverif.view;

import java.awt.*;
import java.awt.event.*;
import deliverif.controller.Controller;
import deliverif.view.viewstate.*;

import javax.swing.*;

public class View implements ActionListener {

    protected Controller controller;
    protected ViewState currentViewState;

    protected JFrame frame;
    protected JPanel mainPanel;

    protected int height = 600;
    protected int width = 800;
    
    public View(Controller controller) {
        this.controller = controller;
        this.frame = new JFrame("Deliverif");
        this.frame.setSize(height, width);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(null);
        this.mainPanel = new MainPanel(height, width);
        System.out.println(this.mainPanel.getWidth());
        System.out.println(this.mainPanel.getHeight());
        this.frame.setContentPane(this.mainPanel);
        this.frame.setVisible(true);

        // debug panel
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setLocation(10, 10);
        panel1.setSize(300, 200);
        panel1.setBackground(Color.red);
        panel1.setVisible(true);
        this.frame.add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.blue);
        this.frame.add(panel2);

        JFrame frame3 = new JFrame();
        frame3.setBackground(Color.cyan);
        frame3.setSize(50, 100);
        frame3.setLocation(400, 50);
        frame3.setVisible(true);
        //this.frame.add(frame3);
    }

    public void init() {
        this.currentViewState = new InitialView(this);
        JPanel panel = this.currentViewState.getJPanel();
        this.frame.add(panel);
        panel.repaint(); // WARN: crucial !!!

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // load action
        this.controller.loadMapButtonClick(this);
    }

    // getters & setters
    public Controller getController() {
        return this.controller;
    }
    public ViewState getCurrentViewState() {
        return this.currentViewState;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
    
}
