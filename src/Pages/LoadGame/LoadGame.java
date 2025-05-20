package src.Pages.LoadGame;

import src.Pages.components.LoadDataFile;
import src.Pages.components.SaveSlot;
import src.Pages.components.CustomFont;
import com.google.gson.*;
import javax.swing.*;
import src.Pages.ComicScene.ComicScene;
import src.Pages.Gameplay.Gameplay;
import src.Pages.MainMenu.MainMenu;
import src.Pages.ParentalPage.Playtime;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The LoadGame class represents the window for loading saved games into slots
 * <p>
 * It provides functionality to display, load, and delete saved game slots.
 * <p>
 * @version 1.0
 */
public class LoadGame {
    private Set<String> selectedFiles = new HashSet<>(); // Keeps track of selected game slots
    private JPanel slotsContainer;

    private JFrame window;
    private JPanel mainPanel;
    private JPanel slot1Panel, slot2Panel, slot3Panel;

    /**
     * Creates the layout of the load game window.
     */
    public LoadGame() {
        window = new JFrame("Load Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        window.setLocationRelativeTo(null);

        // Main panel using BorderLayout for overall structure
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Container for slots with vertical BoxLayout
        slotsContainer = new JPanel();
        slotsContainer.setLayout(new BoxLayout(slotsContainer, BoxLayout.Y_AXIS));
        slotsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create game slot panels
        slot1Panel = createSlotPanel("Game 1", "Game1.json");
        slot2Panel = createSlotPanel("Game 2", "Game2.json");
        slot3Panel = createSlotPanel("Game 3", "Game3.json");

        // Add slots to container with spacing
        slotsContainer.add(slot1Panel);
        slotsContainer.add(Box.createVerticalStrut(20));
        slotsContainer.add(slot2Panel);
        slotsContainer.add(Box.createVerticalStrut(20));
        slotsContainer.add(slot3Panel);

        slotsContainer.setPreferredSize(new Dimension(900, 750));
        slotsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        slotsContainer.setOpaque(false); // Transparent background

        // Center the slots container and style slots
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(slotsContainer);
        centerWrapper.setBackground(new Color(238, 238, 238));

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // Bottom button panel with left alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.BLACK);

        JButton backButton = new JButton("Back");
        JButton eraseButton = new JButton("Erase");
        JButton copyButton = new JButton("Copy");

        // Erase games when checkbox is selected
        eraseButton.addActionListener(e -> {
            eraseButton();
        });

        // Redirects to main menu
        backButton.addActionListener(e -> {
            backButton();
        });

        // Style buttons
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new CustomFont("Regular", 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        eraseButton.setBackground(Color.WHITE);
        eraseButton.setForeground(Color.BLACK);
        eraseButton.setFont(new CustomFont("Regular", 14));
        eraseButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        copyButton.setBackground(Color.WHITE);
        copyButton.setForeground(Color.BLACK);
        copyButton.setFont(new CustomFont("Regular", 14));
        copyButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Bottom left button layouts
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Line breaks
        buttonPanel.add(eraseButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Line breaks
        buttonPanel.add(copyButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        window.setContentPane(mainPanel);
        window.setVisible(true);
    }

    /**
     * Deletes selected game files, data, and their slots.
     */
    private void eraseButton() {
        if (!selectedFiles.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(window, "Delete " + selectedFiles.size() + " saved game(s)?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                for (String file : selectedFiles) {
                    File jsonFile = new File("src/Data/" + file);

                    if (jsonFile.exists()) {
                        try {
                            Files.delete(jsonFile.toPath());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(window, "Failed to delete " + file, "Deletion Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                selectedFiles.clear();
                refreshSlots();
            }
        }
    }

    /**
     * Closes the current window and opens the main menu window.
     */
    private void backButton() {
        window.dispose();
        SwingUtilities.invokeLater(MainMenu::new);
    }

    /**
     * Updates the number of game slots inside the load game window.
     */
    private void refreshSlots() {
        // If no game files exist, delete last_saved.txt
        File Game1 = new File("src/Data/Game1.json");
        File Game2 = new File("src/Data/Game2.json");
        File Game3 = new File("src/Data/Game3.json");

        File lastSaved = new File("src/Data/last_saved.txt");

        if (!Game1.exists() && !Game2.exists() && !Game3.exists()) {
            try {
                Files.delete(lastSaved.toPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(window, "Failed to delete " + lastSaved, "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Clear all slots and recreate them
        slotsContainer.removeAll();

        slotsContainer.add(createSlotPanel("Game 1", "Game1.json"));
        slotsContainer.add(Box.createVerticalStrut(20));
        slotsContainer.add(createSlotPanel("Game 2", "Game2.json"));
        slotsContainer.add(Box.createVerticalStrut(20));
        slotsContainer.add(createSlotPanel("Game 3", "Game3.json"));

        slotsContainer.revalidate();
        slotsContainer.repaint();
    }

    /**
     * Creates a load game slot with existing data.
     * @param title the title of the game
     * @param bgColor the background color of each game slot
     * @param file the file to be read to retrieve data for the game slot
     * @return the entire game slot panel
     */
    private JPanel createLoadGameSlot(String title, Color bgColor, String file) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        LoadDataFile loadDataFile = new LoadDataFile();

        // Game info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        infoPanel.setBackground(Color.LIGHT_GRAY);
        JLabel petTypeLabel = new JLabel("Pet Type: NONE");
        JLabel lastSavedLabel = new JLabel("Last saved: YYYY-MM-DD HH:MM");
        JLabel playtimeLabel = new JLabel("Playtime: Hh MMm");
        JLabel scoreLabel = new JLabel("Score: XX");

        infoPanel.add(petTypeLabel);
        infoPanel.add(lastSavedLabel);
        infoPanel.add(playtimeLabel);
        infoPanel.add(scoreLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.setFont(new CustomFont("Bold", 14));

        panel.add(infoPanel, BorderLayout.CENTER);

        String filePath = "src/Data/" + file;
        File jsonFile = new File("src/Data/" + file);

        // Debug file existence
        System.out.println("Checking for file: " + jsonFile.getAbsolutePath());

        if (jsonFile.exists()) {
            System.out.println("File already exists");
            String jsonContent = new Gson().toJson(loadDataFile.loadJsonFromFile(filePath));
            if (jsonContent != null) {
                try {
                    JsonObject gameData = JsonParser.parseString(jsonContent).getAsJsonObject();

                    // Update labels with validation
                    if (gameData.has("Species")) {
                        petTypeLabel.setText("Pet Type: " + gameData.get("Species").getAsString());
                    }

                    if (gameData.has("Name")) {
                        lastSavedLabel.setText("Pet's Name: " + gameData.get("Name").getAsString());
                    }

                    if (gameData.has("Playtime")) {
                        playtimeLabel.setText("Playtime: " + gameData.get("Playtime").getAsString());
                    }

                    if (gameData.has("Score")) {
                        scoreLabel.setText("Score: " + gameData.get("Score").getAsInt());
                    }

                } catch (Exception exception) {
                    System.out.println("Error parsing JSON from " + filePath);
                    System.out.println(exception);
                }
            }
        } else {
            System.out.println("File NOT found!");
        }

        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new CustomFont("ExtraBold", 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Control Panel (Checkbox + Button)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        controlPanel.setBackground(bgColor);

        JCheckBox deleteBox = new JCheckBox("Delete");
        deleteBox.setFont(new CustomFont("Plain", 14));
        deleteBox.setBackground(bgColor);

        JButton loadButton = new JButton("Load Game");
        loadButton.setBackground(new Color(0, 137, 255));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(new CustomFont("Bold", 14));
        loadButton.setFocusPainted(false);
        loadButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 20, 5, 20))
        );

        controlPanel.add(deleteBox);
        controlPanel.add(loadButton);

        deleteBox.addActionListener(e -> {
            if (deleteBox.isSelected()) {
                selectedFiles.add(file);
            } else {
                selectedFiles.remove(file);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(title + " Button pressed");

                String filePath = "src/Data/" + file;
                loadDataFile.setJsonFile(filePath);

                SwingUtilities.invokeLater(() -> new Gameplay(file));
                window.dispose();
                Playtime.startSession();
            }
        });

        panel.add(controlPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates a new game slot with default data.
     * @param title the title of the game slot
     * @param bgColor the background color of the game slot
     * @param file the file containing the story and text of the game
     * @return the new game slot panel
     */
    private JPanel createNewGameSlot(String title, Color bgColor, String file) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new CustomFont("Bold", 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // New button
        JButton newButton = new JButton("New Game");
        newButton.setBackground(new Color(0, 137, 255)); // Blue Background
        newButton.setForeground(Color.WHITE);
        newButton.setFont(new CustomFont("Bold", 14));
        newButton.setFocusPainted(false);
        newButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        panel.add(newButton, BorderLayout.SOUTH);

        // Creates a new game, redirects to comic scene and gameplay
        newButton.addActionListener(e -> {
            createNewSaveFile(file);
            SaveSlot.set(file); // Set the current save slot
            window.dispose();
            ComicScene comicScene = new ComicScene(file);
        });
        return panel;
    }

    /**
     * Creates a new JSON file for the data of a save slot.
     * @param filename the path to the file name
     */
    private void createNewSaveFile(String filename) {
        String filePath = "src/Data/" + filename;
        try {
            // Randomly chooses the 4 types of pets and creates JSON structure
            // String[] pets = {"Turtle", "Bison", "Beaver", "Hawk"}; // Real Pets
            String[] pets = {"Turtle", "Bison", "Hawk"}; // Real Pets

            String selectedPet = pets[new Random().nextInt(pets.length)];

            // Writing properties into the JSON file
            JsonObject gameData = new JsonObject();
            JsonObject food = new JsonObject(); // Players items
            JsonObject items = new JsonObject(); // Players items
            food.addProperty("Milk Tea", 3);
            food.addProperty("Apple", 3);
            food.addProperty("Water", 0);
            food.addProperty("Cookie", 0);
            food.addProperty("Cake", 0);
            food.addProperty("Meat", 3);
            food.addProperty("Croissant", 0);
            food.addProperty("Blueberries", 0);
            food.addProperty("Broccoli", 0);
            food.addProperty("Chocolate", 0);

            items.addProperty("Bone", 0);
            items.addProperty("Toy", 3);
            items.addProperty("Paint", 3);
            items.addProperty("Ball", 3);
            items.addProperty("Music", 0);

            gameData.addProperty("Saved", "2025-01-01 00:00"); // Last saved time
            gameData.addProperty("Playtime", "0h 0m"); // Playtime duration
            gameData.addProperty("Name", "Spirit Animal"); // Name of pet
            gameData.addProperty("Species", selectedPet); // Pet Type
            gameData.addProperty("State", "happy"); // Pet mood
            gameData.addProperty("Health", 100); // Health meter
            gameData.addProperty("Happiness", 100); // Fullness meter
            gameData.addProperty("Sleep", 100); // Sleepiness meter
            gameData.addProperty("Fullness", 100); // Fullness meter
            gameData.addProperty("Score", 0); // Overall score of player
            gameData.addProperty("ScreenTimeLimit", "00:00"); // Add default screen time limit
            gameData.addProperty("TotalPlaytime", "00:00"); // Add total playtime
            gameData.addProperty("LastPlaytime", "00:00"); // Add last time played
            gameData.addProperty("AveragePlaytime", "00:00"); // Add average playtime
            gameData.addProperty("livingState", 0); // Living state of pet
            gameData.add("food", food);
            gameData.add("item", items);

            // Write to file
            Files.write(Paths.get(filePath), gameData.toString().getBytes());
        } catch (IOException fileException) {
            System.out.println(fileException);
        }
    }

    /**
     * Creates a game slot with existing data or new data depending on if the file exists.
     * @param title the title of the game file
     * @param filename the file name
     * @return the game slot panel
     */
    private JPanel createSlotPanel(String title, String filename) {
        String filePath = "src/Data/" + filename;
        if (Files.exists(Paths.get(filePath))) {
            return createLoadGameSlot(title, Color.LIGHT_GRAY, filename);
        } else {
            return createNewGameSlot(title, Color.LIGHT_GRAY, filename);
        }
    }
}