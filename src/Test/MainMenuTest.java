package src.Test;

import org.junit.jupiter.api.Test;
import javax.swing.ImageIcon;
import static org.junit.jupiter.api.Assertions.*;
import src.Pages.MainMenu.MainMenu;

/**
 * Unit tests for the MainMenu class.
 * This class contains tests to verify the functionality of the MainMenu class
 */
class MainMenuTest {

    /**
     * Tests the createIcon method in the MainMenu class.
     *
     * <p>
     * This test verifies that the createIcon method correctly loads an image
     * and resizes it to the specified dimensions.
     * </p>
     *
     */
    @Test
    public void testCreateIcon() {
        MainMenu mainMenu = new MainMenu();
        ImageIcon icon = mainMenu.createIcon("src/Pages/assets/settings.png", 40, 40);

        // Assert that the icon is not null
        assertNotNull(icon, "Icon should not be null");

        // Assert that the icon's width is 40
        assertEquals(40, icon.getIconWidth(), "Icon width should be 40");

        // Assert that the icon's height is 40
        assertEquals(40, icon.getIconHeight(), "Icon height should be 40");
    }

    //@Test
    
}