package su.grinev.FileUploader.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Component
public class CustomFixedThreadPool implements ExecutorService {

    private final BlockingQueue<TaskWrapper> tasks;
    private final ArrayList<Worker> workerList;
    private final CountingSemaphore countingSemaphore;

    public CustomFixedThreadPool(
            @Value("${MAX_DATA_CONNECTIONS}")
            int threads) {
        if (threads==0) throw new IllegalArgumentException();
        this.tasks=new LinkedBlockingQueue<>();
        this.countingSemaphore=new CountingSemaphore();
        this.workerList=new ArrayList<>();
        for (int i=0;i!=threads;i++){
            Worker worker=new Worker(tasks, countingSemaphore);
            worker.setThread(new Thread(worker));
            workerList.add(worker);
        }
    }

    public void enqueueTask(TaskWrapper task){
        try {
            task.setQueuedState();
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
            this.workerList.forEach(t->{
                t.stopConsume();
                t.shutdown(false);
            });
    }

    @Override
    public List<Runnable> shutdownNow() {
        this.workerList.forEach(t->{
            t.stopConsume();
            t.shutdown(true);
        });
        List<Runnable> result=new ArrayList<>();
        this.tasks.forEach(t->result.add(t.getTask()));
        this.workerList.clear();
        return result;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        try {
            countingSemaphore.await(timeout, unit);
            return true;
        }
        catch (InterruptedException e){
            return false;
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        TaskWrapper future=new TaskWrapper(task, result);
        this.enqueueTask(future);
        return future;
    }

    @Override
    public Future<?> submit(Runnable task) {
        TaskWrapper future=new TaskWrapper(task);
        this.enqueueTask(future);
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void execute(Runnable command) {
        this.enqueueTask(new TaskWrapper(command));
    }
}