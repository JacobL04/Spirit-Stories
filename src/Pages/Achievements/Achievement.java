package src.Pages.Achievements;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import src.Pages.MainMenu.MainMenu;
import src.Pages.components.RoundedIconButton;
import src.Pages.components.Window;
import com.google.gson.JsonObject;
import src.Pages.components.CustomFont;
import src.Pages.components.LoadDataFile;

/**
 * The Achievement class represents the achievements screen of the game.
 * <p>
 * It displays the player's achievements, inventory, and score based on the game data.
 * <p>
 * @version 1.0
 */
public class Achievement extends Window {
    final JPanel achievementPanel;
    private JLabel scoreLabel;
    private int playerScore = 0;
    final JButton backButton;
    final Map<String, Integer> inventory = new HashMap<>();

    /**
     * Constructs an Achievement window, initializes the UI components,
     * loads the game data, and populates the achievements and inventory.
     */
    public Achievement() {
        super();
        setTitle("Achievements");
        setLayout(new BorderLayout());

        // Load game data frome JSON file
        LoadDataFile loadDataFile = new LoadDataFile();
        JsonObject gameData = loadDataFile.loadJsonObjFromFile("src/Data/Game1.json");

        // Get player's score from saved data
        if (gameData.has("Score")) {
            this.playerScore = gameData.get("Score").getAsInt();
        }

        // Load inventory items from saved data
        if (gameData.has("item")) {
            JsonObject itemsObject = gameData.getAsJsonObject("item");
            itemsObject.entrySet().forEach(entry -> inventory.put(entry.getKey(), entry.getValue().getAsInt()));
        }

        if (gameData.has("food")) {
            JsonObject foodObject = gameData.getAsJsonObject("food");
            foodObject.entrySet().forEach(entry -> inventory.put(entry.getKey(), entry.getValue().getAsInt()));
        }

        // Title Label
        JLabel titleLabel = new JLabel("Achievements", SwingConstants.CENTER);
        titleLabel.setFont(new CustomFont("ExtraBoldItalic", 30));

        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.SOUTH);

        // After creating the topPanel, add an empty border to create bottom margin
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(topPanel, BorderLayout.NORTH);

        // Create inventory and score display panel
        JPanel topRightPanel = createInventoryScorePanel();
        topPanel.add(topRightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        /**
         * Initializes the achievement panel with a 6x6 grid of achievement labels.
         * Each achievement label is sized to 80×80 pixels with centered alignment.
         */
        
         // Create achievement panel with 6x6 grid layout
        achievementPanel = new JPanel(new GridLayout(6, 6, 35, 25));
        for (int i = 0; i < 36; i++) {
            JLabel achievementLabel = new JLabel();
            achievementLabel.setHorizontalAlignment(SwingConstants.CENTER);
            achievementLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Load corresponding icon and tooltip
            ImageIcon icon;
            String tooltipText;

            // Determime whether the achievement is unlocked based on scores
            int requiredScore = (i + 1) * 50;
            boolean isUnlocked = (playerScore >= requiredScore);

            // Unlocked achievements show a custom icon, while locked ones show a locked icon
            if (isUnlocked) {
                icon = loadIcon("src/Pages/assets/achievements.png", 50, 50);
                tooltipText = "Unlocked: Score " + requiredScore + " Achievement!";
            } else {
                icon = loadIcon("src/Pages/assets/locked.png", 50, 50);
                tooltipText = "Locked: Reach " + requiredScore + " points to unlock!";
            }

            // Set icon and tooltip for achievement
            achievementLabel.setIcon(icon);
            achievementLabel.setPreferredSize(new Dimension(80, 80));
            achievementLabel.setToolTipText(tooltipText);
            addHoverListener(achievementLabel, tooltipText);
            achievementPanel.add(achievementLabel);
        }

        // Grid container that holds the achievementPanel
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.add(achievementPanel);
        add(gridContainer, BorderLayout.CENTER);

        // Enhanced Back Button
        backButton = new JButton("<- Back to Menu");
        backButton.setFont(new CustomFont("Bold", 16));
        backButton.setOpaque(true);
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(70, 130, 180));
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(MainMenu::new);
        });


        // Create container panel for back button with centered layout and padding
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        backPanel.add(backButton);

        add(backPanel, BorderLayout.SOUTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Creates and returns a panel displaying the player's score and inventory.
     * @return A JPanel containing the score and inventory information
     */
    private JPanel createInventoryScorePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create the score display panel and inventory title label.
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scoreLabel = new JLabel("Score: " + playerScore);
        scoreLabel.setFont(new CustomFont("Bold", 16));
        scorePanel.add(scoreLabel);
        panel.add(scorePanel);

        JLabel inventoryTitle = new JLabel("Inventory: ");
        inventoryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryTitle.setFont(new CustomFont("Variable", 16));

        JPanel inventoryContainer = new JPanel();
        inventoryContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryContainer.add(inventoryTitle);

        // A panel to display inventory items in a responsive grid layout
        JPanel itemsPanel = new JPanel(new GridLayout(0, 3, 5, 5));

        /**
         *
         * Shows all inventory items that the player has (quantity > 0).
         * Each item appears in its own box with an emoji icon, item name and quantity.
         *
         */
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                String emoji = getEmojiForItem(entry.getKey());
                JLabel textLabel = new JLabel(emoji + " " + entry.getKey() + ": " + entry.getValue());
                textLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));  // Ensuring emoji support
                itemPanel.add(textLabel);
                itemsPanel.add(itemPanel);
            }
        }


        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        inventoryContainer.add(scrollPane);
        panel.add(inventoryContainer);

        return panel;
    }


    /**
     * Returns the emoji representation of an inventory item.
     * @param itemName The name of the item to get emoji for
     * @return String containing the emoji character, or empty string if not found
     */
    private String getEmojiForItem(String itemName) {
        Map<String, String> emojiMap = new HashMap<>();
        // Consumable food
        emojiMap.put("Milk Tea", "🧋");
        emojiMap.put("Apple", "🍎");
        emojiMap.put("Water", "🥤");
        emojiMap.put("Cookie", "🍪");
        emojiMap.put("Cake", "🍰");
        emojiMap.put("Meat", "🍖");
        emojiMap.put("Croissant", "🥐");
        emojiMap.put("Blueberries", "🫐");
        emojiMap.put("Broccoli", "🥦");
        emojiMap.put("Chocolate", "🍫");
        emojiMap.put("Mocktail", "🍹");

        // Gift items
        emojiMap.put("Bone", "🦴");
        emojiMap.put("Toy", "🧸");
        emojiMap.put("Paint", "🎨");
        emojiMap.put("Ball", "⚾");
        emojiMap.put("Music", "🎷");
        return emojiMap.getOrDefault(itemName, "");
    }

    /**
     * Add a hover effect over all labels
     * @param label the label to have a hover effect on
     * @param message the message that should show when label is hovered
     */
    private void addHoverListener(JLabel label, String message) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setToolTipText(message);
            }
        });
    }
}
