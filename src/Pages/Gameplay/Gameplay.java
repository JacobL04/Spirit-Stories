package src.Pages.Gameplay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import src.Pages.MainMenu.MainMenu;
import src.Pages.ParentalPage.Playtime;
import src.Pages.components.CustomFont;
import src.Pages.components.LoadDataFile;
import src.Pages.components.Popup;
import src.Pages.components.RoundedIconButton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Timer;
import java.util.TimerTask;
import java.nio.file.Paths;
import java.nio.file.Files;

import src.Pages.components.SaveSlot;

/**
 * The Gameplay classes is the gameplay of the entire game where the user interacts with the pet.
 * <p>
 * The gameplay consists of the user interacting with their pet by feeding and playing with the pet,
 * taking them to the vet, giving them gifts, etc. The settings can also be edited inside this window.
 * Health, sleep, hunger, and happiness values will change depending on how the user treats the pet. 
 * The score increases the more the user treats the pet well
 * <p>
 * @version 1.0
 */
public class Gameplay {
    private ScheduledExecutorService executor;
    private JsonObject currentJson;
    public boolean isRunning = true; // true if game timer is on
    public JFrame window;
    public Container con;
    private JLayeredPane layeredPane;
    private String filename;
    private String petType;
    private int livingState; // 1 = alive, 0 = sleeping, -1 = dead
    private boolean isRenaming = false; // checks if players naming pet

    private int healthValue;
    private int happinessValue;
    private int sleepValue;
    private int hungerValue;
    private int averageStats;
    private int score;
    private int rewardScore = 50; // Gives rewards every 50 points

    private JLabel healthLabel;
    private JLabel happinessLabel;
    private JLabel sleepLabel;
    private JLabel hungerLabel;
    private JLabel scoreLabel;

    private JLabel petLabel;
    private JLabel smileyLabel;

    private JPanel buttonPanel;
    private int feedKey;
    private int sleepKey;
    private int giftKey;
    private int vetKey;
    private int playKey;
    private int inventoryKey;

