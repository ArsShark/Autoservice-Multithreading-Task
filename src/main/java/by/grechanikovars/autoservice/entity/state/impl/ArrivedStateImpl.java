package by.grechanikovars.autoservice.entity.state.impl;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrivedStateImpl implements CarState {

    private static final Logger LOGGER = LogManager.getLogger(ArrivedStateImpl.class);

    @Override
    public void onEnter(Car car) {
        int carId = car.getId();
        int partsNeeded = car.getPartsNeeded();
        LOGGER.info("Car {} [ARRIVED] — waiting for a free bay. Parts needed: {}.", carId, partsNeeded);
    }

    @Override
    public String getStateName() {
        return "ARRIVED";
    }
}
