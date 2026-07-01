package by.grechanikovars.autoservice.entity;

import by.grechanikovars.autoservice.exception.AutoServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PartsWarehouse {

    private static final Logger LOGGER = LogManager.getLogger(PartsWarehouse.class);
    private static final long TIMEOUT_SECONDS = 30L;

    private int availableParts;
    private final ReentrantLock lock;
    private final Condition partsAvailable;

    public PartsWarehouse(int initialParts) {
        this.availableParts = initialParts;
        this.lock = new ReentrantLock();
        this.partsAvailable = lock.newCondition();
    }

    public void takeParts(int count) throws InterruptedException, AutoServiceException {
        lock.lock();
        try {
            long timeoutNanos = TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
            while (availableParts < count) {
                timeoutNanos = partsAvailable.awaitNanos(timeoutNanos);
                if (timeoutNanos <= 0) {
                    String message = String.format(
                            "Timeout waiting for parts. Needed: %d, available: %d",
                            count, availableParts
                    );
                    throw new AutoServiceException(message);
                }
            }
            availableParts -= count;
            LOGGER.info("Parts taken from warehouse: {}. Remaining: {}.", count, availableParts);
        } finally {
            lock.unlock();
        }
    }

    public int getAvailableParts() {
        lock.lock();
        try {
            return availableParts;
        } finally {
            lock.unlock();
        }
    }
}
