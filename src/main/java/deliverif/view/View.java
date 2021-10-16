package deliverif.view;

import java.awt.*;
import java.awt.event.*;
import deliverif.controller.Controller;
import deliverif.view.utils.Assets;
import deliverif.view.utils.ColorTheme;
import deliverif.view.viewstate.*;

import javax.swing.*;

public class View implements ActionListener {

    protected Controller controller;
    protected ViewState currentViewState;

    protected JFrame frame;

    protected int height = 600;
    protected int width = 800;
    
    public View(Controller controller) {
        this.controller = controller;

        this.frame = new JFrame("Deliverif");
        this.frame.setSize(height, width);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.setContentPane(new MainPanel());
        System.out.println(this.frame.getContentPane().getWidth());
        System.out.println(this.frame.getContentPane().getHeight());


        // WEST panel 1
        JPanel panel1 = new JPanel();
        panel1.setBackground(ColorTheme.PANEL_1_BASE_BG);
        panel1.setPreferredSize(
                new Dimension(70, 300)
        );

        FlowLayout verticalFlowLayout = new FlowLayout(
                FlowLayout.LEADING, 10, 20
        );
        panel1.setLayout(verticalFlowLayout);

        JButton loadMapButton = new JButton("Load Map");
        loadMapButton.setPreferredSize(
                new Dimension(50, 50)
        );
        panel1.add(loadMapButton);

        JButton loadRequestsButton = new JButton("Load Map");
        loadRequestsButton.setPreferredSize(
                new Dimension(50, 50)
        );
        panel1.add(loadRequestsButton);

        JButton addRequestButton = new JButton("Load Map");
        addRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        panel1.add(addRequestButton);

        JButton deleteRequestButton = new JButton("Load Map");
        deleteRequestButton.setPreferredSize(
                new Dimension(50, 50)
        );
        panel1.add(deleteRequestButton);

        this.frame.getContentPane().add(
                panel1, BorderLayout.WEST
        );


        // EAST panel 2
        JPanel panel2 = new JPanel();
        panel2.setBackground(ColorTheme.PANEL_2_BASE_BG);
        this.frame.getContentPane().add(
                panel2, BorderLayout.EAST
        );

        //this.frame.pack(); // resize to fit components
        this.frame.setVisible(true);
    }

    public void init() {
        // load assets
        Assets.init();

        // set current ViewState
        this.currentViewState = new InitialView(this);
        JPanel panel = this.currentViewState.getJPanel();
        this.frame.getContentPane().add(
                panel, BorderLayout.CENTER
        );
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
