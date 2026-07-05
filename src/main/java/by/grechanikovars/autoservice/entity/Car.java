package by.grechanikovars.autoservice.entity;

import by.grechanikovars.autoservice.entity.state.CarState;
import by.grechanikovars.autoservice.entity.state.impl.ArrivedStateImpl;
import by.grechanikovars.autoservice.entity.state.impl.DepartedStateImpl;
import by.grechanikovars.autoservice.entity.state.impl.InRepairStateImpl;
import by.grechanikovars.autoservice.entity.state.impl.RepairedStateImpl;
import by.grechanikovars.autoservice.factory.RepairResultFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Car implements Callable<RepairResult> {

    private static final Logger LOGGER = LogManager.getLogger(Car.class);
    private static final long MS_PER_PART = 200L;

    private final int id;
    private final int partsNeeded;
    private final RepairResultFactory resultFactory;
    private CarState state;

    public Car(int id, int partsNeeded, RepairResultFactory resultFactory) {
        this.id = id;
        this.partsNeeded = partsNeeded;
        this.resultFactory = resultFactory;
        this.state = new ArrivedStateImpl();
    }

    @Override
    public RepairResult call() throws Exception {
        Instant startTime = Instant.now();
        state.onEnter(this);

        ServiceStation station = ServiceStation.getInstance();
        RepairBay bay = station.acquireAvailableBay();
        int bayId = bay.getId();
        LOGGER.info("Car {} entered bay {}.", id, bayId);

        setState(new InRepairStateImpl());

        try {
            PartsWarehouse warehouse = station.getWarehouse();
            warehouse.takeParts(partsNeeded);

            long repairTime = MS_PER_PART * partsNeeded;
            TimeUnit.MILLISECONDS.sleep(repairTime);

            setState(new RepairedStateImpl());
        } finally {
            station.releaseRepairBay(bay);
            LOGGER.info("Car {} released bay {}.", id, bayId);
        }

        setState(new DepartedStateImpl());

        Duration repairDuration = Duration.between(startTime, Instant.now());
        long repairDurationMs = repairDuration.toMillis();
        return resultFactory.create(id, bayId, partsNeeded, repairDurationMs, true);
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