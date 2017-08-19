package flowcontrol;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class Buffer<T> {

    private final ScheduledExecutorService resetScheduler = newSingleThreadScheduledExecutor();

    private final BlockingQueue<T> store = new LinkedBlockingQueue<>();

    private final int totalPermits;

    private final Semaphore semaphore;

    public Buffer(int capacity, TimeUnit perTimeUnit, int timeSliceMillis) {
        int timeSlices = (int) (perTimeUnit.toMillis(1) / timeSliceMillis);
        this.totalPermits = capacity / timeSlices;
        this.semaphore = new Semaphore(totalPermits);
        this.resetScheduler.scheduleAtFixedRate(
                () -> {
                    if (store.size() < totalPermits) { // consume 속도가 produce 속도에 뒤쳐지는 경우에는 blocking 유지
                        semaphore.release(totalPermits - semaphore.availablePermits());
                    }
                },
                timeSliceMillis, timeSliceMillis, TimeUnit.MILLISECONDS);
    }

    public void put(T element) throws InterruptedException {
        semaphore.acquire();
        store.put(element);
    }

    public T take() throws InterruptedException {
        return store.take();
    }

}
