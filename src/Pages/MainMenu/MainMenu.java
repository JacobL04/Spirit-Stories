package src.Pages.MainMenu;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import src.Pages.Achievements.Achievement;
import src.Pages.Gameplay.Gameplay;
import src.Pages.Gameplay.Settings;
import src.Pages.LoadGame.LoadGame;
import src.Pages.ParentalPage.ParentalPage;
import src.Pages.ParentalPage.Playtime;
import src.Pages.components.*;
import src.Pages.components.Popup;
import src.Pages.components.Window;

/**
 * The MainMenu class represents the main menu of the game
 * <p>
 * It extends the Window class and provides functionality to create and display
 * the main menu with options such as New Game, Load Game, Tutorial, and Exit Game.
 * It also includes icons to access settings, achievements, and parental controls.
 * <p>
 * @version 1.0
 */
public class MainMenu extends Window {
    JLabel title;
    // Main menu buttons
    JButton newGame = new JButton("New Game");
    JButton loadGame = new JButton("Load Game");
    JButton tutorial = new JButton("Tutorial");
    JButton exitGame = new JButton("Exit Game");

    // Create icons
    private ImageIcon settingsIcon;
    private ImageIcon achievementsIcon;
    private ImageIcon parentalIcon;

    private boolean saveFileExists = false;

    static { // Plays background music
        PlayAudio.PlayMusic("src/Pages/assets/bamblaist.wav", -5.0f);
    }

    /**
     * Constructs the main menu with the title "Spiritstories".
     */
    public MainMenu() {
        this.title = new JLabel("Spiritstories");
        title.setFont(new CustomFont("VariableFont_wght", 100));
        createMenu();
    }

