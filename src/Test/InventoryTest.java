package src.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.Gameplay.Inventory;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Inventory class.
 * This class contains tests to verify the functionality of the Inventory class.
 */
class InventoryTest {

    private Inventory inventory;

    /**
     * Sets up the test environment before each test.
     * Initializes the Inventory instance using the specified game data file.
     */
    @BeforeEach
    public void setUp() {
        inventory = new Inventory("Game1.json");
    }

    /**
     * Tests the getSelectedItems method.
     * Verifies that the selected items list is initially empty.
     */
    @Test
    public void testGetSelectedItemsInitiallyEmpty() {
        List<String> selectedItems = inventory.getSelectedItems();
        assertNotNull(selectedItems, "Selected items list should not be null");
        assertTrue(selectedItems.isEmpty(), "Selected items list should be empty initially");
    }

    /**
     * Tests the getSelectedItems method.
     * Verifies that items can be selected and retrieved correctly.
     */
    @Test
    public void testGetSelectedItemsAfterSelection() {
        // Simulate selecting items
        JCheckBox checkBox1 = (JCheckBox) inventory.getComponent(0);
        JCheckBox checkBox2 = (JCheckBox) inventory.getComponent(1);
        checkBox1.setSelected(true);
        checkBox2.setSelected(true);

        List<String> selectedItems = inventory.getSelectedItems();
        assertEquals(2, selectedItems.size(), "Selected items list should contain 2 items");
        assertTrue(selectedItems.contains("Item1"), "Selected items list should contain 'Item1'");
        assertTrue(selectedItems.contains("Item2"), "Selected items list should contain 'Item2'");
    }

    /**
     * Tests the getEmojiForItem method.
     * Verifies that the correct emoji is returned for a given item name.
     */
    @Test
    public void testGetEmojiForItem() {
        String emoji = inventory.getEmojiForItem("Milk Tea");
        assertEquals("🧋", emoji, "Emoji for 'Milk Tea' should be '🧋'");

        emoji = inventory.getEmojiForItem("Unknown Item");
        assertEquals("", emoji, "Emoji for unknown item should be an empty string");
    }
}