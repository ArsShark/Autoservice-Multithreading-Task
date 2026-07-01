package by.grechanikovars.autoservice.parser;

import by.grechanikovars.autoservice.entity.ServiceConfig;
import by.grechanikovars.autoservice.exception.AutoServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ServiceConfigParser {

    private static final Logger LOGGER = LogManager.getLogger(ServiceConfigParser.class);

    private static final Pattern SPLIT_PATTERN    = Pattern.compile("=");
    private static final Pattern CAR_LIST_PATTERN = Pattern.compile(",");

    private static final String KEY_BAYS            = "bays";
    private static final String KEY_WAREHOUSE_PARTS = "warehouse_parts";
    private static final String KEY_CARS            = "cars";

    public ServiceConfig parse(List<String> lines) throws AutoServiceException {
        int bayCount = 0;
        int warehouseParts = 0;
        List<Integer> carPartsNeeded = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            String[] fields = SPLIT_PATTERN.split(trimmed, 2);
            if (fields.length != 2) {
                String message = String.format("Invalid config line: [%s]", trimmed);
                throw new AutoServiceException(message);
            }
            String key   = fields[0].trim();
            String value = fields[1].trim();

            switch (key) {
                case KEY_BAYS            -> bayCount       = Integer.parseInt(value);
                case KEY_WAREHOUSE_PARTS -> warehouseParts = Integer.parseInt(value);
                case KEY_CARS            -> carPartsNeeded = parseCarList(value);
                default                  -> LOGGER.warn("Unknown config key ignored: {}", key);
            }
        }

        LOGGER.info("Config parsed: bays={}, warehouse_parts={}, cars={}",
                bayCount, warehouseParts, carPartsNeeded.size());
        return new ServiceConfig(bayCount, warehouseParts, carPartsNeeded);
    }

    private List<Integer> parseCarList(String value) {
        String[] tokens = CAR_LIST_PATTERN.split(value);
        List<Integer> result = new ArrayList<>();
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                result.add(Integer.parseInt(trimmed));
            }
        }
        return result;
    }
}
