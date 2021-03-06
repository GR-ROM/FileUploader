package su.grinev.FileUploader.utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskWrapper implements Future {

    private final Runnable task;
    private CountingSemaphore semaphore;
    private Object T;
    private Thread workerThread;
    private int state;
    public final static int CREATED=0;
    public final static int QUEUED=1;
    public final static int RUNNING=2;
    public final static int DONE=3;
    public final static int CANCELLED=4;

    public TaskWrapper(Runnable task, Object T){
        this(task);
        this.T=T;
    }

    public TaskWrapper(Runnable task) {
        this.task = task;
        this.semaphore=new CountingSemaphore();
        this.workerThread =null;
        this.state=CREATED;
    }

    public void setCancelledState(){
        this.state=CANCELLED;
    }

    public void setQueuedState(){
        if (this.state==CREATED){
            this.state=QUEUED;
        } else throw new IllegalStateException();
    }

    public void setRunningState(){
        if (this.state==QUEUED){
            this.state=RUNNING;
            this.semaphore.countUp();
        } else throw new IllegalStateException();
    }

    public void setDoneState(){
        if (this.state==RUNNING){
            this.state=DONE;
            this.semaphore.countDown();
        } else throw new IllegalStateException();
    }

    public void setWorkerThread(Thread workerThread) {
        this.workerThread = workerThread;
    }

    public Runnable getTask() {
        return task;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (workerThread == null) return false;
        if (mayInterruptIfRunning && this.state == RUNNING){
            workerThread.interrupt();
            this.semaphore.countDown();
            this.state=CANCELLED;
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return state == CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state == DONE;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        this.semaphore.await();
        return this.T;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        this.semaphore.await(timeout, unit);
        return this.T;
    }
}
