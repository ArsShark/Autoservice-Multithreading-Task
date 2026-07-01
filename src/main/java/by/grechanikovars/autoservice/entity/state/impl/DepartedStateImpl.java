package by.grechanikovars.autoservice.entity.state.impl;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartedStateImpl implements CarState {

    private static final Logger LOGGER = LogManager.getLogger(DepartedStateImpl.class);

    @Override
    public void onEnter(Car car) {
        int carId = car.getId();
        LOGGER.info("Car {} [DEPARTED] — has left the auto service.", carId);
    }

    @Override
    public String getStateName() {
        return "DEPARTED";
    }
}
