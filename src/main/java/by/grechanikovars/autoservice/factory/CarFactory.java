package by.grechanikovars.autoservice.factory;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class CarFactory {

    private final IdGenerator carIdGenerator;
    private final RepairResultFactory resultFactory;

    public CarFactory(IdGenerator carIdGenerator, RepairResultFactory resultFactory) {
        this.carIdGenerator = carIdGenerator;
        this.resultFactory = resultFactory;
    }

    public List<Car> createCars(List<Integer> partsPerCar) {
        List<Car> cars = new ArrayList<>();
        for (Integer partsNeeded : partsPerCar) {
            int carId = carIdGenerator.nextId();
            cars.add(new Car(carId, partsNeeded, resultFactory));
        }
        return cars;
    }
}