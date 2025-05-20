package src.Pages.WelcomePage;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import src.Pages.MainMenu.MainMenu;
import src.Pages.WelcomePage.Welcome;

/**
 * The initial window that opens when running the program.
 * <p>
 * The welcome window contains the logo of the game and the play 
 * button that goes to the main menu window
 * <p>
 * @version 1.0
 */
public class Welcome {

    // Main application window
    JFrame window;
    Container con;
    JPanel panel;

    /**
     * Welcome screen of the game which consists of the logo and play button
     */
    public Welcome() {

        // Creation of JFrame
        window = new JFrame("Welcome!");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        con = window.getContentPane();
        con.setBackground(Color.WHITE);
        con.setLayout(new GridBagLayout());

        // Create and setup panel
        panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        // Component Declarations
        JButton startButton = new JButton("Play!"); // Create button
        ImageIcon logo = loadIcon("src/Pages/assets/Logo.png", 350, 350); // Load logo image
        JLabel logoLabel = new JLabel(logo);

        // Style the start button
        startButton.setFont(new Font("Montserrat", Font.BOLD, 60));
        startButton.setFocusable(false);
        startButton.setPreferredSize(new Dimension(225,100));
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(false);

        // Custom styling of the border to make it rounded and
        startButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arcSize = 50; // Adjust this for roundness
                int thickness = 2; // Adjust this for border thickness

                // Button Background
                g2.setColor(startButton.getBackground());
                g2.fillRoundRect(thickness / 2, thickness / 2,
                        c.getWidth() - thickness, c.getHeight() - thickness,
                        arcSize, arcSize); // Rounded edges

                // Thicker Border
                g2.setStroke(new BasicStroke(thickness)); // Set thicker stroke
                g2.setColor(Color.BLACK); // Border color
                g2.drawRoundRect(thickness / 2, thickness / 2,
                        c.getWidth() - thickness, c.getHeight() - thickness,
                        arcSize, arcSize);

                g2.dispose();
                super.paint(g, c);
            }
        });

        // Layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Add logo with spacing
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 50, 0); // Increased vertical spacing
        panel.add(logoLabel, gbc);

        // Add button with spacing
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0); // Adjust spacing below button
        panel.add(startButton, gbc);

        // Menu Button event, when you click on the button, go from the welcome page to the main menu page
        startButton.addActionListener(e -> {
            window.dispose(); // delete current window
            SwingUtilities.invokeLater(MainMenu::new);
        });

        // Add the panel and make the window visible
        con.add(panel);
        window.setVisible(true);    
    }

    /**
     * Resizes the logo of our game 
     * @param path the path to get to the image in the folders
     * @param width the new width of the image
     * @param height the new height of the image
     * @return the resized image
     */
    public ImageIcon loadIcon(String path, int width, int height) {
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            System.err.println("Couldn't find file: " + path);
            return new ImageIcon();
        } else {
            System.out.println("Loaded resource: " + resource);
        }

        ImageIcon originalIcon = new ImageIcon(resource);
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
