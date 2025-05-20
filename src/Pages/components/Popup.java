package src.Pages.components;

import javax.swing.*;
import src.Pages.Gameplay.Gameplay;
import src.Pages.Gameplay.Inventory;
import src.Pages.Gameplay.Settings;
import src.Pages.MainMenu.MainMenu;

/**
 * The Popup class creates a popup when the user receives an item
 * <p>
 * The popup acts as a notification for the user that they received an item
 * and it is automatically deleted after a 2 seconds
 * <p>
 * @version 1.0
 */
public class Popup extends Window {

    private static Popup instance;

    /**
     * The properties of the popup for the inventory
     */
    public Popup() {
        super();

        // Set up basic properties for the popup window
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
    }
    
    /**
     * Only allows 1 window instance at a time for a popup
     * @param popupClass the popup
     */
    public static void getInstance(Class<? extends Popup> popupClass, Object... args) { 
        try {
            // Create new popup if none exists or type is different
            if (instance == null || !instance.getClass().equals(popupClass)) {
                
                // Close previous popup if it exists
                if (instance != null) {
                    instance.dispose();
                }
                
                // Create Inventory popup with a String argument
                if (popupClass == Inventory.class && args.length > 0) { // Handle parameterized constructor for Inventory
                    instance = popupClass.getDeclaredConstructor(String.class).newInstance(args[0]);
                } 
                
                // Create Settings popup with a Gameplay argument
                // else if (popupClass == Settings.class) {
                //     System.out.println(args[0].getClass() == MainMenu.class);
                    
                //     if(args[0].getClass() == MainMenu.class) {
                //         instance = popupClass.getDeclaredConstructor().newInstance();
                //     }
                //     else if (args[0].getClass() == Gameplay.class){
                //         instance = popupClass.getDeclaredConstructor(Gameplay.class).newInstance(args[0]);   
                //     }

                    
                // }

                else if (popupClass == Settings.class) {
                    Object param = (args.length > 0) ? args[0] : null;
                
                    if (param instanceof MainMenu) {
                        // Use no-arg constructor for Settings
                        instance = popupClass.getDeclaredConstructor().newInstance();
                    } else if (param instanceof Gameplay) {
                        // Use Gameplay constructor
                        instance = popupClass.getDeclaredConstructor(Gameplay.class).newInstance(param);
                    } else {
                        // Fallback to no-arg constructor
                        instance = popupClass.getDeclaredConstructor().newInstance();
                    }
                }
                
                // Create popup using default constructor
                else {
                    instance = popupClass.getDeclaredConstructor().newInstance();
                }
            } 
            
            // Bring existing popup to front
            else {
                instance.toFront();
                instance.repaint();
            }
        } 
        
        catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /**
     * Close the window
     */
    @Override
    public void dispose() {
        instance = null;
        super.dispose();
    }

    /**
     * Get the current popup
     * @return the popup
     */
    public static Popup getCurrentInstance() {
        return instance;
    }
}