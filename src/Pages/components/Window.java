package src.Pages.components;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * The Window class creates the initial layout for other windows
 * <p>
 * Sets default values of windows that extend this class such as the title, non resizable window, custom cursor, etc.
 * <p>
 * @version 1.0
 */
public class Window extends JFrame {
    /**
     * Change the size of the image into an icon
     * @param path path to the image
     * @param width desired width of the image
     * @param height desired height of the image
     * @return an icon with the updated height or width
     */
    protected ImageIcon loadIcon(String path, int width, int height) {
        // Load image from resource path
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            System.err.println("Couldn't find file: " + path);
            return new ImageIcon();
        }
        
        ImageIcon originalIcon = new ImageIcon(resource);
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    /**
     * Default settings of each JFrame or window
     */
    public Window() {
        // Default Frame Settings
        setSize(800, 600);
        setTitle("Pet Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Changes the java cup logo to our image logo
        // ImageIcon logo = new ImageIcon("src/GUI/assets/servos.jpg");
        // SetIconImage(logo.getImage());

        // Change the default cursor to a cursor of our 
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage("src/Pages/assets/cute-cursor.png").getScaledInstance(16, 16,
                Image.SCALE_SMOOTH);
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "Custom Cursor");
        setCursor(customCursor);

        // make window visible, use dispose() or setVisible(false)? to delete window
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // Print in console if a window is being deleted or created
    public static void windowStatus(String status, String window) {
        if ("del".equals(status)) {
            System.out.printf("Deleted %s Window\n", window);
        } else if ("create".equals(status)) {
            System.out.printf("Created %s Window\n", window);
        }
    }
}
