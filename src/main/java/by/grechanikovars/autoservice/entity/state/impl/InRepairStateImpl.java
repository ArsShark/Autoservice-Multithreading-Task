package by.grechanikovars.autoservice.entity.state.impl;

import by.grechanikovars.autoservice.entity.Car;
import by.grechanikovars.autoservice.entity.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InRepairStateImpl implements CarState {

    private static final Logger LOGGER = LogManager.getLogger(InRepairStateImpl.class);

    @Override
    public void onEnter(Car car) {
        int carId = car.getId();
        int partsNeeded = car.getPartsNeeded();
        LOGGER.info("Car {} [IN_REPAIR] — repair started, using {} part(s).", carId, partsNeeded);
    }

    @Override
    public String getStateName() {
        return "IN_REPAIR";
    }
}
