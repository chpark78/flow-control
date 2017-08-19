package flowcontrol;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws Exception {
//        Buffer<Boolean> buffer = new ThreadBasedControlBuffer<>(20000, TimeUnit.SECONDS, 10);
        Buffer<Boolean> buffer = new ThreadLessControlBuffer<>(20000, TimeUnit.SECONDS, 10);

        int producerCount = 40;

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(producerCount + 2);

        Producer<Boolean> producer = new Producer<>(buffer, Boolean.TRUE, 1);
        for (int i = 0; i < producerCount; i++) {
            threadPool.submit(producer);
        }

        Consumer<Boolean> consumer = new Consumer<>(buffer);
        threadPool.submit(consumer);

        int countIntervalSeconds = 5;
        Counter counter = new Counter(consumer, countIntervalSeconds);
        threadPool.scheduleAtFixedRate(counter, countIntervalSeconds, countIntervalSeconds, TimeUnit.SECONDS);

        threadPool.awaitTermination(1, TimeUnit.DAYS);
    }

}
