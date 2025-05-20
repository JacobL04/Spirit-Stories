package src.Pages.ComicScene;

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
 * The short comic that tells you a short story about how you get your pet
 * <p>
 * The comic is always played every time the user plays the game on a new game file
 * <p>
 * @version 1.0
 */
public class ComicScene extends JFrame {

    private JLabel imageLabel;
    private ImageIcon imageIcon;
    private JPanel mainPanel;
    private JTextArea textArea;
    private JButton dialogueButton;
    private ArrayList<String> dialogueLines; // Store the dialogues in an array
    private ArrayList<String> imagePaths;  // Store the image paths corresponding to dialogues
    private int currentLineIndex;

    /**
     * Shows the dialogue/story for a new game before gameplay
     * @param file is the game file we're going to use for the gameplay
     */
    public ComicScene(String file){
        cutSceneDialogue(); // Initialize the cutscenes + dialogues
        
        // Set up the window
        setTitle("Cutscene Dialogue");
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

        // Creates the text area for the dialogue
        textArea = new JTextArea();
        textArea.setText(dialogueLines.get(currentLineIndex)); // First dialogue in the array
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Wraps new line to stay within the text area
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 30));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Create the button to cycle through dialogues
        dialogueButton = new JButton("Next");
        dialogueButton.setBackground(Color.BLACK);
        dialogueButton.setForeground(Color.WHITE);
        dialogueButton.setFont(new Font("Arial", Font.PLAIN, 20));
        dialogueButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // When button pressed changes dialogue and image
        dialogueButton.addActionListener(event -> {
            nextDialogue(file);
        });

        // Add the image and text area to the main panel
        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        mainPanel.add(dialogueButton, BorderLayout.SOUTH);  // Add the button at the bottom

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
     * Gets the scene of the next dialogue in the story
     * @param file file contaning the dialogue
     */
    private void nextDialogue(String file){
        currentLineIndex++;
            if (currentLineIndex < dialogueLines.size()){
                textArea.setText(dialogueLines.get(currentLineIndex)); // Updates the text
                imageIcon = new ImageIcon(imagePaths.get(currentLineIndex)); // Updates the image
                imageLabel.setIcon(imageIcon);
                
                resizeImage();
            }
            
            else { // When dialogue is done, goes to next action
                dispose(); // Closes program for now
                Gameplay gameplay = new Gameplay(file);
                System.out.println("End of the cutscene");
            }
    }

    /**
     * Adds all images and dialogue into an ArrayList
     */
    private void cutSceneDialogue() {
        // Initialize dialogue lines
        dialogueLines = new ArrayList<>();
        imagePaths = new ArrayList<>();

        // Adds the dialogue image paths into an array which changes when a button is pressed
        dialogueLines.add("As you wandered around the deep dark forest, something catches your eye...");
        imagePaths.add("src/Pages/assets/Scene1.png");  // Image for first dialogue

        dialogueLines.add("You take a closer look at what it appears to be an Egg..");
        imagePaths.add("src/Pages/assets/Scene2.png"); // Same image, different dialogue

        dialogueLines.add("!!!");
        imagePaths.add("src/Pages/assets/Scene3.png");  // Image for second dialogue

        dialogueLines.add("The egg begins to crack open. Something inside is coming to life!");
        imagePaths.add("src/Pages/assets/Scene4.png"); // Image changes after the second dialogue

        dialogueLines.add("It appears to be a Spirit Animal!");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        dialogueLines.add("They are rare and powerful creatures from ancient legends...");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        dialogueLines.add("..Spirit Animals will bond with the first creature they see...");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        dialogueLines.add("..only those who are pure-hearted can earn their trust.");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        dialogueLines.add("The bond between you and your Spirit Animal is about to begin...");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        dialogueLines.add("Take care of it, and it will guide you on a journey like no other...");
        imagePaths.add("src/Pages/assets/Scene5.png"); // Image changes again after the last dialogue

        currentLineIndex = 0;
    }

    /**
     * Resizes the images inside the window
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