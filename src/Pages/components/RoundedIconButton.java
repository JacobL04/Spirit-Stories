package src.Pages.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * The RoundedIconButton class rounds the corners of a button
 * <p>
 * The settings, achievements, and parental control buttons have rounded corners,
 * with a hover effect that makes the button darker for a more modern look
 * <p>
 * @version 1.0
 */
public class RoundedIconButton extends JButton {
    private ImageIcon defaultIcon;
    private ImageIcon hoverIcon;
    private ImageIcon muteIcon; 
    private boolean showMuteIcon = false; 

    /**
     * Make the corners of the buttons round and add a hover effect
     * @param icon a button with an icon
     */
    public RoundedIconButton(ImageIcon icon) {
        super(icon);
        this.defaultIcon = icon;
        this.hoverIcon = createHoverIcon(icon);

        setContentAreaFilled(false); // Remove default fill
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60)); // Adjust as needed

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            PlayAudio.PlaySound("src/Pages/assets/Click.wav", -15.0f);

                if (!showMuteIcon) {
                    setIcon(hoverIcon); // Show hover icon
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!showMuteIcon) { 
                    setIcon(defaultIcon); // Reset to default
                } else {
                    setIcon(muteIcon); // Show mute icon if muted
                }
                repaint();
            }
        });

    }

    /**
     * Creates a slightly scaled version of the icon for hover effect.
     */
    private ImageIcon createHoverIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image hoverImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Slightly scale up
        return new ImageIcon(hoverImg);
    }

    /**
     * Custom paint method to keep the rounded shape, and when darkens the button when hovered
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color change on hover
        if (getModel().isRollover()) {
            g2.setColor(new Color(200, 200, 200, 150)); // Light gray when hovered
        } else {
            g2.setColor(new Color(255, 255, 255, 150)); // Default white
        }

        // Draw rounded button background
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }
}
