package flowcontrol;

public class Producer<T> implements Runnable {

    private Buffer<T> buffer;

    private T element;

    private long delayMillis;

    public Producer(Buffer<T> buffer, T element, long delayMillis) {
        this.buffer = buffer;
        this.element = element;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.put(element);
                if (delayMillis > 0) {
                    Thread.sleep(delayMillis);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

}
