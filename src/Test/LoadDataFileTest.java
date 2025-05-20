package src.Test;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Pages.components.LoadDataFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LoadDataFile class.
 * This class contains tests to verify the functionality of the LoadDataFile class.
 */
class LoadDataFileTest {

    private LoadDataFile loadDataFile;

    /**
     * Sets up the test environment before each test.
     * Initializes the LoadDataFile instance.
     */
    @BeforeEach
    public void setUp() {
        loadDataFile = new LoadDataFile("Game1.json");
    }

    /**
     * Tests the loadJsonFromFile method.
     * Verifies that the JSON object is loaded correctly from the file.
     */
    @Test
    public void testLoadJsonFromFile() {
        JsonObject jsonObject = loadDataFile.loadJsonFromFile("src/Data/Game1.json");
        assertNotNull(jsonObject, "JSON object should not be null");
        assertTrue(jsonObject.has("someKey"), "JSON object should contain 'someKey'");
    }

    /**
     * Tests the setJsonFile method with a file path.
     * Verifies that the JSON object is set correctly from the file.
     */
    @Test
    public void testSetJsonFileWithPath() {
        loadDataFile.setJsonFile("src/Data/Game1.json");
        JsonObject jsonObject = loadDataFile.getGameDataObject();
        assertNotNull(jsonObject, "JSON object should not be null");
        assertTrue(jsonObject.has("someKey"), "JSON object should contain 'someKey'");
    }

    /**
     * Tests the setJsonFile method with a JsonObject.
     * Verifies that the JSON object is set correctly.
     */
    @Test
    public void testSetJsonFileWithJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("testKey", "testValue");
        loadDataFile.setJsonFile(jsonObject);
        JsonObject result = loadDataFile.getGameDataObject();
        assertNotNull(result, "JSON object should not be null");
        assertEquals("testValue", result.get("testKey").getAsString(), "JSON object should contain 'testKey' with value 'testValue'");
    }

    /**
     * Tests the getGameDataObject method.
     * Verifies that the correct JSON object is returned.
     */
    @Test
    public void testGetGameDataObject() {
        JsonObject jsonObject = loadDataFile.getGameDataObject();
        assertNotNull(jsonObject, "JSON object should not be null");
    }

    /**
     * Tests the getGameDataFile method.
     * Verifies that the correct file is returned.
     */
    @Test
    public void testGetGameDataFile() {
        File file = loadDataFile.getGameDataFile();
        assertNotNull(file, "File should not be null");
        assertTrue(file.exists(), "File should exist");
    }

    /**
     * Tests the getFilePath method.
     * Verifies that the correct file path is returned.
     */
    @Test
    public void testGetFilePath() {
        String filePath = loadDataFile.getFilePath();
        assertNotNull(filePath, "File path should not be null");
        assertTrue(filePath.endsWith("Game1.json"), "File path should end with 'Game1.json'");
    }

    /**
     * Tests the loadJsonObjFromFile method.
     * Verifies that the JSON object is loaded correctly from the file.
     */
    @Test
    public void testLoadJsonObjFromFile() {
        JsonObject jsonObject = loadDataFile.loadJsonObjFromFile("src/Data/Game1.json");
        assertNotNull(jsonObject, "JSON object should not be null");
        assertTrue(jsonObject.has("someKey"), "JSON object should contain 'someKey'");
    }

    /**
     * Tests the saveJsonObjToFile method.
     * Verifies that the JSON object is saved correctly to the file.
     */
    @Test
    public void testSaveJsonObjToFile() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("testKey", "testValue");
        loadDataFile.saveJsonObjToFile("src/Data/TestSave.json", jsonObject);

        JsonObject loadedJson = loadDataFile.loadJsonObjFromFile("src/Data/TestSave.json");
        assertNotNull(loadedJson, "Loaded JSON object should not be null");
        assertEquals("testValue", loadedJson.get("testKey").getAsString(), "Loaded JSON object should contain 'testKey' with value 'testValue'");
    }
}