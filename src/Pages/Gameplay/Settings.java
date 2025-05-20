package src.Pages.Gameplay;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonObject;

import src.Pages.MainMenu.MainMenu;
import src.Pages.components.CustomFont;
import src.Pages.components.LoadDataFile;
import src.Pages.components.PlayAudio;
import src.Pages.components.Popup;

/**
 * The Settings class represents the settings of the game
 * <p>
 * The settings include changing the music and sound volume with a slider, 
 * and editing the keybindings of the game to a key of the user's choice
 * <p>
 * @version 1.0
 */
public class Settings extends Popup {

    // Game instance
    Gameplay game;
    private Map<String, JSlider> audioSliders = new HashMap<>(); // Stores slider numbers
    private Map<String, JLabel> keyLabels = new HashMap<>(); // Stores key labels
    private JsonObject settingsJson;

    /**
     * Creates the layout of the settings window
     */
    public Settings(Gameplay game) {
        super();
        setTitle("Settings");
        this.game = game;

        // Save settings when window is closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveSettings();
                if (game != null) {
                    game.refreshButtonPanel();
                }
            }
        });

        // Load settings from JSON
        LoadDataFile loadDataFile = new LoadDataFile();
        this.settingsJson = loadDataFile.loadJsonObjFromFile("src/Data/Settings.json");

        JsonObject audioObject = settingsJson.getAsJsonObject("audio");
        int SFXVolume = audioObject.get("SoundEffectVolume").getAsInt();
        int musicVolume = audioObject.get("MusicVolume").getAsInt();

        JPanel settingsPanel = new JPanel(new GridLayout(12, 1, 5, 5));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Make it scrollable
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create header and sliders
        JLabel audioHeader = createHeaderLabel("AUDIO");
        settingsPanel.add(audioHeader);

        settingsPanel.add(createSliderPanel("Sound Effect Volume", SFXVolume, "SoundEffectVolume"));
        settingsPanel.add(createSliderPanel("Music Volume", musicVolume, "MusicVolume"));

        // Create keybinding section
        JLabel keysHeader = createHeaderLabel("KEYBINDINGS");
        settingsPanel.add(keysHeader);

        createKeysPanel(settingsPanel);
        settingsPanel.add(createButtonPanel());

        // Developer credits
        JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        creditsPanel.add(new JLabel("Developers: ", SwingConstants.CENTER));
        creditsPanel.add(new JLabel("Jiwon, Maharshii, Eric, Jennifer & Jacob", SwingConstants.CENTER));
        creditsPanel.add(new JLabel(" - Team 49 created for CS2212 Western University Winter 2025", SwingConstants.CENTER));
        settingsPanel.add(creditsPanel);
        add(scrollPane);
    }

    public Settings(){
        new Settings(null);
        dispose();
    }


    /**
     * Adds the keybindings of the gameplay to the settings page 
     */
    private void createKeysPanel(JPanel panel) {
        JsonObject keybindings = settingsJson.get("keybinds").getAsJsonObject();

        String feedKeyString = keybindings.get("Feed").getAsString();
        panel.add(createKeyBindPanel("Feed", KeyEvent.getExtendedKeyCodeForChar(feedKeyString.charAt(0)), "Feed"));

        String sleepKeyString = keybindings.get("Sleep").getAsString();
        panel.add(createKeyBindPanel("Sleep", KeyEvent.getExtendedKeyCodeForChar(sleepKeyString.charAt(0)), "Sleep"));

        String giftKeyString = keybindings.get("Gift").getAsString();
        panel.add(createKeyBindPanel("Gift", KeyEvent.getExtendedKeyCodeForChar(giftKeyString.charAt(0)), "Gift"));

        String vetKeyString = keybindings.get("Vet").getAsString();
        panel.add(createKeyBindPanel("Vet", KeyEvent.getExtendedKeyCodeForChar(vetKeyString.charAt(0)), "Vet"));

        String playKeyString = keybindings.get("Play").getAsString();
        panel.add(createKeyBindPanel("Play", KeyEvent.getExtendedKeyCodeForChar(playKeyString.charAt(0)), "Play"));

        String inventoryKeyString = keybindings.get("Inventory").getAsString();
        panel.add(createKeyBindPanel("Inventory", KeyEvent.getExtendedKeyCodeForChar(inventoryKeyString.charAt(0)), "Inventory"));
    }

    /**
     * Creates the layout of each button 
     * @return a button with a border
     */
    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(5, 5));
        buttonPanel.add(applyButton(), BorderLayout.WEST);
        buttonPanel.add(exitButton(), BorderLayout.EAST);
        return buttonPanel;
    }

    /**
     * Button that changes the values of the user's changed settings
     * @return the UI and functionality of the apply button
     */
    public JButton applyButton() {
        JButton button = new JButton("Apply");
        button.setFont(new CustomFont("SemiBold", 20));
        button.setOpaque(true);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addActionListener(e -> {
            // Add apply settings logic here
            saveSettings();
            createMessageLabel();
            if (game != null) {
                game.refreshButtonPanel();
            }
            System.out.println("Settings applied");
        });

        return button;
    }

    /**
     * Creates the header of the settings window
     * @param labelText title of the header
     * @return the title
     */
    public JLabel createHeaderLabel(String labelText) {
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new CustomFont("ExtraBold", 40));
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);

        return label;
    }

    /**
     * Create a popup message that notifies the user they updated their settings
     */
    private void createMessageLabel() {
        JLabel messageLabel = new JLabel("Settings Applied!", SwingConstants.CENTER);
        messageLabel.setFont(new CustomFont("Medium", 20));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(0, 128, 0));
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(messageLabel, JLayeredPane.POPUP_LAYER);

        // Calculate the position based on the frame's dimensions
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int labelWidth = 300;
        int labelHeight = 50;
        int x = (frameWidth - labelWidth) / 2;
        int y = (frameHeight - labelHeight) / 2;
        messageLabel.setBounds(x, y, labelWidth, labelHeight);

        Timer timer = new Timer(2000, e -> {
            layeredPane.remove(messageLabel);
            layeredPane.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Exits the settings window and goes back to the main menu window
     * @return the exit button 
     */
    public JButton exitButton() {
        JButton button = new JButton("Return to Main Menu");
        button.setFont(new CustomFont("SemiBold", 20));
        button.setOpaque(true);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addActionListener(e -> {
            dispose();
            if (game != null){
                game.closeGameplay();
                SwingUtilities.invokeLater(MainMenu::new);
            }
        });

        return button;
    }

    /**
     * Saves and updates the audio settings to a JSON file
     */
    private void saveSettings() {
        JsonObject audioObject = settingsJson.getAsJsonObject("audio");
        for (Map.Entry<String, JSlider> entry : audioSliders.entrySet()) {
            audioObject.addProperty(entry.getKey(), entry.getValue().getValue());
        }

        new LoadDataFile().saveJsonObjToFile("src/Data/Settings.json", settingsJson);
    }

    /**
     * Creates the slider for each the music and sound effect feature in the settings window
     * @param labelText text that the slider belongs to
     * @param initialValue initial value of the slider
     * @return the entire panel of the slider
     */
    public JPanel createSliderPanel(String labelText, int initialValue, String jsonKey) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        JSlider slider = new JSlider(0, 100, initialValue);
        JLabel valueLabel = new JLabel(initialValue + "%");

        label.setFont(new CustomFont("SemiBold", 15));
        valueLabel.setFont(new CustomFont("Medium", 15));

        audioSliders.put(jsonKey, slider);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            valueLabel.setText(value + "%");

            if ("MusicVolume".equals(jsonKey)) {
            PlayAudio.setMusicVolume(value);
        }
        });

        panel.add(label, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Creates panel to show the keybindings of the game
     */
    public JPanel createKeyBindPanel(String labelText, int initialKey, String jsonKey) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        JLabel keyLabel = new JLabel(KeyEvent.getKeyText(initialKey));
        label.setFont(new CustomFont("SemiBold", 15));
        keyLabel.setFont(new CustomFont("Medium", 15));
        keyLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(0, 15, 0, 15) // Adjusted padding
        ));

        keyLabels.put(jsonKey, keyLabel);

        keyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                keyLabel.setText("Press a key...");
                keyLabel.requestFocus();
            }
        });

        keyLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyLabel.setText(KeyEvent.getKeyText(e.getKeyCode()));
                settingsJson.getAsJsonObject("keybinds").addProperty(jsonKey, KeyEvent.getKeyText(e.getKeyCode()));
                saveSettings();
            }
        });

        panel.add(label, BorderLayout.WEST);
        panel.add(keyLabel, BorderLayout.EAST);

        return panel;
    }
}

