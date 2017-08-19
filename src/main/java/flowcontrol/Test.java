package flowcontrol;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws Exception {
        Buffer<Boolean> buffer = new Buffer<>(20000, TimeUnit.SECONDS, 10);
        Producer<Boolean> producer = new Producer<>(buffer, Boolean.TRUE);
        Consumer<Boolean> consumer = new Consumer<>(buffer);

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        threadPool.submit(producer);
        threadPool.submit(producer);
        threadPool.submit(consumer);
        threadPool.scheduleAtFixedRate(new Counter(consumer, 5), 5, 5, TimeUnit.SECONDS);

        threadPool.awaitTermination(1, TimeUnit.DAYS);
    }

}
