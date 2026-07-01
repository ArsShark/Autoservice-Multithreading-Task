package by.grechanikovars.autoservice.entity.state.impl;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RepairedStateImpl implements CarState {

    private static final Logger LOGGER = LogManager.getLogger(RepairedStateImpl.class);

    @Override
    public void onEnter(Car car) {
        int carId = car.getId();
        LOGGER.info("Car {} [REPAIRED] — fully repaired, ready to depart.", carId);
    }

    @Override
    public String getStateName() {
        return "REPAIRED";
    }
}
