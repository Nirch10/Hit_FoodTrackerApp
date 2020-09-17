package foodTrackerServer.Config;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FoodTrackerServerConfigWrapper {

    public static FoodTrackerServerConfig foodTrackerServerConfig;

    public static void Serialize(FoodTrackerServerConfig inFoodTrackerServerConfig, String filePath) throws IOException {
        Gson jsonParser = new Gson();
        foodTrackerServerConfig = inFoodTrackerServerConfig;
        String json = jsonParser.toJson(foodTrackerServerConfig);
        Serialize(json, filePath);
    }

    private static void Serialize(String data, String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(data);
        fw.close();
    }
    public static FoodTrackerServerConfig Deserialize(String configPath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(configPath)));
        Gson jsonParser = new Gson();
        foodTrackerServerConfig = jsonParser.fromJson(content, FoodTrackerServerConfig.class);
        return foodTrackerServerConfig;
    }
}

