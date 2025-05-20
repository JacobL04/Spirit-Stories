package src.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.Gameplay.Gameplay;
import src.Pages.Gameplay.Settings;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Settings class.
 * This class contains tests to verify the functionality of the Settings class.
 */
class SettingsTest {

    private Settings settings;
    private Gameplay mockGameplay;

    /**
     * Sets up the test environment before each test.
     * Initializes the Settings instance with a mock Gameplay instance.
     */
    @BeforeEach
    public void setUp() {
        mockGameplay = new Gameplay("Game1.json");
        settings = new Settings(mockGameplay);
    }

    /**
     * Tests the createButtonPanel method.
     * Verifies that the button panel is created with the correct components.
     */
    @Test
    public void testCreateButtonPanel() {
        JPanel buttonPanel = settings.createButtonPanel();
        assertNotNull(buttonPanel, "Button panel should not be null");
        assertEquals(2, buttonPanel.getComponentCount(), "Button panel should contain 2 components");
    }

    /**
     * Tests the applyButton method.
     * Verifies that the apply button is created with the correct properties.
     */
    @Test
    public void testApplyButton() {
        JButton applyButton = settings.applyButton();
        assertNotNull(applyButton, "Apply button should not be null");
        assertEquals("Apply", applyButton.getText(), "Apply button text should be 'Apply'");
    }

    /**
     * Tests the createHeaderLabel method.
     * Verifies that the header label is created with the correct text and properties.
     */
    @Test
    public void testCreateHeaderLabel() {
        JLabel headerLabel = settings.createHeaderLabel("Test Header");
        assertNotNull(headerLabel, "Header label should not be null");
        assertEquals("Test Header", headerLabel.getText(), "Header label text should be 'Test Header'");
    }

    /**
     * Tests the exitButton method.
     * Verifies that the exit button is created with the correct properties.
     */
    @Test
    public void testExitButton() {
        JButton exitButton = settings.exitButton();
        assertNotNull(exitButton, "Exit button should not be null");
        assertEquals("Return to Main Menu", exitButton.getText(), "Exit button text should be 'Return to Main Menu'");
    }

    /**
     * Tests the createSliderPanel method.
     *
     * <p>
     * Verifies that the slider panel is created with the correct
     * components and initial values.
     * </p>
     *
     */
    @Test
    public void testCreateSliderPanel() {
        JPanel sliderPanel = settings.createSliderPanel("Test Slider", 50, "TestSlider");
        assertNotNull(sliderPanel, "Slider panel should not be null");
        assertEquals(3, sliderPanel.getComponentCount(), "Slider panel should contain 3 components");
    }

    /**
     * Tests the createKeyBindPanel method.
     * Verifies that the key bind panel is created with the correct components and initial key.
     */
    @Test
    public void testCreateKeyBindPanel() {
        JPanel keyBindPanel = settings.createKeyBindPanel("Test Key", KeyEvent.VK_A, "TestKey");
        assertNotNull(keyBindPanel, "Key bind panel should not be null");
        assertEquals(2, keyBindPanel.getComponentCount(), "Key bind panel should contain 2 components");
    }
}