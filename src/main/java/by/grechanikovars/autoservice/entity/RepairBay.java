package by.grechanikovars.autoservice.entity;

import java.util.concurrent.locks.ReentrantLock;

public class RepairBay {

    private final int id;
    private final ReentrantLock lock;

    public RepairBay(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public boolean tryOccupy() {
        return lock.tryLock();
    }

    public void release() {
        lock.unlock();
    }

    public int getId() {
        return id;
    }
}
