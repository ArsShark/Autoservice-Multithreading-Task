package by.grechanikovars.autoservice.service.impl;

import by.grechanikovars.autoservice.entity.AutoService;
import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.RepairResult;
import by.grechanikovars.autoservice.exception.AutoServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRepairServiceImplTest {

    private static final int BAY_COUNT   = 3;
    private static final int PARTS_COUNT = 50;

    private static final Car CAR_ID_1_PARTS_2 = new Car(1, 2);
    private static final Car CAR_ID_2_PARTS_3 = new Car(2, 3);
    private static final Car CAR_ID_3_PARTS_1 = new Car(3, 1);

    private CarRepairServiceImpl repairService;

    @BeforeEach
    void setUp() {
        AutoService.resetForTest();
        AutoService.initialize(BAY_COUNT, PARTS_COUNT);
        repairService = new CarRepairServiceImpl();
    }

    @Test
    void repairAllCars_WhenThreeCarsAndSufficientParts_ShouldReturnSuccessfulResults()
            throws AutoServiceException {
        // given
        List<Car> cars = List.of(CAR_ID_1_PARTS_2, CAR_ID_2_PARTS_3, CAR_ID_3_PARTS_1);

        // when
        List<RepairResult> results = repairService.repairAllCars(cars);

        // then
        assertEquals(3, results.size());
        Stream<RepairResult> resultStream = results.stream();
        boolean allSucceeded = resultStream.allMatch(RepairResult::success);
        assertTrue(allSucceeded);
    }

    @Test
    void repairAllCars_WhenSingleCar_ShouldConsumeCorrectNumberOfParts()
            throws AutoServiceException {
        int partsNeeded = 4;
        List<Car> cars = List.of(new Car(1, partsNeeded));

        List<RepairResult> results = repairService.repairAllCars(cars);

        assertEquals(1, results.size());
        RepairResult result = results.get(0);
        int actualPartsUsed = result.partsUsed();
        assertEquals(partsNeeded, actualPartsUsed);
        assertTrue(result.success());
    }

    @Test
    void repairAllCars_WhenMoreCarsThanBays_ShouldServeAllCars()
            throws AutoServiceException {
        List<Car> cars = List.of(
                new Car(1, 1),
                new Car(2, 1),
                new Car(3, 1),
                new Car(4, 1),
                new Car(5, 1)
        );

        List<RepairResult> results = repairService.repairAllCars(cars);

        int totalResults = results.size();
        assertEquals(5, totalResults);
        Stream<RepairResult> resultStream = results.stream();
        long successCount = resultStream.filter(RepairResult::success).count();
        assertEquals(5L, successCount);
    }

    @Test
    void repairAllCars_WhenRepaired_ShouldHavePositiveRepairDuration()
            throws AutoServiceException {
        List<Car> cars = List.of(new Car(1, 2));

        List<RepairResult> results = repairService.repairAllCars(cars);

        RepairResult result = results.get(0);
        long duration = result.repairDurationMs();
        assertTrue(duration > 0);
    }
}
