package su.grinev.FileUploader.utility;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountingSemaphore {

    private AtomicInteger counter;

    public CountingSemaphore(){
        counter=new AtomicInteger();
    }

    public void countUp() {
        this.counter.incrementAndGet();
    }

    public void countDown() {
        if (this.counter.decrementAndGet() == 0) {
            synchronized (this) {
                this.notify();
            }
        }
    }

    public void await(long time, TimeUnit timeunit) throws InterruptedException {
        if (counter.get() > 0) {
            synchronized (this) {
                this.wait(timeunit.toMillis(time));
            }
        }
    }

    public void await() throws InterruptedException {
        if (counter.get() > 0) {
            synchronized (this) {
                this.wait();
            }
        }
    }
}
