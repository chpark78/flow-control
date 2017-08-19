package flowcontrol;

import java.time.LocalDateTime;

public class Counter implements Runnable {

    private Consumer consumer;

    private int periodSeconds;

    public Counter(Consumer consumer, int periodSeconds) {
        this.consumer = consumer;
        this.periodSeconds = periodSeconds;
    }

    @Override
    public void run() {
        System.out.println(LocalDateTime.now() + " " + consumer.getAndReset() / periodSeconds);
    }

}
