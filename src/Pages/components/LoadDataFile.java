package src.Pages.components;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The Load Data file class saves and loads the data from a file into a JSON file
 * <p>
 * The data from the file will added to a JSON file as a JSON object
 * <p>
 * @version 1.0
 */
public class LoadDataFile {

    private static String path;
    private static JsonObject gameData;

    /**
     * Data file is initially null
     */
    public LoadDataFile() {

        gameData = null;
    }

    /**
     * A datafile for the game is created with the String filename
     * @param filename name of the data file
     */
    public LoadDataFile(String filename) {

        String filepath = "src/Data/" + filename;
        path = filepath;
        gameData = loadJsonFromFile(filepath);

    }

    /**
     * Store all the data from the file name to a JSON file
     * @param filePath path to the file containing data
     * @return A JsonObject storing all the data
     */
    public JsonObject loadJsonFromFile(String filePath) {
        // Read file content into a string
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException exception) {
            System.out.println("Error reading JSON from " + filePath);
            System.out.println(exception);
            return null;
        }

        // Parse string into JsonObject
        return JsonParser.parseString(content.toString()).getAsJsonObject();
    }

    /**
     * Load data into a Json file with the name Json
     * @param json name of the Json file
     */
    public void setJsonFile(String json) {
        gameData = loadJsonFromFile(json);
    }

    /**
     * Stores the JsonObject containing all the data into gameData
     * @param json Json containing data
     */
    public void setJsonFile(JsonObject json) {
        gameData = json;
    }

    /**
     * Gets the JsonObject
     * @return Json object containing all data
     */
    public JsonObject getGameDataObject() {
        return gameData;
    }

    /**
     * Get the game data file
     * @return creates a data file into the path
     */
    public File getGameDataFile() {
        return new File(path);
    }

    /**
     * Get path of the data file
     * @return the path to the data file
     */
    public String getFilePath() {
        return path;
    }

    /**
     * Load Json data from the file
     * @param filepath path to the json file
     * @return JsonObject containing data
     */
    public JsonObject loadJsonObjFromFile(String filepath){
        return loadJsonFromFile(filepath);
    }

    /**
     * Update the Json data into a file
     * @param filePath path to the file
     * @param jsonObject Json object data that is written to the file
     */
    public void saveJsonObjToFile(String filePath, JsonObject jsonObject) { // Saves JSON modification
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Convert JsonObject to JSON string and write to file
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
