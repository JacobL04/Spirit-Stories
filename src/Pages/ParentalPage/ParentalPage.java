package src.Pages.ParentalPage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import src.Pages.MainMenu.MainMenu;
import src.Pages.components.CustomFont;
import src.Pages.components.LoadDataFile;
import src.Pages.components.SaveSlot;
import src.Pages.components.Window;

/**
 * Represents the Parental Controls page in the application.
 * <p>
 * Provides methods to manage parental control settings and a password to access them. 
 * Used to restrict a user's playtime every day and allows them to revive their dead pet
 * <p>
 * @version 1.0
 */
public class ParentalPage extends Window { 
    private JButton backButton;
    private JPanel mainPanel;
    private JCheckBox enableLimitCheckbox;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JPasswordField passwordField; // Add password field

    /**
     * Creates the parental page window
     */
    public ParentalPage() {
        super();
        setTitle("Parental Controls");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //Set the main title  
        JLabel titleLabel = new JLabel("Parental Controls", SwingConstants.CENTER);
        titleLabel.setFont(new CustomFont("ExtraBoldItalic", 30));
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        titleLabel.setOpaque(true);
        add(titleLabel, BorderLayout.NORTH);

        //Set up main panel
        mainPanel = new JPanel(new GridBagLayout()); 
        mainPanel.setBackground(Color.WHITE); 
        add(mainPanel, BorderLayout.CENTER);

        //Build a Parental Control Framework
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 6), // set thickness to 0 to remove border later
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        //controlPanel.setPreferredSize(new Dimension(700, 500));
        controlPanel.setBackground(Color.WHITE);
        
        mainPanel.add(controlPanel);


