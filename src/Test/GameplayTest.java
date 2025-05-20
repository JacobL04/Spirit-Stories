
package src.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.Gameplay.Gameplay;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Gameplay class.
 * This class contains tests to verify the functionality of the Gameplay class.
 */
class GameplayTest {

    private Gameplay gameplay;
    private JFrame testWindow;

    /**
     * Sets up the test environment before each test.
     * Initializes the Gameplay instance and a test JFrame.
     */
    @BeforeEach
    public void setUp() {
        gameplay = new Gameplay("Game1.json");
        testWindow = new JFrame();
        gameplay.window = testWindow;
    }

    /**
     * Tests the createButtonActionPanel method.
     * Verifies that the panel is not null and contains 6 butons.
     */
    @Test
    public void testCreateButtonActionPanel() {
        JPanel panel = gameplay.createButtonActionPanel();
        assertNotNull(panel, "Button action panel should not be null");
        assertEquals(6, panel.getComponentCount(), "Button action panel should contain 6 buttons");
    }

    /**
     * Tests the refreshButtonPanel method.
     * Verifies that the button panel is refreshed by checking the components of the main panel.
     */
    @Test
    public void testRefreshButtonPanel() {
        gameplay.refreshButtonPanel();
        // Verify that the button panel is refreshed by checking the components of main panel
        Component[] components = gameplay.con.getComponents();
        JPanel mainPanel = null;
        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0) {
                mainPanel = (JPanel) comp;
                break;
            }
        }
        assertNotNull(mainPanel, "Main panel should not be null");
        assertEquals(2, mainPanel.getComponentCount(), "Main panel should contain 2 components");
    }

    /**
     * Tests the closeGameplay method.
     * Verifies that the game is no longer running and the window is disposed.
     */
    @Test
    public void testCloseGameplay() {
        gameplay.closeGameplay();
        // Verify that the game is no longer running and the window is disposed
        assertFalse(gameplay.isRunning, "Gameplay should not be running");
        assertFalse(testWindow.isDisplayable(), "Window should be disposed");
    }
}