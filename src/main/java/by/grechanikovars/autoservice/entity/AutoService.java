package by.grechanikovars.autoservice.entity;

import by.grechanikovars.autoservice.exception.AutoServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class AutoService {

    private static final Logger LOGGER = LogManager.getLogger(AutoService.class);

    private static final AtomicReference<AutoService> INSTANCE = new AtomicReference<>();
    private static final ReentrantLock INIT_LOCK = new ReentrantLock();

    private final Semaphore baysSemaphore;
    private final List<RepairBay> repairBays;
    private final PartsWarehouse warehouse;

    private AutoService(int bayCount, int partsCount) {
        this.baysSemaphore = new Semaphore(bayCount, true);
        List<RepairBay> bays = new ArrayList<>();
        for (int i = 1; i <= bayCount; i++) {
            bays.add(new RepairBay(i));
        }
        this.repairBays = List.copyOf(bays);
        this.warehouse = new PartsWarehouse(partsCount);
        LOGGER.info("AutoService initialized: {} bay(s), {} part(s) in warehouse.", bayCount, partsCount);
    }

    public static AutoService initialize(int bayCount, int partsCount) {
        AutoService existing = INSTANCE.get();
        if (existing != null) {
            return existing;
        }
        INIT_LOCK.lock();
        try {
            existing = INSTANCE.get();
            if (existing == null) {
                AutoService newInstance = new AutoService(bayCount, partsCount);
                INSTANCE.set(newInstance);
                return newInstance;
            }
            return existing;
        } finally {
            INIT_LOCK.unlock();
        }
    }

    public static AutoService getInstance() throws AutoServiceException {
        AutoService instance = INSTANCE.get();
        if (instance == null) {
            throw new AutoServiceException("AutoService is not initialized. Call initialize() first.");
        }
        return instance;
    }

    public RepairBay acquireAvailableBay() throws InterruptedException, AutoServiceException {
        baysSemaphore.acquire();
        for (RepairBay bay : repairBays) {
            if (bay.tryOccupy()) {
                return bay;
            }
        }
        baysSemaphore.release();
        throw new AutoServiceException("Failed to acquire a bay after semaphore acquisition.");
    }

    public void releaseRepairBay(RepairBay bay) {
        bay.release();
        baysSemaphore.release();
    }

    public PartsWarehouse getWarehouse() {
        return warehouse;
    }

}
