package flowcontrol;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.currentTimeMillis;

public class ThreadLessControlBuffer<T> extends SemaphoreBasedControlBuffer<T> {

    private int checkpointIntervalMillis;

    private final AtomicLong lastCheckpointMillis = new AtomicLong(0);

    public ThreadLessControlBuffer(int capacity, TimeUnit perTimeUnit, int checkpointIntervalMillis) {
        super(capacity, perTimeUnit, checkpointIntervalMillis);
        this.checkpointIntervalMillis = checkpointIntervalMillis;
    }

    @Override
    protected void acquirePermit(Semaphore semaphore) throws InterruptedException {
        do {
            releaseAllPermitsIfNecessary();
        } while (!semaphore.tryAcquire(100, TimeUnit.MICROSECONDS));
    }

    private void releaseAllPermitsIfNecessary() {
        long currTimeMillis = currentTimeMillis();
        long lastTimeMillis = lastCheckpointMillis.get();
        if (currTimeMillis - lastTimeMillis >= checkpointIntervalMillis) {
            if (lastCheckpointMillis.compareAndSet(lastTimeMillis, currTimeMillis)) {
                releaseAllPermits();
            }
        }
    }

}
