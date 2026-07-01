package by.grechanikovars.autoservice.entity;

import by.grechanikovars.autoservice.entity.state.impl.*;
import by.grechanikovars.autoservice.entity.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Car implements Callable<RepairResult> {

    private static final Logger LOGGER = LogManager.getLogger(Car.class);

    private static final long MS_PER_PART = 200L;

    private final int id;
    private final int partsNeeded;
    private CarState state;

    public Car(int id, int partsNeeded) {
        this.id = id;
        this.partsNeeded = partsNeeded;
        this.state = new ArrivedStateImpl();
    }

    @Override
    public RepairResult call() throws Exception {
        long startTime = System.currentTimeMillis();

        setState(new ArrivedStateImpl());

        AutoService autoService = AutoService.getInstance();
        RepairBay bay = autoService.acquireAvailableBay();
        int bayId = bay.getId();
        LOGGER.info("Car {} entered bay {}.", id, bayId);

        setState(new InRepairStateImpl());

        try {
            PartsWarehouse warehouse = autoService.getWarehouse();
            warehouse.takeParts(partsNeeded);
            LOGGER.info("Car {} received {} part(s) from warehouse.", id, partsNeeded);

            long repairTime = MS_PER_PART * partsNeeded;
            TimeUnit.MILLISECONDS.sleep(repairTime);

            setState(new RepairedStateImpl());
        } finally {
            autoService.releaseRepairBay(bay);
            LOGGER.info("Car {} released bay {}.", id, bayId);
        }

        setState(new DepartedStateImpl());

        long duration = System.currentTimeMillis() - startTime;
        return new RepairResult(id, bayId, partsNeeded, duration, true);
    }

    private void setState(CarState newState) {
        this.state = newState;
        this.state.onEnter(this);
    }

    public int getId() {
        return id;
    }

    public int getPartsNeeded() {
        return partsNeeded;
    }

    public String getStateName() {
        return state.getStateName();
    }
}
