package src.Pages.MainMenu;
import javax.swing.*;

import com.google.gson.Gson;

import src.Pages.Gameplay.Gameplay;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Provides user guidelines on how to play the game and what each button does
 * <p>
 * Images with bright red border and text on certain parts of the 
 * screen shows the user how to play the game on each window
 * <p>
 */
public class Tutorial extends JFrame{
    private JLabel imageLabel;
    private ImageIcon imageIcon;
    private JPanel mainPanel;
    private JTextArea textArea;
    private JButton nextButton;
    private ArrayList<String> dialogueLines; //Store the dialogies in an array
    private ArrayList<String> imagePaths;  // Store the image paths corresponding to dialogues
    private int currentLineIndex;

    /**
     * Shows the dialogue/story for a new game before gameplay. 
     * Tells the user how to play the game with edited images that have 
     * text on what each button and feature on the current window has
     */
    public Tutorial(){
        cutSceneDialogue(); // Initialize the cutscenes + dialogues
        
        // Set up the window
        setTitle("Tutorial");
        setSize(800, 525);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setLocationRelativeTo(null); // Centers the window
        
        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Loads the initial image
        imageIcon = new ImageIcon(imagePaths.get(currentLineIndex)); // Use the first image in the array
        imageLabel = new JLabel(imageIcon);

        // Create the button to cycle through dialogues
        nextButton = new JButton("Next");
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Arial", Font.PLAIN, 20));
        nextButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // When button pressed changes dialogue and image
        nextButton.addActionListener(event -> {
            nextDialogue();
        });

        // Add the image and text area to the main panel
        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        mainPanel.add(nextButton, BorderLayout.SOUTH);  // Add the button at the bottom

        add(mainPanel);

        // Add a component listener to the window to track resizing
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                resizeImage();
            }
        });

        // Resize the image initially after the frame is displayed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                resizeImage();
            }
        });

        setVisible(true);
    }

    /**
     * Fetches the next line of dialogue until there are no more lines left to be red.
     * Tutorial ends and goes back to main menu after
     */
    private void nextDialogue(){
        currentLineIndex++;
            if (currentLineIndex < imagePaths.size()){
                imageIcon = new ImageIcon(imagePaths.get(currentLineIndex)); // Updates the image
                imageLabel.setIcon(imageIcon);
                
                resizeImage();
            }
            
            else { // Wehn dialogue is done, goes to next action
                dispose(); // Closes program for now
                MainMenu mainMenu = new MainMenu();
                System.out.println("End of the tutorial");
            }
    }

    /**
     * Adds images  and dialogie to separate ArrayLists. They will be read together 
     * so each line of dialogue mirros with each image in the ArrayList
     */
    private void cutSceneDialogue() {
        // Initialize dialogue lines
        dialogueLines = new ArrayList<>();
        imagePaths = new ArrayList<>();

        imagePaths.add("src/Pages/assets/tut1.png");

        imagePaths.add("src/Pages/assets/tut2.png");

        imagePaths.add("src/Pages/assets/tut3.png");

        imagePaths.add("src/Pages/assets/tut4.png");

        imagePaths.add("src/Pages/assets/tut5.png");

        imagePaths.add("src/Pages/assets/tut6.png");

        imagePaths.add("src/Pages/assets/tut7.png");

        imagePaths.add("src/Pages/assets/tut8.png");

        imagePaths.add("src/Pages/assets/tut9.png");

        imagePaths.add("src/Pages/assets/tut10.png");

        imagePaths.add("src/Pages/assets/tut11.png");

        imagePaths.add("src/Pages/assets/tut12.png");
        currentLineIndex = 0;
    }

    /**
     * Resizes the images inside the window so it fills almost the entire screen horizontally
     */
    private void resizeImage() {
        int width = getWidth();

        // Scale the image dynamically to fit within the current window size
        int newWidth = (int) (width * 0.85); // 85% of the window width
        int newHeight = (int) (imageIcon.getIconHeight() * ((double) newWidth / imageIcon.getIconWidth()));

        Image resizedImage = imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(resizedImage)); // Update the label with the resized image
    }

}