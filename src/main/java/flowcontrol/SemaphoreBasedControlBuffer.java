package flowcontrol;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public abstract class SemaphoreBasedControlBuffer<T> implements Buffer<T> {

    private final BlockingQueue<T> store = new LinkedBlockingQueue<>();

    private final int totalPermits;

    private final Semaphore semaphore;

    public SemaphoreBasedControlBuffer(int capacity, TimeUnit perTimeUnit, int checkpointIntervalMillis) {
        int timeSlices = (int) (perTimeUnit.toMillis(1) / checkpointIntervalMillis);
        this.totalPermits = capacity / timeSlices;
        this.semaphore = new Semaphore(totalPermits);
    }

    protected abstract void acquirePermit(Semaphore semaphore) throws InterruptedException;

    @Override
    public void put(T element) throws InterruptedException {
        acquirePermit(semaphore);
        store.put(element);
    }

    @Override
    public T take() throws InterruptedException {
        return store.take();
    }

    protected void releaseAllPermits() {
        if (store.size() < totalPermits) { // consume 속도가 produce 속도에 뒤쳐지는 경우에는 blocking 유지
            int permits = totalPermits - semaphore.availablePermits();
            if (permits > 0) {
                semaphore.release(permits);
            }
        }
    }

}
