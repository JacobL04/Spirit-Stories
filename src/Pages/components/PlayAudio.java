package src.Pages.components;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * The PlayAudio class creates the background music and audio press sound effect 
 * <p>
 * The music and audio effect volume can be changed by the user in the settings window
 * <p>
 * @version 1.0
 */
public class PlayAudio {
    private static Clip musicClip;
    private static Clip soundClip;

    /**
     * Plays the music file 
     * @param music path to the music file
     * @param gain adjusts volume of the music file
     */

    public static void PlayMusic(String music, float gain){
        try {
            File musicPath = new File(music);

            // Check if the music file exists
            if (musicPath.exists()){
                // Load the audio stream from the file
                AudioInputStream audioInput;
                audioInput = AudioSystem.getAudioInputStream(musicPath);

                // Create a clip to play the audio
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);

                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gain); // adjust volumn/gain
                // gainControl.setValue(-5.0f); // adjust volume/gain
                gainControl.setValue(gain); // adjust volume/gain

                // Loop the music continuously and start playback
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();

                // Save the music clip for future volume control
                musicClip = clip;           
            }
            
            else {
                System.out.println("Can't find music file");
            }
        }

        catch(Exception exception) {
            System.out.println(exception);
        }
    }
    /**
     * Set and update the music volume
     * @param volumePercentage
     */
    public static void setMusicVolume(int volumePercentage) {
        if (musicClip != null && musicClip.isRunning()) {
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float minGain = gainControl.getMinimum();
            float maxGain = gainControl.getMaximum();
            float gain = (volumePercentage / 100.0f) * (maxGain - minGain) + minGain;
            gainControl.setValue(gain);
        }
    }

    /**
     * Plays the sound effect file
     * @param sound path to the sound effect file
     * @param gain adjusts volumn gain of the sound effect file
     */
    public static void PlaySound(String sound, float gain){
        try {
            File soundPath = new File(sound);

            // Check if the sound file exists
            if (soundPath.exists()){
                // Load the audio stream
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                
                // Create a clip to play the sound
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                
                // Set the volume
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gain); // adjust volume/gain
                
                // Play the sound once
                clip.start();       
                
                // Save the sound clip
                soundClip = clip;     
            }
            
            else {
                System.out.println("Can't find music file");
            }
        }

        catch(Exception exception) {
            System.out.println(exception);
        }
    }

    /**
     * Gets the music file
     * @return music file
     */
    public static Clip getMusicClip() {
        return musicClip;
    }

    /**
     * Get the sound file 
     * @return sound file
     */
    public static Clip getSoundClip() {
        return soundClip;
    }
}
