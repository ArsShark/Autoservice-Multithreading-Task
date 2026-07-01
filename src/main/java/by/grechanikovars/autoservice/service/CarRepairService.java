package by.grechanikovars.autoservice.service;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.RepairResult;
import by.grechanikovars.autoservice.exception.AutoServiceException;

import java.util.List;

public interface CarRepairService {
    List<RepairResult> repairAllCars(List<Car> cars) throws AutoServiceException;
}
