package src.Pages.components;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom fonts are available to be used for text 
 * <p>
 * Gives the text of JLabels and JButtons a custom font
 * <p>
 * @version 1.0
 */
public class CustomFont extends Font {

    /**
     * Makes a custom font
     * @param style font type
     * @param size size of the font
     */
    public CustomFont(String style, int size) {
        // Call the superclass constructor with default font settings
        super(null, 0, size);

        Font customFont = null;

        // Base directory path for custom font files
        String fontPath = "src/Pages/assets/font/";

        // Select the font file based on style
        String fontFile;
        switch (style) {
            case "Black": fontFile = "Rubik-Black.ttf"; break;
            case "BlackItalic": fontFile = "Rubik-BlackItalic.ttf"; break;
            case "Bold": fontFile = "Rubik-Bold.ttf"; break;
            case "BoldItalic": fontFile = "Rubik-BoldItalic.ttf"; break;
            case "ExtraBold": fontFile = "Rubik-ExtraBold.ttf"; break;
            case "ExtraBoldItalic": fontFile = "Rubik-ExtraBoldItalic.ttf"; break;
            case "ItalicVariable": fontFile = "Rubik-Italic-VariableFont_wght.ttf"; break;
            case "Italic": fontFile = "Rubik-Italic.ttf"; break;
            case "Light": fontFile = "Rubik-Light.ttf"; break;
            case "LightItalic": fontFile = "Rubik-LightItalic.ttf"; break;
            case "Medium": fontFile = "Rubik-Medium.ttf"; break;
            case "MediumItalic": fontFile = "Rubik-MediumItalic.ttf"; break;
            case "Regular": fontFile = "Rubik-Regular.ttf"; break;
            case "SemiBold": fontFile = "Rubik-SemiBold.ttf"; break;
            case "SemiBoldItalic": fontFile = "Rubik-SemiBoldItalic.ttf"; break;
            case "Variable": fontFile = "Rubik-VariableFont_wght.ttf"; break;
            default: fontFile = "Rubik-Regular.ttf"; // Default font
        }

        try {
            // Load and register the font
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + fontFile)).deriveFont((float) size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            this.name = customFont.getName();
            this.style = customFont.getStyle();
            this.size = size;

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
