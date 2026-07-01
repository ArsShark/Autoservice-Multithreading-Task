package by.grechanikovars.autoservice.factory;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.util.IdGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarFactoryTest {

    @Test
    void createCars_WhenGivenPartsList_ShouldAssignSequentialIds() {
        // given
        CarFactory factory = new CarFactory(new IdGenerator());
        List<Integer> partsPerCar = List.of(3, 2, 5);
        // when
        List<Car> cars = factory.createCars(partsPerCar);
        // then
        assertEquals(3, cars.size());
        assertEquals(1, cars.get(0).getId());
        assertEquals(2, cars.get(1).getId());
        assertEquals(3, cars.get(2).getId());
    }

    @Test
    void createCars_WhenGivenPartsList_ShouldPreservePartsNeededOrder() {
        CarFactory factory = new CarFactory(new IdGenerator());
        List<Integer> partsPerCar = List.of(4, 1, 7);

        List<Car> cars = factory.createCars(partsPerCar);

        assertEquals(4, cars.get(0).getPartsNeeded());
        assertEquals(1, cars.get(1).getPartsNeeded());
        assertEquals(7, cars.get(2).getPartsNeeded());
    }

    @Test
    void createCars_WhenEmptyList_ShouldReturnEmptyResult() {
        CarFactory factory = new CarFactory(new IdGenerator());
        List<Integer> partsPerCar = List.of();

        List<Car> cars = factory.createCars(partsPerCar);

        assertEquals(0, cars.size());
    }
}
