package by.grechanikovars.autoservice.factory;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class CarFactory {

    private final IdGenerator idGenerator;

    public CarFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<Car> createCars(List<Integer> partsPerCar) {
        List<Car> cars = new ArrayList<>();
        for (Integer partsNeeded : partsPerCar) {
            int id = idGenerator.nextId();
            cars.add(new Car(id, partsNeeded));
        }
        return cars;
    }
}
