import com.fasterxml.jackson.databind.ObjectMapper;
import model.Config;
import service.GameMatrixService;
import service.GameResultService;

import java.io.File;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String configFilePath = null;
        double betAmount = 0.0;
        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--config")) {
                configFilePath = args[++i];
            } else if (args[i].equals("--betting-amount")) {
                betAmount = Double.parseDouble(args[++i]);
            }
        }
        if (configFilePath == null || betAmount <= 0) {
            System.out.println("Usage: java -jar scratch-game.jar --config config.json --betting-amount 100");
            System.exit(1);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new File(configFilePath), Config.class);
            GameMatrixService gameMatrixService = new GameMatrixService(config);
            gameMatrixService.generateMatrix();
            GameResultService gameResultService = new GameResultService(gameMatrixService.getMatrix(), betAmount, config);
            gameResultService.calculateReward();
            Map<String, Object> result = gameResultService.toResultMap();
            String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
