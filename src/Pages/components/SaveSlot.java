package src.Pages.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The SaveSlot class stores the game file that was used in the last play session0
 * <p>
 * This class determines which game slot the user goes to when pressing the 
 * "Continue Game" button in the main menu window
 * <p>
 * @version 1.0
 */
public class SaveSlot {
    private static String currentFile;
    private static final String last_saved = "src/Data/last_saved.txt";

    // Load last saved slot when the class is first loaded
    static {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sets the file name
     * @param filename name of the file
     */
    public static void set(String filename) {
        currentFile = filename;
        save();
    }

    /**
     * Gets the file name
     * @return
     */
    public static String get() {
        try (BufferedReader reader = new BufferedReader(new FileReader(last_saved))) {
            currentFile = reader.readLine();
        } catch (IOException exception) {
            System.out.println(exception);
        }
        return currentFile;
    }

    /**
     * Loads the file
     * @throws IOException file does not exist
     */
    private static void load () throws IOException {
        try {
            Path path = Paths.get(last_saved);
            if (Files.exists(path)) {
                currentFile = new String(Files.readAllBytes(path));
            }
            
            else {
                currentFile = null;
            }
        }
        
        catch (IOException exception) {
            System.out.println(exception);
            currentFile = null;
            throw new IOException();
        }
    }

    /**
     * Saves the file with updated data
     */
    private static void save() {
        try {
            if (currentFile != null) {
                Files.write(Paths.get(last_saved), currentFile.getBytes());
            }
            
            else {
                Files.deleteIfExists(Paths.get(last_saved));
            }
        }
        
        catch (IOException exception) {
            System.out.println(exception);
        }
    }
}
