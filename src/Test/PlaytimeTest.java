package src.Test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.Pages.ParentalPage.Playtime;

/**
 * Unit test for the Playtime class.
 */
public class PlaytimeTest {

    @Test
    public void testResetPlaytime() {
        Playtime.resetPlaytime();

        Playtime.startSession();
        Playtime.stopSession();

        int last = Playtime.getLastPlayTime();
        // In theory, last should be >= 0
        assertTrue(last >= 0);
    }
}