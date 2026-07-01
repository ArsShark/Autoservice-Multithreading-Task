package by.grechanikovars.autoservice;

import by.grechanikovars.autoservice.entity.AutoService;
import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.RepairResult;
import by.grechanikovars.autoservice.entity.ServiceConfig;
import by.grechanikovars.autoservice.exception.AutoServiceException;
import by.grechanikovars.autoservice.factory.CarFactory;
import by.grechanikovars.autoservice.parser.ServiceConfigParser;
import by.grechanikovars.autoservice.reader.TaskFileReader;
import by.grechanikovars.autoservice.reader.impl.TaskFileReaderImpl;
import by.grechanikovars.autoservice.service.CarRepairService;
import by.grechanikovars.autoservice.service.impl.CarRepairServiceImpl;
import by.grechanikovars.autoservice.util.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String CONFIG_FILE = "data/config.txt";

    public static void main(String[] args) {
        try {
            TaskFileReader reader = new TaskFileReaderImpl();
            List<String> lines = reader.readLines(CONFIG_FILE);
            ServiceConfigParser parser = new ServiceConfigParser();
            ServiceConfig config = parser.parse(lines);

            int bayCount = config.bayCount();
            int warehouseParts = config.warehouseParts();
            AutoService.initialize(bayCount, warehouseParts);

            CarFactory carFactory = new CarFactory(new IdGenerator());
            List<Car> cars = carFactory.createCars(config.carPartsNeeded());
            int carCount = cars.size();
            LOGGER.info("=== Auto service simulation start: {} car(s), {} bay(s). ===", carCount, bayCount);

            CarRepairService repairService = new CarRepairServiceImpl();
            List<RepairResult> results = repairService.repairAllCars(cars);

            LOGGER.info("=== Repair Session Summary ===");
            for (RepairResult result : results) {
                LOGGER.info("  {}", result);
            }
            int totalRepaired = results.size();
            LOGGER.info("Total cars repaired: {}.", totalRepaired);

        } catch (AutoServiceException e) {
            LOGGER.error("Auto service error: {}", e.getMessage(), e);
        }
    }
}