    /**
     * Creates an icon that can be put into a button.
     * @param imagePath the path to the image
     * @param width width of the image
     * @param height height of the image
     * @return the image in the form of an icon
     */
    public ImageIcon createIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        // Scale the icon to fit the button size
        Image scaledIcon = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledIcon);
        return icon;
    }

    /**
     * Creates the layout of the main menu of the game and the functionality of each button
     */
    private void createMenu() {
        // Creates a grid layout for the main menu(similar to HTML grid layout)
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Load icons
        settingsIcon = createIcon("src/Pages/assets/settings.png", 40, 40);
        achievementsIcon = createIcon("src/Pages/assets/achievements.png", 40, 40);
        parentalIcon = createIcon("src/Pages/assets/parental.png", 40, 40);

        // Create buttons with a hover effect and round borders with a class that extends JButton
        JButton settings = new RoundedIconButton(settingsIcon);
        JButton achievements = new RoundedIconButton(achievementsIcon);
        JButton parental = new RoundedIconButton(parentalIcon);

        // Add title to layout
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(175, 0, 20, 0);
        title.setFont(new Font("Montserrat", Font.BOLD, 80));
        add(title, gbc);

        // Creates a box layout JPanel of flow layout panels for the buttons to make each of them centered
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        // Create, center, and remove borders for every button/game option
        JPanel newGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // If a user already has a game file, then change the new game button to continue game
        saveFileExists = Files.exists(Paths.get("src/Data/last_saved.txt")); // Checks if a save file exists
        if (saveFileExists) {
            newGame.setText("Continue Game");
        }
        newGamePanel.add(newGame);
        newGame.setBorderPainted(false);
        newGame.setBorder(null);
        gamePanel.add(newGamePanel);

        JPanel loadGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loadGamePanel.add(loadGame);
        loadGame.setBorderPainted(false);
        loadGame.setBorder(null);
        gamePanel.add(loadGamePanel);

        JPanel tutorialPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tutorialPanel.add(tutorial);
        tutorial.setBorderPainted(false);
        tutorial.setBorder(null);
        gamePanel.add(tutorialPanel);

        JPanel exitGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitGamePanel.add(exitGame);
        exitGame.setBorderPainted(false);
        exitGame.setBorder(null);
        gamePanel.add(exitGamePanel);

        // Make the font of the buttons' a custom font
        Font fixedWidthFont = new CustomFont("VariableFont_wght", 50);
        newGame.setFont(fixedWidthFont);
        loadGame.setFont(fixedWidthFont);
        tutorial.setFont(fixedWidthFont);
        exitGame.setFont(fixedWidthFont);
        newGame.setContentAreaFilled(false);
        loadGame.setContentAreaFilled(false);
        tutorial.setContentAreaFilled(false);
        exitGame.setContentAreaFilled(false);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 100, 0);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 1;
        add(gamePanel, gbc);

        // Bottom icons (Settings + Achievements + Parental)
        JPanel bottomIcons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomIcons.add(settings);
        bottomIcons.add(achievements);
        bottomIcons.add(parental);

        // Set size for bottom icon panel and buttons
        bottomIcons.setPreferredSize(new Dimension(400, 80));
        settings.setPreferredSize(new Dimension(50, 50));
        achievements.setPreferredSize(new Dimension(50, 50));
        parental.setPreferredSize(new Dimension(50, 50));

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(bottomIcons, gbc);

        // Hover effect that changes the text in the button
        if (saveFileExists) {
            hoverText(newGame, "Continue Game");
        } else {
            hoverText(newGame, "New Game");
        }
        hoverText(loadGame, "Load Game");
        hoverText(tutorial, "Tutorial");
        hoverText(exitGame, "Exit Game");

        // Open settings popup
        settings.addActionListener(e -> { // Open settings window
            PlayAudio.PlaySound("src/Pages/assets/Icon_pop.wav", -15.0f);
            SwingUtilities.invokeLater(() -> Popup.getInstance(Settings.class, this));
        });

        // Open achievements screen
        achievements.addActionListener(e -> {
            dispose();
            PlayAudio.PlaySound("src/Pages/assets/Icon_pop.wav", -15.0f);
            SwingUtilities.invokeLater(Achievement::new);
        });

        // Exit game with sound and delay
        exitGame.addActionListener(e -> {
            Playtime.stopSession(); // Stop the playtime timer when exiting the game
            PlayAudio.PlaySound("src/Pages/assets/byebye.wav", 5.0f);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                System.out.println(exception);
            }
            System.exit(0);
        });

        String password = new LoadDataFile("Settings.json").getGameDataObject().get("password").getAsString();

        // Prompt password before accessing parental page
        parental.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "Enter parental control password:\n(Note: Default password is 1234 for first-time access)");
            if (!password.equals(input)) {
                JOptionPane.showMessageDialog(null, "Incorrect password. Access denied.");
                return;
            }

            dispose();
            PlayAudio.PlaySound("src/Pages/assets/Icon_pop.wav", -15.0f);
            SwingUtilities.invokeLater(ParentalPage::new);
        });

        loadGame.addActionListener(e -> {
            // Prevent loading the game if current time is outside the allowed playtime
            if (!isPlaytimeAllowed()) {
                JOptionPane.showMessageDialog(null, "Access denied. Game can only be played during allowed hours.");
                return;
            }

            System.out.println("Deleted window");
            dispose(); // delete current window
            System.out.println("Created new window");
            LoadGame loadGame = new LoadGame();
            Playtime.startSession(); // Start timer when loading a saved game
        });

        newGame.addActionListener(e -> {
            // Prevent continuing the game if current time is outside the allowed playtime
            if (!isPlaytimeAllowed()) {
                JOptionPane.showMessageDialog(null, "Access denied. Game can only be played during allowed hours.");
                return;
            }

            if (!saveFileExists) {
                System.out.println("Deleted window");
                dispose(); // delete current window
                System.out.println("Created new game window");
                Playtime.startSession(); // Start the playtime timer when starting a new game
                new LoadGame();
            } else {
                String filename = SaveSlot.get();
                if (filename != null) {
                    dispose();
                    Gameplay gameplay = new Gameplay(filename);
                    Playtime.startSession();
                }
            }
        });

        tutorial.addActionListener(e -> {
            dispose(); // delete current window
            System.out.println("Created new tutorial window");
            Tutorial tutorial = new Tutorial();
        });
    }

    /**
     * Game text button hover effect helper method. The symbols '>' and '<'
     * will be put outside the text to indicate which button the user is hovering on
     * @param button the button to have the hover effect on
     * @param text the text that should be changed when hovered
     */
    private void hoverText(JButton button, String text) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PlayAudio.PlaySound("src/Pages/assets/Click.wav", -15.0f);
                button.setText("> " + text + " <");
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setText(text);
            }
        });
    }

    /**
     * Verifies whether the current time is within the allowed playtime range.
     * <p>
     * According to the spec: "If a user attempts to play the game outside of the
     * allowed time range, the game should not start." This method checks whether
     * the current time falls within the start and end time specified in the save file.
     * </p>
     *
     * @return true if the game is allowed to be played at the current time; false otherwise.
     */
    private boolean isPlaytimeAllowed() {
        try {
            String filePath = "src/Data/" + SaveSlot.get();
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();

            boolean enabled = json.get("TimeLimitEnabled").getAsBoolean();
            if (!enabled) return true;

            String start = json.get("AllowedStart").getAsString(); // e.g. "08:00"
            String end = json.get("AllowedEnd").getAsString();     // e.g. "20:00"

            LocalTime now = LocalTime.now();
            LocalTime startTime = LocalTime.parse(start);
            LocalTime endTime = LocalTime.parse(end);

            // Handles overnight ranges (e.g., 22:00 to 06:00)
            if (startTime.isBefore(endTime)) {
                return !now.isBefore(startTime) && !now.isAfter(endTime);
            } else {
                return !now.isAfter(endTime) || !now.isBefore(startTime);
            }
        } catch (Exception e) {
            System.out.println("Failed to read time limit: " + e.getMessage());
            return true; // Default to allowed if there is an error
        }
    }
}