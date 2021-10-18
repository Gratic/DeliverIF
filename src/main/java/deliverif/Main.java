package deliverif;

import deliverif.controller.Controller;

public class Main
{
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Controller controller = new Controller();
            }
        });
    }
}
