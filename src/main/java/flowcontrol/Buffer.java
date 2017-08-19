package flowcontrol;

public interface Buffer<T> {

    void put(T element) throws InterruptedException;

    T take() throws InterruptedException;

}
