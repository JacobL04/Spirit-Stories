package src.Pages.Gameplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import com.google.gson.JsonObject;

import src.Pages.components.CustomFont;
import src.Pages.components.LoadDataFile;
import src.Pages.components.Popup;

/**
 * The Inventory class creates the inventory for the gameplay
 * <p>
 * The inventory stores items into inventory slots that the user can use to interact with their pet
 * <p>
 * @version 1.0
 */
public class Inventory extends Popup {
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private final Map<JCheckBox, String> checkboxItemMap = new HashMap<>();
    private final List<String> selectedItems = new ArrayList<>();

    /**
     * Creates the popup window for the inventory
     */
    public Inventory(String filePath) {
        super();
        // Load game data from JSON
        LoadDataFile loadDataFile = new LoadDataFile();
        JsonObject json_contents = loadDataFile.loadJsonObjFromFile("src/Data/" + filePath);
        
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inventory label
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel titleLabel = new JLabel("Inventory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Inventory slots
        JPanel inventoryPanel = new JPanel(new GridLayout(ROWS, COLS, 15, 15));
        inventoryPanel.setBackground(Color.WHITE);
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JsonObject jsonObject = json_contents.getAsJsonObject();
        JsonObject itemsObjectFood = jsonObject.getAsJsonObject("food");
        JsonObject itemsObjectItem = jsonObject.getAsJsonObject("item");

        // Merge both items into one set
        Set<String> foodItemNames = itemsObjectFood.keySet();
        Set<String> regularItemNames = itemsObjectItem.keySet();
        Set<String> allItemNames = new HashSet<>();
        allItemNames.addAll(foodItemNames);
        allItemNames.addAll(regularItemNames);

        List<String> itemList = new ArrayList<>(allItemNames);

        // Fill inventory slots
        for (int i = 0; i < ROWS * COLS; i++) {
            JPanel slotPanel = new JPanel(new BorderLayout());
            slotPanel.setPreferredSize(new Dimension(80, 80));
            slotPanel.setBackground(Color.LIGHT_GRAY);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));

            JCheckBox selectItemBox = new JCheckBox("Select");
            selectItemBox.setFont(new CustomFont("Plain", 10));
            selectItemBox.setBackground(Color.LIGHT_GRAY);
            slotPanel.add(selectItemBox, BorderLayout.NORTH);

            String emoji = "";
            String itemName = "";
            String quantity = "";
            boolean hasItem = i < itemList.size();

            if (hasItem) {
                String currentItem = itemList.get(i);
                
                // Check both food and item categories for quantity
                int qty = 0;
                if (itemsObjectFood.has(currentItem)) {
                    qty = itemsObjectFood.get(currentItem).getAsInt();
                } else if (itemsObjectItem.has(currentItem)) {
                    qty = itemsObjectItem.get(currentItem).getAsInt();
                }

                if (qty > 0) {
                    emoji = getEmojiForItem(currentItem);
                    itemName = currentItem;
                    quantity = "x" + qty;

                    addItem(slotPanel, emoji, itemName, quantity);
                    checkboxItemMap.put(selectItemBox, itemName);
                }
            } else {
                checkboxItemMap.put(selectItemBox, null);
            }

            // Selection listener remains the same
            selectItemBox.addActionListener(e -> {
                JCheckBox cb = (JCheckBox) e.getSource();
                String name = checkboxItemMap.get(cb);
                if (name != null) {
                    if (cb.isSelected()) {
                        if (!selectedItems.contains(name)) {
                            selectedItems.add(name);
                        }
                    } else {
                        selectedItems.remove(name);
                    }
                }
            });

            inventoryPanel.add(slotPanel);
        }

        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Stores all the items that the inventory can have into a hash map 
     * @param itemName the name of the item
     * @return the slot the items should be in in the inventory
     */
    public String getEmojiForItem(String itemName) {
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
     * Add items to their inventory slot
     * @param slot      the inventory slot the item should be put in
     * @param emojiItem the emoji of the item
     * @param itemName  the item name
     * @param quantity  the number of this item
     */
    private void addItem(JPanel slot, String emojiItem, String itemName, String quantity) {
        JLabel itemLabel = new JLabel(emojiItem, SwingConstants.CENTER);
        itemLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        JLabel nameLabel = new JLabel(itemName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel qtyLabel = new JLabel(quantity, SwingConstants.CENTER);
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(nameLabel);
        textPanel.add(qtyLabel);

        slot.add(itemLabel, BorderLayout.CENTER);
        slot.add(textPanel, BorderLayout.SOUTH);
    }

    /**
     * Gets all items that the user selects in the inventory
     * @return an ArrayList of items
     */
    public List<String> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(Inventory::new);
    // }
}