    /**
     * Creates the gameplay window and displays all the features on the screen.
     * @param filename the name of the file containing the game data
     */
    public Gameplay(String filename) {
        Playtime.startSession(); // Start timing when entering Gameplay
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.filename = filename;
        this.petLabel = new JLabel();
        this.smileyLabel = new JLabel();
        startLimitTimer(); // Start screen time limit countdown

        // Creation of JFrame
        window = new JFrame("Gameplay");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        con = window.getContentPane();
        con.setBackground(Color.WHITE);
        con.setLayout(new BorderLayout());

        // Create layered pane for precise positioning
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        layeredPane.add(petLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(smileyLabel, JLayeredPane.PALETTE_LAYER);

        LoadDataFile loadDataFile = new LoadDataFile();
        this.currentJson = loadDataFile.loadJsonObjFromFile("src/Data/" + filename);
        JsonObject json_contents = loadDataFile.loadJsonObjFromFile("src/Data/" + filename);
        this.petType = json_contents.get("Species").getAsString();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Screen size

        // Settings button at top left
        JButton settings = new RoundedIconButton(loadIcon("src/Pages/assets/settings.png", 60, 60));
        settings.setBounds(20, 20, 60, 60);
        settings.addActionListener(e -> { // Open settings window
            SwingUtilities.invokeLater(() -> Popup.getInstance(Settings.class, this));
        });
        layeredPane.add(settings, JLayeredPane.PALETTE_LAYER);

        // Stats panel at top right
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBounds(screenSize.width - 250, 10, 250, 230);

        healthValue = json_contents.get("Health").getAsInt();
        happinessValue = json_contents.get("Happiness").getAsInt();
        sleepValue = json_contents.get("Sleep").getAsInt();
        hungerValue = json_contents.get("Fullness").getAsInt();
        livingState = json_contents.get("livingState").getAsInt();
        score = json_contents.get("Score").getAsInt();

        averageStats = (int) ((healthValue + happinessValue + sleepValue + hungerValue) / 4);
        rewardScore = ((score / 50) + 1) * 50; // keeps track of rewards of intervals of 50

        healthLabel = indicatorLabels("Health", healthValue, 0);
        happinessLabel = indicatorLabels("Happiness", happinessValue, 0);
        sleepLabel = indicatorLabels("Sleep", sleepValue, 0);
        hungerLabel = indicatorLabels("Hunger", hungerValue, 0);
        scoreLabel = indicatorLabels("Score", score, 1);

        statsPanel.add(healthLabel);
        statsPanel.add(happinessLabel);
        statsPanel.add(sleepLabel);
        statsPanel.add(hungerLabel);
        statsPanel.add(scoreLabel);

        layeredPane.add(statsPanel, JLayeredPane.PALETTE_LAYER);

        // Create button panel
        buttonPanel = createButtonActionPanel();

        // Position button panel below the pet
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(layeredPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add mainPanel to the content pane
        con.add(mainPanel, BorderLayout.CENTER);

        // Player can rename pet
        JLabel nameLabel = createNameLabel(json_contents.get("Name").getAsString());
        layeredPane.add(nameLabel);

        JTextField renameField = new JTextField("Enter Pet Name");
        Dimension nameScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int fieldX = (nameScreenSize.width - 300) / 2; // Centers horizontally
        int fieldY = (nameScreenSize.height - 700) / 5 + 60;
        renameField.setBounds(fieldX, fieldY, 300, 30);
        renameField.setFont(new CustomFont("Medium", 18));
        layeredPane.add(renameField, JLayeredPane.POPUP_LAYER);

        renameField.addActionListener(e -> {
            String newName = renameField.getText().trim();
            if (!newName.isEmpty()) {
                nameLabel.setText(newName);
                LoadDataFile loadData = new LoadDataFile();
                JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
                jsonContents.addProperty("Name", newName);
                loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents); // Writes pets name into JSON
                renameField.setVisible(false);
                isRenaming = false;
                refreshButtonPanel();
            }
            renameField.setText("");
        });
        
        // Game timer
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            if (!isRunning) return;

            updatePetImage(); // Refreshes pets assets every 1 second
            buttonPanel = createButtonActionPanel();

            window.validate();
            window.repaint();

            window.setVisible(true);

            updateData(json_contents, "livingState", livingState);

            if (livingState == 1) { // If pet is alive

                System.out.println("Pet is alive");
                
                if (sleepValue >= 0 && hungerValue >= 0 && happinessValue >= 0) { // If stats are all above 0 then we preform our normal game logic
                    averageStats = (int) ((healthValue + happinessValue + sleepValue + hungerValue) / 4);
                    score += 5;
                    scoreLabel.setText("Score: " + score);
                    updateData(json_contents, "Score", score);
                    checkForScoreReward();

                    if (sleepValue > 0) {
                        if (petType.equals("Turtle")) {
                            sleepValue -= 2;
                            if (sleepValue < 0) sleepValue = 0;
                            sleepLabel.setText("Sleep: " + sleepValue + "%");
                            updateData(json_contents, "Sleep", sleepValue);
                        }

                        else if (petType.equals("Bison")) { // Bison gets tired faster
                            sleepValue -= 5;
                            if (sleepValue < 0) sleepValue = 0;
                            sleepLabel.setText("Sleep: " + sleepValue + "%");
                            updateData(json_contents, "Sleep", sleepValue);
                        }

                        else if (petType.equals("Hawk")) {
                            sleepValue -= 2;
                            if (sleepValue < 0) sleepValue = 0;
                            sleepLabel.setText("Sleep: " + sleepValue + "%");
                            updateData(json_contents, "Sleep", sleepValue);
                        }
                        
                        else {
                            sleepValue -= 2;
                            if (sleepValue < 0) sleepValue = 0;
                            sleepLabel.setText("Sleep: " + sleepValue + "%");
                            updateData(json_contents, "Sleep", sleepValue);
                        }
                    }

                    if (hungerValue > 0) {
                        if (petType.equals("Turtle")) { // Turtle doesn't get as hungry
                            hungerValue -= 2;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);
                        }

                        else if (petType.equals("Bison")) {
                            hungerValue -= 5;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);
                        }

                        else if (petType.equals("Hawk")) { // Hawk gets hungry fast
                            hungerValue -= 7;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);
                            
                        }
                        
                        else {
                            hungerValue -= 5;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);
                        }   
                    }

                    if (hungerValue <= 0) { // All pets starve at the same rate
                        healthValue -= 10;
                        if (healthValue < 0) healthValue = 0;
                        healthLabel.setText("Health: " + healthValue + "%");
                        updateData(json_contents, "Health", healthValue);
                    }

                    if (happinessValue > 0) {
                        if (petType.equals("Turtle")) {
                            happinessValue -= 5;
                            if (happinessValue < 0) happinessValue = 0;
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue);
                        }

