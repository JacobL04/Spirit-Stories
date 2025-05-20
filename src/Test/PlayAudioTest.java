package src.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.components.PlayAudio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PlayAudio class.
 * This class contains tests to verify the functionality of the PlayAudio class.
 */
class PlayAudioTest {

    private static final String TEST_MUSIC_PATH = "src/Data/test_music.wav";
    private static final String TEST_SOUND_PATH = "src/Data/test_sound.wav";

    /**
     * Sets up the test environment before each test.
     * Ensures the test audio files exist.
     */
    @BeforeEach
    public void setUp() {
        assertTrue(new File(TEST_MUSIC_PATH).exists(), "Test music file should exist");
        assertTrue(new File(TEST_SOUND_PATH).exists(), "Test sound file should exist");
    }

    /**
     * Tests the PlayMusic method.
     * Verifies that the music is played correctly.
     */
    @Test
    public void testPlayMusic() {
        PlayAudio.PlayMusic(TEST_MUSIC_PATH, -5.0f);
        Clip musicClip = PlayAudio.getMusicClip();
        assertNotNull(musicClip, "Music clip should not be null");
        assertTrue(musicClip.isRunning(), "Music clip should be running");
    }

    /**
     * Tests the setMusicVolume method.
     * Verifies that the music volume is set correctly.
     */
    @Test
    public void testSetMusicVolume() {
        PlayAudio.PlayMusic(TEST_MUSIC_PATH, -5.0f);
        PlayAudio.setMusicVolume(50);
        Clip musicClip = PlayAudio.getMusicClip();
        assertNotNull(musicClip, "Music clip should not be null");
        FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
        float expectedGain = (50 / 100.0f) * (gainControl.getMaximum() - gainControl.getMinimum()) + gainControl.getMinimum();
        assertEquals(expectedGain, gainControl.getValue(), 0.01, "Music volume should be set correctly");
    }

    /**
     * Tests the PlaySound method.
     * Verifies that the sound is played correctly.
     */
    @Test
    public void testPlaySound() {
        PlayAudio.PlaySound(TEST_SOUND_PATH, -5.0f);
        Clip soundClip = PlayAudio.getSoundClip();
        assertNotNull(soundClip, "Sound clip should not be null");
        assertTrue(soundClip.isRunning(), "Sound clip should be running");
    }
}