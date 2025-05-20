package src;

import javax.swing.SwingUtilities;
import src.Pages.WelcomePage.Welcome;

/**
 * Represents a window in the application.
 * This class extends JFrame and provides methods to load icons and manage window settings.
 */
public class Play {
   /**
    * The main method that serves as the entry point of the program.
    * It schedules a job for the event-dispatching thread: creating and showing the application's GUI.
    * 
    * @param args Command line arguments
    */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure that the GUI updates are performed on the Event Dispatch Thread
        SwingUtilities.invokeLater(Welcome::new);
    }
}
