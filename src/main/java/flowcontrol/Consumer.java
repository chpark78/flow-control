package flowcontrol;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer<T> implements Runnable {

    private Buffer<T> buffer;

    private AtomicInteger counter = new AtomicInteger();

    public Consumer(Buffer<T> buffer) {
        this.buffer = buffer;
    }

    public int getAndReset() {
        return counter.getAndSet(0);
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.take();
                counter.incrementAndGet();
            }
        } catch (InterruptedException ignored) {
        }
    }

}