                        else if (petType.equals("Bison")) { // Bison gets sad slower 
                            happinessValue -= 2;
                            if (happinessValue < 0) happinessValue = 0;
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue);
                        }

                        else if (petType.equals("Hawk")) { // Hawk gets sad faster
                            happinessValue -= 7;
                            if (happinessValue < 0) happinessValue = 0;
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue);
                            
                        }
                        
                        else {
                            happinessValue -= 2;
                            if (happinessValue < 0) happinessValue = 0;
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue);
                        }
                    }

                    if (happinessValue <= 0) {
                        healthValue -= 5;
                        if (healthValue < 0) healthValue = 0;
                        healthLabel.setText("Health: " + healthValue + "%");
                        updateData(json_contents, "Health", healthValue);
                    }

                    else {
                        System.out.println("Sleep value reached 0, timer stopped.");
                    }

                }

            } else if (livingState == 0) {

                System.out.println("Sleeping");

                if (sleepValue < 100) {
                    if (sleepValue == 0) {
                        healthValue -= 5; // health penalty is applied
                        healthLabel.setText("Health: " + healthValue + "%");
                        updateData(json_contents, "Health", healthValue);
                    }

                    if (petType.equals("Turtle")) { 
                        sleepValue += 5;
                        sleepValue = Math.min(sleepValue, 100);
                        sleepLabel.setText("Sleep: " + sleepValue + "%");
                        updateData(json_contents, "Sleep", sleepValue);

                        // Hunger ticking
                        if (hungerValue > 0) {
                            hungerValue -= 2;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);  
                        }

                        // Happiness slightly increases
                        if (happinessValue > 0) {
                            happinessValue += 2;
                            happinessValue = Math.min(happinessValue, 100);
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue); 
                        }
                    }

                    else if (petType.equals("Bison")) {
                        sleepValue += 5;
                        sleepValue = Math.min(sleepValue, 100);
                        sleepLabel.setText("Sleep: " + sleepValue + "%");
                        updateData(json_contents, "Sleep", sleepValue);

                        // Hunger ticking
                        if (hungerValue > 0) {
                            hungerValue -= 2;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);  
                        }

                        // Happiness slightly increases
                        if (happinessValue > 0) {
                            happinessValue += 2;
                            happinessValue = Math.min(happinessValue, 100);
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue); 
                        }
                    }

                    else if (petType.equals("Hawk")) { // Hawk sleeps faster
                        sleepValue += 15;
                        sleepValue = Math.min(sleepValue, 100);
                        sleepLabel.setText("Sleep: " + sleepValue + "%");
                        updateData(json_contents, "Sleep", sleepValue);

                        // Hunger ticking
                        if (hungerValue > 0) {
                            hungerValue -= 2;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);  
                        }

                        // Happiness slightly increases
                        if (happinessValue > 0) {
                            happinessValue += 2;
                            happinessValue = Math.min(happinessValue, 100);
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue); 
                        }
                    }

                    else {
                        sleepValue += 5;
                        sleepValue = Math.min(sleepValue, 100);
                        sleepLabel.setText("Sleep: " + sleepValue + "%");
                        updateData(json_contents, "Sleep", sleepValue);

                        // Hunger ticking
                        if (hungerValue > 0) {
                            hungerValue -= 2;
                            if (hungerValue < 0) hungerValue = 0;
                            hungerLabel.setText("Hunger: " + hungerValue + "%");
                            updateData(json_contents, "Fullness", hungerValue);  
                        }

                        // Happiness slightly increases
                        if (happinessValue > 0) {
                            happinessValue += 2;
                            happinessValue = Math.min(happinessValue, 100);
                            happinessLabel.setText("Happiness: " + happinessValue + "%");
                            updateData(json_contents, "Happiness", happinessValue); 
                        }
                    }

                    score += 5;
                    scoreLabel.setText("Score: " + score);
                    updateData(json_contents, "Score", score);
                    checkForScoreReward();

                    // Save the updated sleep value to JSON
                    LoadDataFile loadData = new LoadDataFile();
                    JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
                    jsonContents.addProperty("Sleep", sleepValue);
                    loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents);
                }

                else {
                    livingState = 1; // Wake up the pet
                    JLabel messageLabel = createMessageLabel("Your pet woke up!", new Color(0, 137, 255));
                    new javax.swing.Timer(2000, e -> layeredPane.remove(messageLabel)).start();
                }

                updatePetImage();

            } else if (livingState == -1 || healthValue == 0) {
                livingState = -1;
                happinessValue = 0;
                healthValue = 0;
                sleepValue = 0;
                hungerValue = 0;
                averageStats = 0;

                happinessLabel.setText("Happiness: " + happinessValue + "%");
                healthLabel.setText("Health: " + healthValue + "%");
                sleepLabel.setText("Sleep: " + sleepValue + "%");
                hungerLabel.setText("Hunger: " + hungerValue + "%");

                System.out.println("Pet is dead");

                updatePetImage();

                createMessageLabel("Your pet is dead :(", Color.RED);
            } else {
                throw new RuntimeException("Pet is in another state of being :O");
            }

            window.revalidate();
            window.repaint();
        };

        Runnable updatePet = this::updatePetImage;
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(updatePet, 0, 1, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                // Additional shutdown logic
            } catch (Exception e) {
                // Handle exception
            }
        }));
    }

    /**
     * Creates the button action panel to do the following operations:
     * feed, sleep, gift, vet, play, and open inventory.
     * @return the button action panel
     */
    public JPanel createButtonActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton feed = gameButtons("Feed", "src/Pages/assets/Feed.png");
        JButton sleep = gameButtons("Sleep", "src/Pages/assets/Moon.png");
        JButton gift = gameButtons("Gift", "src/Pages/assets/Present.png");
        JButton vet = gameButtons("Vet", "src/Pages/assets/Medkit.png");
        JButton play = gameButtons("Play", "src/Pages/assets/Play.png");
        JButton inventory = gameButtons("Inventory", "src/Pages/assets/Inventory.png");

        // Set all elements to be focusable
        panel.setFocusable(true);
        feed.setFocusable(true);
        sleep.setFocusable(true);
        gift.setFocusable(true);
        vet.setFocusable(true);
        play.setFocusable(true);
        inventory.setFocusable(true);

        // Player Action Button Event Listeners
        feed.addActionListener(e -> feedPet());
        sleep.addActionListener(e -> putPetToSleep(sleepLabel));
        gift.addActionListener(e -> giftPet());
        vet.addActionListener(e -> {
            vet.setEnabled(false); // Disable the button for 10 seconds
            takePetToVet(healthLabel, happinessLabel); 
            new javax.swing.Timer(10000, event -> {
                vet.setEnabled(true); // Re-enable after 10 seconds
            }).start();
        });

        play.addActionListener(e -> {
            play.setEnabled(false); // Disable the button for 10 seconds
            playWithPet(sleepLabel, happinessLabel);
            new javax.swing.Timer(10000, event -> {
                play.setEnabled(true); // Re-enable after 10 seconds
            }).start();
        });
        inventory.addActionListener(e -> { // Open inventory window
            SwingUtilities.invokeLater(() -> Popup.getInstance(Inventory.class, filename));
        });

        // Load Settings JSON Data
        LoadDataFile loadData = new LoadDataFile("Settings.json");
        JsonObject jsonContents = loadData.getGameDataObject().get("keybinds").getAsJsonObject();

        String feedKeyString = jsonContents.get("Feed").getAsString();
        this.feedKey = KeyEvent.getExtendedKeyCodeForChar(feedKeyString.charAt(0));

        String sleepKeyString = jsonContents.get("Sleep").getAsString();
        this.sleepKey = KeyEvent.getExtendedKeyCodeForChar(sleepKeyString.charAt(0));

        String giftKeyString = jsonContents.get("Gift").getAsString();
        this.giftKey = KeyEvent.getExtendedKeyCodeForChar(giftKeyString.charAt(0));

        String vetKeyString = jsonContents.get("Vet").getAsString();
        this.vetKey = KeyEvent.getExtendedKeyCodeForChar(vetKeyString.charAt(0));

        String playKeyString = jsonContents.get("Play").getAsString();
        this.playKey = KeyEvent.getExtendedKeyCodeForChar(playKeyString.charAt(0));

        String inventoryKeyString = jsonContents.get("Inventory").getAsString();
        this.inventoryKey = KeyEvent.getExtendedKeyCodeForChar(inventoryKeyString.charAt(0));

        // Add key bindings to the panel
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        // Feed - F key
        inputMap.put(KeyStroke.getKeyStroke(this.feedKey, 0), "feed"); // Make sure works on inventory menu
        actionMap.put("feed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming) return;
                feedPet();
            }
        });

        // Sleep - S key
        inputMap.put(KeyStroke.getKeyStroke(this.sleepKey, 0), "sleep");
        actionMap.put("sleep", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming) return;
                putPetToSleep(sleepLabel);
            }
        });

        // Gift - G key
        inputMap.put(KeyStroke.getKeyStroke(this.giftKey, 0), "gift");
        actionMap.put("gift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming) return;
                giftPet();
            }
        });

        // Vet - V key
        inputMap.put(KeyStroke.getKeyStroke(this.vetKey, 0), "vet");
        actionMap.put("vet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming) return;
                takePetToVet(healthLabel, happinessLabel);
            }
        });

        // Play - P key
        inputMap.put(KeyStroke.getKeyStroke(this.playKey, 0), "play");
        actionMap.put("play", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming || !play.isEnabled()) return; // Check cooldown
                play.setEnabled(false);
                playWithPet(sleepLabel, happinessLabel);
                new javax.swing.Timer(10000, ev -> play.setEnabled(true)).start();
            }
        });

        // Inventory - I key
        inputMap.put(KeyStroke.getKeyStroke(this.inventoryKey, 0), "inventory");
        actionMap.put("vet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRenaming || !vet.isEnabled()) return; // Check cooldown
                vet.setEnabled(false);
                takePetToVet(healthLabel, happinessLabel);
                new javax.swing.Timer(10000, ev -> vet.setEnabled(true)).start();
            }
        });

        panel.add(feed);
        panel.add(sleep);
        panel.add(gift);
        panel.add(vet);
        panel.add(play);
        panel.add(inventory);

        // Requset focus for the pannel to ensure it can receive the key events
        panel.requestFocusInWindow();

        return panel;
    }

    /**
     * Refreshes the button panel. Usually called when player changes keybindings in settings.
     */
    public void refreshButtonPanel() {
        // Get the main pannel from the content pane
        Component[] components = con.getComponents();
        JPanel mainPanel = null;
        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
                mainPanel = (JPanel) comp;
                break;
            }
        }

        if (mainPanel != null) {
            // Remove the old button panel from mainPanel
            mainPanel.remove(buttonPanel);

            // Create and add the new button panel
            buttonPanel = createButtonActionPanel();
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Refresh the UI
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    /**
     * Updates the game data and writes it into the JSON file.
     * @param jsonContents the JSON object containing the game data
     * @param property the property to update
     * @param value the new value of the property
     */
    private void updateData(JsonObject jsonContents, String property, int value) { // Updates dakta and writes it into the JSON
        LoadDataFile loadData = new LoadDataFile();
        jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
        jsonContents.addProperty(property, value);
        loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents);
    }

    /**
     * Checks if the player has reached the score reward threshold and gives a random item if so.
     */
    private void checkForScoreReward() {
        while (score >= rewardScore) {
            giveRandomItem();
            rewardScore += 50;
        }
    }

    /**
     * Gives a random item to the player as a reward.
     */
    private void giveRandomItem() { // rewards players with random items
        try {
            LoadDataFile loadData = new LoadDataFile();
            JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);

            Random rand = new Random();
            String[] categories = {"food", "item"};
            String category = categories[rand.nextInt(categories.length)];
            JsonObject items = jsonContents.getAsJsonObject(category);

            List<String> itemList = new ArrayList<>(items.keySet());
            if (itemList.isEmpty()) return;

            String item = itemList.get(rand.nextInt(itemList.size()));
            int quantity = rand.nextInt(10) + 1; // from 1 to 10 items

            items.addProperty(item, items.get(item).getAsInt() + quantity);
            loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents); // Writes new changes to JSON

            // Notifies players of items reward
            JLabel messageLabel = createMessageLabel(item + " x" +quantity + " rewarded", new Color(0, 137, 255));
            new javax.swing.Timer(3000, e -> layeredPane.remove(messageLabel)).start();

            System.out.println("Rewarded " + quantity + " " + item + "(s)");
        } catch (Exception e) {
            System.out.println("Error giving reward: " + e.getMessage());
        }
    }

    /**
     * Loads an icon from the specified path and resizes it.
     * @param path the path to the icon
     * @param width the width of the icon
     * @param height the height of the icon
     * @return the resized icon
     */
    private ImageIcon loadIcon(String path, int width, int height) {
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new RuntimeException("Couldn't find file: " + path);
        }

        ImageIcon originalIcon = new ImageIcon(resource);
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    /**
     * Creates GIFs for the pet and sets their positions into the middle of the screen.
     * @param petPath the path to the pet GIF
     * @param petEmotion the emotion of the pet
     * @param screenSize the size of the screen
     **/
    private void createPetGifs(String petPath, String petEmotion, Dimension screenSize) {

        // Load images
        ImageIcon petGIF = new ImageIcon(petPath);
        petGIF.setImage(petGIF.getImage().getScaledInstance(350, 350, Image.SCALE_DEFAULT));
        petLabel.setIcon(petGIF);

        ImageIcon smile = new ImageIcon("src/Pages/assets/" + petEmotion);
        smile.setImage(smile.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        smileyLabel.setIcon(smile);

        // Set positions of previously loaded images
        // Note that these widths and heights below are not the same as the ones above, rather they are the bounds
        int petWidth = 350, petHeight = 275;
        int emotionWidth = 100, emotionHeight = 100;

        int centerX = (screenSize.width - petWidth) / 2;
        int centerY = (screenSize.height - petHeight) / 2;

        petLabel.setBounds(centerX, centerY, petWidth, petHeight);
        smileyLabel.setBounds(centerX + petWidth - emotionWidth, centerY - (emotionHeight / 2), emotionWidth, emotionHeight);
    }

    /**
     * Updates the pet image when called upon.
     */
    private void updatePetImage() {
        String[] petStates = getPetStates();
        String emotionSuffix = petStates[0], petEmotion = petStates[1];
        String currentPetPath = "src/Pages/assets/" + petType + "_" + emotionSuffix;
        createPetGifs(currentPetPath, petEmotion, Toolkit.getDefaultToolkit().getScreenSize());
    }

    /**
     * Starts the countdown based on the screen time limit set in the save file.
     * If time runs out, it will automatically close the gameplay window.
     */
    private void startLimitTimer() {
        try {
            // Read screen limit time from JSON file
            String filePath = "src/Data/" + SaveSlot.get();
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            String limit = json.get("ScreenTimeLimit").getAsString();

            String[] parts = limit.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int millis = (hours * 60 + minutes) * 60 * 1000;

            if (millis > 0) {
                // Start countdown timer
                Timer limitTimer = new Timer();
                limitTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Screen time limit reached. Closing game.");
                        SwingUtilities.invokeLater(() -> {
                            closeGameplay();
                            new MainMenu();
                        });
                    }
                }, millis);
            }
        } catch (Exception e) {
            System.out.println("Failed to start screen time countdown: " + e.getMessage());
        }
    }

    /**
     * Creates a label for the game indicators.
     * @param indicator the name of the indicator
     * @param value the value of the indicator
     * @param isScore checks if it's the score label. 0 for no, anything else it is
     * @return the indicator label
     */
    private JLabel indicatorLabels(String indicator, int value, int isScore) {
        JLabel label;
        if (isScore == 0){
            label = new JLabel(indicator + ": " + value + "%", SwingConstants.RIGHT);
            label.setFont(new CustomFont("SemiBold", 25));
            label.setBorder(new EmptyBorder(20, 0, 5, 30));
            return label;
        }

        label = new JLabel(indicator + ": " + value, SwingConstants.RIGHT);
        label.setFont(new CustomFont("SemiBold", 25));
        label.setBorder(new EmptyBorder(20, 0, 5, 30));
        label.setForeground(new Color(31, 31, 219));

        return label;
    }

    /**
     * Creates a game button with the specified label and icon.
     * @param text the label of the button
     * @param iconPath the path to the icon image
     * @return the created JButton
     */
    private JButton gameButtons(String text, String iconPath) {

        ImageIcon icon = loadIcon(iconPath, 25, 25);
        JButton button = new JButton(text, icon);
        button.setFont(new CustomFont("Medium", 25));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);

        button.addActionListener(e -> {

            System.out.println(e.getActionCommand());

            // Change the background color temporarily
            button.setBackground(Color.LIGHT_GRAY);

            // Create a small delay to simulate a "press" effect
            new javax.swing.Timer(150, temp -> button.setBackground(Color.WHITE)).start();
        });

        return button;
    }

    /**
     * Closes the gameplay window and stops the playtime session and saves the new playtime into it's JSON file
     */
    public void closeGameplay() { // Closes gameplay
        isRunning = false; // stops game timer
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
        saveGameState();
        Playtime.stopSession();  // Stop timing when closing Gameplay
        savePlaytimeToJson();  // Save playtime data to JSON file

        window.dispose();
    }

    /**
     * Saves the current game state to the JSON file associated with the current save slot.
     * This method updates the health, happiness, sleep, fullness, and score values in the JSON file.
     */
    private void saveGameState() {
        // Update the JSON object with the current game state values
        currentJson.addProperty("Health", healthValue);
        currentJson.addProperty("Happiness", happinessValue);
        currentJson.addProperty("Sleep", sleepValue);
        currentJson.addProperty("Fullness", hungerValue);
        currentJson.addProperty("Score", score);

        // Save the updated JSON object to the file
        LoadDataFile loadData = new LoadDataFile();
        loadData.saveJsonObjToFile("src/Data/" + filename, currentJson);
    }

    /**
     * Retrieves the current pet states based on its attributes and emotion values.
     * @return an array containing the emotion suffix and pet emotion
     */
    private String[] getPetStates() {
        if (healthValue == 0 || livingState == -1) {
            livingState = -1;
            return new String[]{"Dead.gif", ""};
        } else if (sleepValue == 0 || livingState == 0) {
            livingState = 0;
            return new String[]{"Sleep.gif", ""};
        } else if (averageStats < 25 || happinessValue < 25 || healthValue < 25 || hungerValue < 25 || sleepValue < 25) {
            return new String[]{"Sad.gif", "Sad.gif"};
        } else if (averageStats >= 30 && averageStats < 75) {
            return new String[]{"Neutral.gif", "Neutral.gif"};
        } else if (averageStats >= 75) {
            return new String[]{"Happy.gif", "Smile.gif"};
        } else {
            throw new RuntimeException("Error, pet is soulless. No emotions :(");
        }
    }

    /**
     * Feeds the pet and updates the hunger value.
     */
    private void feedPet() {
        if (livingState == 0 || livingState == -1){ // prevent eating when pet is sleeping
            return;
        }

        System.out.println("Feeding Pet");
        Popup currentPopup = Popup.getCurrentInstance();

        if (currentPopup instanceof Inventory playerInventory) {
            List<String> selectedFood = playerInventory.getSelectedItems();

            // Load JSON data
            LoadDataFile loadData = new LoadDataFile();
            JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
            JsonObject itemsObject = jsonContents.getAsJsonObject("food");

            // Decrement quantity for EACH selected item
            for (String food : selectedFood) {
                if (itemsObject.has(food)) {
                    int currentQty = itemsObject.get(food).getAsInt();
                    if (currentQty > 0) {
                        itemsObject.addProperty(food, currentQty - 1); // Decrease food qty

                        // Increase health & hunger and cap it at 100
                        healthValue = Math.min(healthValue + 5, 100);
                        hungerValue = Math.min(hungerValue + 10, 100);
                        happinessValue = Math.min(happinessValue + 1, 100);

                        // Updates JSON health value
                        jsonContents.addProperty("Health", healthValue);
                        jsonContents.addProperty("Fullness", hungerValue);
                        jsonContents.addProperty("Happiness", happinessValue);
                    }
                }
            }

            healthLabel.setText("Health: " + healthValue + "%");
            hungerLabel.setText("Hunger: " + hungerValue + "%");
            happinessLabel.setText("Happiness " + happinessValue + "%");
            averageStats = (healthValue + happinessValue + sleepValue + hungerValue) / 4;

            // Save changes to JSON file
            loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents);
            System.out.println("Updated items: " + selectedFood);

            playerInventory.dispose(); // Close inventory
            SwingUtilities.invokeLater(() -> Popup.getInstance(Inventory.class, filename)); // Reopens inventory

            updatePetImage();
        } else {
            System.out.println("Inventory not open, no changes made.");
        }
    }

    /**
     * Puts the pet to sleep and updates the sleep value.
     * @param sleepLabel the JLabel displaying the sleep value
     */
    private void putPetToSleep(JLabel sleepLabel) {
        if (livingState == 0 || livingState == -1){
            return;
        }

        if (sleepValue >= 70) {
            sleepValue = 100;
            livingState = 1;
            return;
        }

        if (sleepValue <= 40) {
            livingState = 0;
//            sleepValue += 5;
            sleepValue = Math.min(sleepValue, 100);
            sleepLabel.setText("Sleep: " + sleepValue + "%");

            // Save the updated sleep value to JSON
            LoadDataFile loadData = new LoadDataFile();
            JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
            jsonContents.addProperty("Sleep", sleepValue);
            loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents);

            JLabel messageLabel = createMessageLabel("Your pet is now sleeping!", new Color(0, 137, 255));
            new javax.swing.Timer(2000, e -> layeredPane.remove(messageLabel)).start();
        } else {
            JLabel messageLabel = createMessageLabel("Your pet isn't too tired yet!", Color.YELLOW);
            new javax.swing.Timer(2000, e -> layeredPane.remove(messageLabel)).start();
        }

        updatePetImage();
    }

    /**
     * Creates a JLabel to display a message with the specified text and background color.
     * The label is positioned at the center of the screen.
     * @param message the text to display in the label
     * @param color the background color of the label
     * @return the created JLabel
     */
    private JLabel createMessageLabel(String message, Color color) {
        // Create a new JLabel with the specified message and center alignment
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new CustomFont("Medium", 20)); // Set custom font
        messageLabel.setOpaque(true); // Make the label background visible
        messageLabel.setBackground(color); // Set the background color
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the text

        // Add the label to the layered pane at the popup layer
        layeredPane.add(messageLabel, JLayeredPane.POPUP_LAYER);

        // Get the screen size to position the label at the center
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        messageLabel.setBounds((screenSize.width - 300) / 2, (screenSize.height - 50) / 5, 300, 50);

        return messageLabel; // Return the created label
    }

    private JLabel createNameLabel(String message) { // Players pet animal name
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new CustomFont("Medium", 20));
        messageLabel.setOpaque(true);
        // messageLabel.setBackground();
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        layeredPane.add(messageLabel, JLayeredPane.POPUP_LAYER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        messageLabel.setBounds((screenSize.width - 300) / 2, (screenSize.height - 700) / 5, 300, 50);

        return messageLabel;
    }

    private void giftPet() {
        if (livingState == 0 || livingState == -1){ // prevent eating when pet is sleeping
            return;
        }

        System.out.println("Gifting");
        Popup currentPopup = Popup.getCurrentInstance();

        if (currentPopup instanceof Inventory playerInventory) {
            List<String> selectedItems = playerInventory.getSelectedItems();

            // Load JSON data
            LoadDataFile loadData = new LoadDataFile();
            JsonObject jsonContents = loadData.loadJsonObjFromFile("src/Data/" + filename);
            JsonObject itemsObject = jsonContents.getAsJsonObject("item");

            // Decrement quantity for EACH selected item
            for (String item : selectedItems) {
                if (itemsObject.has(item)) {
                    int currentQty = itemsObject.get(item).getAsInt();
                    if (currentQty > 0) {
                        itemsObject.addProperty(item, currentQty - 1); // Decrease food qty
                        happinessValue = Math.min(happinessValue + 5, 100); // Increase happiness and cap it at 100
                        System.out.println("Happy: "+happinessValue);
                        jsonContents.addProperty("Happiness", happinessValue); // Updates JSON happiness value
                    }
                }
            }

            happinessLabel.setText("Happiness: " + happinessValue + "%");
            averageStats = (healthValue + happinessValue + sleepValue + hungerValue) / 4;

            // Save changes to JSON file
            loadData.saveJsonObjToFile("src/Data/" + filename, jsonContents);
            System.out.println("Updated items: " + selectedItems);

            playerInventory.dispose(); // Close inventory
            SwingUtilities.invokeLater(() -> Popup.getInstance(Inventory.class, filename)); // Reopens inventory

            updatePetImage();
        } else {
            System.out.println("Inventory not open, no changes made.");
        }

    }

    /**
     * Takes the pet to the vet and updates the health and happiness values.
     * @param healthLabel the JLabel displaying the health value
     * @param happinessLabel the JLabel displaying the happiness value
     */
    private void takePetToVet(JLabel healthLabel, JLabel happinessLabel) {

        if (livingState == 0 || livingState == -1){ // prevent eating when pet is sleeping
            return;
        }

        healthValue += 50;
        if (healthValue >= 100) {
            healthValue = 100;
        }

        happinessValue -= 25;
        if (happinessValue <= 0) {
            happinessValue = 0;
        }

        score -= 10;
        if (score <=0) {
            score = 0;
        }

        averageStats = (int) ((healthValue + happinessValue + sleepValue + hungerValue) / 4);

        healthLabel.setText("Health: " + healthValue + "%");
        happinessLabel.setText("Happiness: " + happinessValue + "%");
        scoreLabel.setText("Score: " + score);


        updatePetImage();

        JLabel messageLabel = createMessageLabel("Doctors are so mean... :(", Color.RED);

        // Remove the message after 2 seconds
        new javax.swing.Timer(2000, e -> layeredPane.remove(messageLabel)).start();

    }

    /**
     * Plays with the pet and updates the sleep and happiness values.
     * @param sleepLabel the JLabel displaying the sleep value
     * @param happinessLabel the JLabel displaying the happiness value
     */
    private void playWithPet(JLabel sleepLabel, JLabel happinessLabel) {

        if (livingState == 0 || livingState == -1){ // prevent eating when pet is sleeping
            return;
        }

        sleepValue -= 20;
        if (sleepValue <= 0) {
            sleepValue = 0;
        }

        happinessValue += 30;
        if (happinessValue >= 100) {
            happinessValue = 100;
        }

        hungerValue -= 5;
        if (hungerValue <= 0){
            hungerValue = 0;
        }

        averageStats = (int) ((healthValue + happinessValue + sleepValue + hungerValue) / 4);

        sleepLabel.setText("Sleep: " + sleepValue + "%");
        happinessLabel.setText("Happiness: " + happinessValue + "%");
        hungerLabel.setText("Hunger: " + hungerValue + "%");

        updatePetImage();

        JLabel messageLabel = createMessageLabel("Your pet happier now!", Color.GREEN);

        // Remove the message after 2 seconds
        new javax.swing.Timer(2000, e -> layeredPane.remove(messageLabel)).start();

        JLabel messageLabel2 = createMessageLabel("Cannot play for 10 sec", Color.RED);

        // Remove the message after 3 seconds
        new javax.swing.Timer(3000, e -> layeredPane.remove(messageLabel2)).start();
    }

    /**
     * Saves the playtime data to the JSON file associated with the current save slot.
     * This method updates the total playtime, last playtime, and average playtime in the JSON file.
     */
    private void savePlaytimeToJson() {
        try {
            // Get the file path for the current save slot
            String filePath = "src/Data/" + SaveSlot.get();
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();

            // Update playtime properties in the JSON object
            json.addProperty("TotalPlaytime", Playtime.getTotalPlayTime());
            json.addProperty("LastPlaytime", Playtime.getLastPlayTime());
            json.addProperty("AveragePlaytime", Playtime.getAveragePlayTime());

            // Write the updated JSON object back to the file with pretty printing
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.write(Paths.get(filePath), gson.toJson(json).getBytes());
            System.out.println("Playtime data saved to JSON.");
        } catch (Exception e) {
            // Handle any exceptions that occur during the file read/write process
            System.out.println("Failed to save playtime to JSON: " + e.getMessage());
        }
    }
}
