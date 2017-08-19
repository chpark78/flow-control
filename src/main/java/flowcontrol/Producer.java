package flowcontrol;

public class Producer<T> implements Runnable {

    private Buffer<T> buffer;

    private T element;

    public Producer(Buffer<T> buffer, T element) {
        this.buffer = buffer;
        this.element = element;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.put(element);
            }
        } catch (InterruptedException ignored) {
        }
    }

}
