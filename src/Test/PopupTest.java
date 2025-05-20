package src.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.Gameplay.Gameplay;
import src.Pages.Gameplay.Inventory;
import src.Pages.Gameplay.Settings;
import src.Pages.components.Popup;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Popup class.
 * This class contains tests to verify the functionality of the Popup class.
 */
class PopupTest {

    /**
     * Sets up the test environment before each test.
     * Ensures no existing Popup instance.
     */
    @BeforeEach
    public void setUp() {
        if (Popup.getCurrentInstance() != null) {
            Popup.getCurrentInstance().dispose();
        }
    }

    /**
     * Tests the getInstance method.
     * Verifies that a new Popup instance is created correctly.
     */
    @Test
    public void testGetInstance() {
        Popup.getInstance(Inventory.class, "Game1.json");
        Popup popup = Popup.getCurrentInstance();
        assertNotNull(popup, "Popup instance should not be null");
        assertInstanceOf(Inventory.class, popup, "Popup instance should be of type Inventory");
    }

    /**
     * Tests the dispose method.
     * Verifies that the Popup instance is disposed correctly.
     */
    @Test
    public void testDispose() {
        Popup.getInstance(Inventory.class, "Game1.json");
        Popup popup = Popup.getCurrentInstance();
        assertNotNull(popup, "Popup instance should not be null");

        popup.dispose();
        assertNull(Popup.getCurrentInstance(), "Popup instance should be null after disposal");
    }

    /**
     * Tests the getCurrentInstance method.
     * Verifies that the current Popup instance is returned correctly.
     */
    @Test
    public void testGetCurrentInstance() {
        assertNull(Popup.getCurrentInstance(), "Popup instance should be null initially");

        Popup.getInstance(Inventory.class, "Game1.json");
        Popup popup = Popup.getCurrentInstance();
        assertNotNull(popup, "Popup instance should not be null");
    }
}