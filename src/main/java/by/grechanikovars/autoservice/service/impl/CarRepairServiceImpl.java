package by.grechanikovars.autoservice.service.impl;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.RepairResult;
import by.grechanikovars.autoservice.exception.AutoServiceException;
import by.grechanikovars.autoservice.service.CarRepairService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CarRepairServiceImpl implements CarRepairService {

    private static final Logger LOGGER = LogManager.getLogger(CarRepairServiceImpl.class);
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 60L;

    @Override
    public List<RepairResult> repairAllCars(List<Car> cars) throws AutoServiceException {
        int carCount = cars.size();
        ExecutorService executor = Executors.newFixedThreadPool(carCount);
        List<Future<RepairResult>> futures = new ArrayList<>();

        for (Car car : cars) {
            Future<RepairResult> future = executor.submit(car);
            futures.add(future);
        }

        List<RepairResult> results = new ArrayList<>();
        for (Future<RepairResult> future : futures) {
            try {
                RepairResult result = future.get();
                results.add(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AutoServiceException("Repair process was interrupted.", e);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String causeMessage = cause.getMessage();
                throw new AutoServiceException("Error during car repair: " + causeMessage, cause);
            }
        }

        executor.shutdown();
        awaitExecutorTermination(executor);
        LOGGER.info("All {} car(s) have been serviced.", carCount);
        return results;
    }

    private void awaitExecutorTermination(ExecutorService executor) throws AutoServiceException {
        try {
            boolean terminated = executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!terminated) {
                LOGGER.warn("Executor did not terminate within {}s, forcing shutdown.", SHUTDOWN_TIMEOUT_SECONDS);
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AutoServiceException("Interrupted while awaiting executor termination.", e);
        }
    }
}
