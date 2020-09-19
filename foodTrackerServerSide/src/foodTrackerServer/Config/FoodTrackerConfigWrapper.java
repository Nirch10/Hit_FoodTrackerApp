package foodTrackerServer.Config;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FoodTrackerConfigWrapper {

    public static FoodTrackerConfig foodTrackerConfig;

    public static FoodTrackerConfig Deserialize(String configPath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(configPath)));
        Gson jsonParser = new Gson();
        foodTrackerConfig = jsonParser.fromJson(content, FoodTrackerConfig.class);
        return foodTrackerConfig;
    }
}

