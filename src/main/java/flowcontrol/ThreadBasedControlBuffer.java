package flowcontrol;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class ThreadBasedControlBuffer<T> extends SemaphoreBasedControlBuffer<T> {

    private final ScheduledExecutorService checkpointScheduler = newSingleThreadScheduledExecutor();

    public ThreadBasedControlBuffer(int capacity, TimeUnit perTimeUnit, int checkpointIntervalMillis) {
        super(capacity, perTimeUnit, checkpointIntervalMillis);
        checkpointScheduler.scheduleAtFixedRate(
                this::releaseAllPermits,
                checkpointIntervalMillis,
                checkpointIntervalMillis,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    protected void acquirePermit(Semaphore semaphore) throws InterruptedException {
        semaphore.acquire();
    }

}
