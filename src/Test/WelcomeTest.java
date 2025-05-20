package src.Test;

import org.junit.jupiter.api.Test;
import javax.swing.ImageIcon;
import static org.junit.jupiter.api.Assertions.*;
import src.Pages.WelcomePage.Welcome;

/**
 * Unit tests for the Welcome class.
 * <p>
 * This class contains tests to verify the functionality of the Welcome class
 * </p>
 */
class WelcomeTest {

    /**
     * Tests the loadIcon method in the Welcome class.
     *
     * <p>
     * This test makes sure that the loadIcon method correctly loads an image
     * and resizes it to the given dimensions
     * </p>
     *
     */
    @Test
    public void testLoadIcon() {
        Welcome welcome = new Welcome();
        ImageIcon icon = welcome.loadIcon("src/Pages/assets/Logo.png", 350, 350);
        assertNotNull(icon, "Icon shouldn't be null");
        assertEquals(350, icon.getIconWidth(), "Icon width should be 350");
        assertEquals(350, icon.getIconHeight(), "Icon height should be 350");
    }
}