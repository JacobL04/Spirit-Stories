package src.Pages.ParentalPage;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The logic behind various user playtimes for the game
 * <p>
 * Calculates total playtime, average playtime, and previous playtime.
 * Allows a reset to playtime and updates every 60 seconds
 * <p>
 * @version 1.0
 */
public class Playtime {
    private static int totalValue = 0;
    private static int lastValue = 0;
    private static int sessionStart = 0;
    private static int sessionCount = 0;
    
    /**
     * Updates the user playtime every 60 seconds
     */
    private static Timer timer = new Timer(60000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            totalValue++;
            lastValue++;
        }
    });

    /**
     * Starts the playtime timer whenever a game session starts
     */
    public static void startSession() {
        sessionStart = totalValue;
        timer.start();
    }

    /**
     * Stops the playtime timer whenever a game session stops (user exits gameplay)
     */
    public static void stopSession() {
        timer.stop();
        lastValue = totalValue - sessionStart;
        sessionCount++;
    }

    /**
     * Gets total playtime of the user
     */
    public static int getTotalPlayTime() {
        return totalValue;
    }

    /**
     * Get the previous playtime session of the user
     * @return previous playtime
     */
    public static int getLastPlayTime() {
        return lastValue;
    }

    /**
     * Gets the average playtime of the user across all game sessions
     * @return average playtime
     */
    public static int getAveragePlayTime() {
        if (sessionCount == 0)
            return 0;
        return totalValue / sessionCount;
    }

    /**
     * Resets all user playtime values to 0
     */
    public static void resetPlaytime() {
        totalValue = 0;
        lastValue = 0;
        sessionStart = 0;
        sessionCount = 0;
    }
}