        //Set up internal components of the Parental Control Panel
        //Set up the Playtime label
        JPanel playtimeTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        JLabel playtimeTitle = new JLabel("  Playtime");
        playtimeTitle.setFont(new CustomFont("Bold", 35));
        playtimeTitle.setOpaque(true);
        playtimeTitle.setBackground(Color.DARK_GRAY);
        playtimeTitle.setForeground(Color.WHITE);
        playtimeTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 960));
        playtimeTitlePanel.setBackground(Color.WHITE);
        playtimeTitlePanel.add(playtimeTitle);

        //Set up internal components of the playtime panel
        JPanel playtimePanel = new JPanel(new GridBagLayout());
        playtimePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 39, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; 
        
        JLabel totalPlaytime = new JLabel("Total Playtime");
        JLabel lastPlaytime = new JLabel("Last Playtime");
        JLabel averagePlaytime = new JLabel("Average Playtime");

        //Initialize time
        JLabel totalValue = new JLabel(formatTime(Playtime.getTotalPlayTime()), SwingConstants.RIGHT);
        JLabel lastValue = new JLabel(formatTime(Playtime.getLastPlayTime()), SwingConstants.RIGHT);
        JLabel averageValue = new JLabel(formatTime(Playtime.getAveragePlayTime()), SwingConstants.RIGHT);

        //Update game time every minute
        Timer updateTimer = new Timer(60000, e -> {  
            totalValue.setText(formatTime(Playtime.getTotalPlayTime()));
            lastValue.setText(formatTime(Playtime.getLastPlayTime()));
            averageValue.setText(formatTime(Playtime.getAveragePlayTime()));
        });
        updateTimer.start();
        
        //Set up the font
        Font labelFont = new CustomFont("Bold", 21);
        totalPlaytime.setFont(labelFont);
        lastPlaytime.setFont(labelFont);
        averagePlaytime.setFont(labelFont);
        totalValue.setFont(labelFont);
        lastValue.setFont(labelFont);
        averageValue.setFont(labelFont);
        
        //Set up the left and right columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        playtimePanel.add(totalPlaytime, gbc);
        gbc.gridy = 1;
        playtimePanel.add(lastPlaytime, gbc);
        gbc.gridy = 2;
        playtimePanel.add(averagePlaytime, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        playtimePanel.add(totalValue, gbc);
        gbc.gridy = 1;
        playtimePanel.add(lastValue, gbc);
        gbc.gridy = 2;
        playtimePanel.add(averageValue, gbc);
        
        //Set up the container
        JPanel playtimeContainer = new JPanel(new BorderLayout());
        playtimeContainer.setBackground(Color.WHITE);
        playtimeContainer.add(playtimeTitlePanel, BorderLayout.NORTH);
        playtimeContainer.add(playtimePanel, BorderLayout.CENTER);

        //Set up the Restrictions label
        JPanel restrictionsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel restrictionsTitle = new JLabel("Restrictions");
        restrictionsTitle.setFont(new CustomFont("Bold", 35));
        restrictionsTitle.setOpaque(true);
        restrictionsTitle.setBackground(Color.DARK_GRAY);
        restrictionsTitle.setForeground(Color.WHITE);
        restrictionsTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 900));
        restrictionsTitlePanel.setBackground(Color.WHITE);
        restrictionsTitlePanel.add(restrictionsTitle);

        //Set up internal components of the restrictiosn panel
        JPanel restrictionsPanel = new JPanel(new GridBagLayout());
        restrictionsPanel.setBackground(Color.WHITE);
        gbc.insets = new Insets(20, 39, 20, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; 

        //Edit the format of the right column of the restrictions panel
        JLabel screentime = new JLabel("Screen Time Limit (Press Enter to save)");
        JLabel reset = new JLabel("Reset Playtime / Revive Pet");
        
        // Set up the value of screen time limit
        JTextField screentimeValue = new JTextField("00:00", 5);
        
        // Read the ScreenTimeLimit value from the current JSON file
        String filePath = "src/Data/" + SaveSlot.get();
        String jsonContent = readJsonFromFile(filePath);

        System.out.println(filePath);

        if (jsonContent != null) {
            JsonObject json = JsonParser.parseString(jsonContent).getAsJsonObject();
            String screenLimit = json.get("ScreenTimeLimit").getAsString();
            screentimeValue.setText(screenLimit); // show it in the text field
            } else {
                screentimeValue.setText("00:00"); // fallback value
                System.out.println("Failed to read screen time limit. No game file exists.");
            }
        
        //Set the format of screen time limit
        screentime.setFont(new Font("Montserrat", Font.BOLD, 15));
        screentimeValue.setHorizontalAlignment(JTextField.CENTER);
        screentimeValue.setOpaque(true);//add borders
        screentimeValue.setBackground(Color.WHITE);
        screentimeValue.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        reset.setFont(new Font("Montserrat", Font.BOLD, 15));
        
        screentimeValue.addActionListener(e -> {
            String newTime = screentimeValue.getText();
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();
                json.addProperty("ScreenTimeLimit", newTime);
                Files.write(Paths.get(filePath), json.toString().getBytes());
                System.out.println("Screen time limit saved.");
            } catch (IOException ex) {
                System.out.println("Failed to save screen time limit: " + ex.getMessage());
            }
        });

        //Set the reset button‘s functions
        JButton resetButton = getJButton(totalValue, lastValue, averageValue);

        //Set the format of the reset button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.setOpaque(true); 
        resetButton.setBackground(Color.WHITE);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        /**
         * Allows parents to revive the pet, restoring it to a normal state
         * with maximum values for all statistics. (Requirement 3.1.5)
         */
        JButton reviveButton = new JButton("Revive");

        reviveButton.addActionListener(e -> {
            String reviveFilePath = "src/Data/" + SaveSlot.get();
            try {
                String content = new String(Files.readAllBytes(Paths.get(reviveFilePath)));
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();

                // Revive pet by setting state to happy and stats to max
                json.addProperty("State", "happy");
                json.addProperty("Health", 100);
                json.addProperty("Happiness", 100);
                json.addProperty("Fullness", 100);
                json.addProperty("Sleep", 100);

                // Save the updated data back to file (with pretty format)
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Files.write(Paths.get(reviveFilePath), gson.toJson(json).getBytes());

                JOptionPane.showMessageDialog(null, "Pet revived successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to revive pet: " + ex.getMessage());
            }
        });

        // Set the format of the revive button
        reviveButton.setHorizontalAlignment(SwingConstants.CENTER);
        reviveButton.setOpaque(true); 
        reviveButton.setBackground(Color.WHITE);
        reviveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Set up password field
        JLabel passwordLabel = new JLabel("Password (Press Enter to save)");
        passwordLabel.setFont(new CustomFont("Bold", 15));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new CustomFont("Plain", 15));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setOpaque(true);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        // Add password field to restrictions panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        restrictionsPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        restrictionsPanel.add(passwordField, gbc);
        
        // Save password when pressing Enter in password field
        passwordField.addActionListener(e -> {
            savePasswordToJson(new String(passwordField.getPassword()));
        });

        //Set up the font
        screentime.setFont(labelFont);
        reset.setFont(labelFont);
        screentimeValue.setFont(labelFont);
        resetButton.setFont(labelFont);
        reviveButton.setFont(labelFont);

        //Set up the left and right columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        restrictionsPanel.add(screentime, gbc);
        gbc.gridy = 1;
        restrictionsPanel.add(reset, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        restrictionsPanel.add(screentimeValue, gbc);
        
        //Create a panel to hold both Reset and Revive buttons
        gbc.gridy = 1;
        JPanel resetRevivePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetRevivePanel.setBackground(Color.WHITE);
        resetRevivePanel.add(resetButton);
        resetRevivePanel.add(reviveButton);
        restrictionsPanel.add(resetRevivePanel, gbc);
        
        // Add checkbox to enable/disable time range restriction
        JLabel enableLimitLabel = new JLabel("Enable Time Limit (Check the box then press Enter to save)");
        enableLimitLabel.setFont(labelFont);

        enableLimitCheckbox = new JCheckBox();
        enableLimitCheckbox.setBackground(Color.WHITE);
        enableLimitCheckbox.setFocusPainted(false);
        enableLimitCheckbox.setSelected(false); // Default: not enabled
        
        /**
         * Adds UI components for time-of-day play restrictions,
         * including a checkbox to enable the restriction, and two fields to set
         * allowed start and end times (e.g., 08:00 to 20:00).
         */

        //Create start and end time fields next to checkbox
        startTimeField = new JTextField("08:00", 5);
        startTimeField.setHorizontalAlignment(JTextField.CENTER);
        startTimeField.setFont(labelFont);
        startTimeField.setBackground(Color.WHITE);
        startTimeField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        endTimeField = new JTextField("20:00", 5);
        endTimeField.setHorizontalAlignment(JTextField.CENTER);
        endTimeField.setFont(labelFont);
        endTimeField.setBackground(Color.WHITE);
        endTimeField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        //Set up the container
        JPanel enableLimitControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enableLimitControlPanel.setBackground(Color.WHITE);
        enableLimitControlPanel.add(enableLimitCheckbox);
        enableLimitControlPanel.add(startTimeField);
        enableLimitControlPanel.add(endTimeField);

        // Add to restrictionsPanel
        gbc.gridx = 0;
        gbc.gridy = 2;
        restrictionsPanel.add(enableLimitLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        restrictionsPanel.add(enableLimitControlPanel, gbc); 
        
        /**
         * Enables or disables the time-of-day limit based on checkbox selection
         * and saves the changes to JSON.
         */
        enableLimitCheckbox.addActionListener(e -> {
            boolean enabled = enableLimitCheckbox.isSelected();
            startTimeField.setEnabled(enabled);
            endTimeField.setEnabled(enabled);
            
            // Save to JSON
            saveTimeLimitToJson(enabled, startTimeField.getText(), endTimeField.getText());
        });

        //Set up the container
        JPanel restrictionsContainer = new JPanel(new BorderLayout());
        restrictionsContainer.setBackground(Color.WHITE);
        restrictionsContainer.add(restrictionsTitlePanel, BorderLayout.NORTH);
        restrictionsContainer.add(restrictionsPanel, BorderLayout.CENTER);
        
        // Set a vertical layout to stack playtimeContainer and restrictionsContainer
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(playtimeContainer);
        controlPanel.add(restrictionsContainer);

        //Set the format of the back button
        backButton = new JButton("Back");
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center back button
        backPanel.add(backButton);
        backPanel.setBackground(Color.WHITE);
        backPanel.setOpaque(true);

        //Set the function of the back button
        backButton.addActionListener(e -> {
            backButton();
        });

        // Save when pressing Enter in start time field
        startTimeField.addActionListener(e -> {
            saveTimeLimitToJson(enableLimitCheckbox.isSelected(), startTimeField.getText(), endTimeField.getText());
        });

        // Save when pressing Enter in end time field
        endTimeField.addActionListener(e -> {
            saveTimeLimitToJson(enableLimitCheckbox.isSelected(), startTimeField.getText(), endTimeField.getText());
        });

        add(backPanel, BorderLayout.SOUTH);
    }

    /**
     * Resets all the average, last, and total playtime values to 0
     * @param totalValue current total playtime
     * @param lastValue last playtime
     * @param averageValue average playtime between all game sessions
     * @return
     */
    private static JButton getJButton(JLabel totalValue, JLabel lastValue, JLabel averageValue) {
        JButton resetButton = new JButton("Reset");

        // Resets the playtime data to zero and updates the playtime display fields
        resetButton.addActionListener(e -> {
            Playtime.resetPlaytime();

            //Update UI data
            totalValue.setText("00:00");
            lastValue.setText("00:00");
            averageValue.setText("00:00");

            JOptionPane.showMessageDialog(null, "Playtime has been reset successfully!");
        });
        return resetButton;
    }

    /**
     * The timer for the gameplay stops when the user exits the gameplay
     */
    private void backButton() {
        Playtime.stopSession(); // Stop timer when returning to Main Menu
        dispose();
        SwingUtilities.invokeLater(MainMenu::new);
    }

    /**
     * Formats the time the user played the game into hours and minutes
     * @param minutes number of minutes the user has played the game
     * @return a String showing the number of hurs and minutes the user has played the game in total
     */
    private String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    /**
     * Reads text from a Json file to retrieve data
     * @param filePath path to get the Json file
     * @return all the data inside the Json file?
     */
    public static String readJsonFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        return content.toString();
    }
    
    /**
    * Saves time limit settings (enabled state, start time, and end time) to the JSON file.
    * @param enabled whether the time limit is enabled
    * @param startTime the allowed start time (e.g., "08:00")
    * @param endTime the allowed end time (e.g., "20:00")
    */
    private void saveTimeLimitToJson(boolean enabled, String startTime, String endTime) {
        String filePath = "src/Data/" + SaveSlot.get();
        String content = readJsonFromFile(filePath);
        
        if (content != null) {
            try {
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();
                json.addProperty("TimeLimitEnabled", enabled);
                json.addProperty("AllowedStart", startTime);
                json.addProperty("AllowedEnd", endTime);

                // Use Gson to format the JSON data before saving it
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String formattedJson = gson.toJson(json);
            
                Files.write(Paths.get(filePath), formattedJson.getBytes());
                System.out.println("Time limit settings saved.");
            } catch (IOException e) {
                System.out.println("Failed to save time limit settings: " + e.getMessage());
            }
        } else {
            System.out.println("Could not read file to save time limit settings.");
        }
    }

    /**
     * Saves the password to the JSON file if the password field is not empty.
     * @param newPassword the new password to save
     */
    private void savePasswordToJson(String newPassword) {
        if (!newPassword.isEmpty()) {
            String filePath = "src/Data/Settings.json";
            LoadDataFile loadDataFile = new LoadDataFile();
            JsonObject settingsJson = loadDataFile.loadJsonObjFromFile(filePath);
            settingsJson.addProperty("password", newPassword);
            loadDataFile.saveJsonObjToFile(filePath, settingsJson);

            // Create and display the message label
            JLabel messageLabel = new JLabel("Password saved!", SwingConstants.CENTER);
            messageLabel.setFont(new CustomFont("Medium", 20));
            messageLabel.setOpaque(true);
            messageLabel.setBackground(new Color(0, 128, 0));
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLayeredPane layeredPane = getLayeredPane();
            layeredPane.add(messageLabel, JLayeredPane.POPUP_LAYER);

            // Calculate the position based on the frame's dimensions
            int frameWidth = getWidth();
            int frameHeight = getHeight();
            int labelWidth = 200;
            int labelHeight = 50;
            int x = (frameWidth - labelWidth) / 2;
            int y = (frameHeight - labelHeight) / 2;
            messageLabel.setBounds(x, y, labelWidth, labelHeight);

            // Remove the message label after 2 seconds
            Timer timer = new Timer(2000, e -> {
                layeredPane.remove(messageLabel);
                layeredPane.repaint();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}
