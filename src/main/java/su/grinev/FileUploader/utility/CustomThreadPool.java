package su.grinev.FileUploader.utility;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Component
public class CustomThreadPool implements ExecutorService {

    private final BlockingQueue<TaskWrapper> tasks;
    private final ArrayList<Thread> threadList;
    private final ArrayList<Worker> workerList;
    private final CountingSemaphore countingSemaphore;

    public CustomThreadPool(
            @Value("${MAX_DATA_CONNECTIONS}")
            int threads) {
        if (threads==0) throw new IllegalArgumentException();
        this.tasks=new LinkedBlockingQueue<>();
        this.countingSemaphore=new CountingSemaphore();
        this.threadList=new ArrayList<>();
        this.workerList=new ArrayList<>();
        for (int i=0;i!=threads;i++){
            Worker worker=new Worker(tasks, countingSemaphore);
            Thread thread=new Thread(worker);
            worker.setThread(thread);
            workerList.add(worker);
            threadList.add(thread);
        }
        startExecutor();
    }

    public synchronized void enqueueTask(TaskWrapper task){
        try {
            countingSemaphore.countUp();
            task.setQueuedState();
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startExecutor(){
        try {
            threadList.forEach(t -> {
                t.start();
            });
        }
        catch (IllegalThreadStateException e){
            System.out.println(e.toString());
        }
    }

    public void terminateAll(){
        this.purgeQueue();
        this.workerList.forEach(t->t.terminateWorker());
        this.workerList.clear();
        this.threadList.clear();
    }

    public void waitForComplete(){
        try {
            countingSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void purgeQueue(){
        tasks.clear();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        this.workerList.forEach(t->{
            t.stopConsume();
            t.terminateWorker();
        });
        List<Runnable> result=new ArrayList<>();
        this.tasks.forEach(t->result.add(t.getTask()));
        this.workerList.clear();
        this.threadList.clear();
        return result;
    }

    @Override
    public boolean isShutdown() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isTerminated() {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new NotImplementedException();
    }

    @Override
    public Future<?> submit(Runnable task) {
        TaskWrapper future=new TaskWrapper(task);
        this.enqueueTask(future);
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new NotImplementedException();
    }

    @Override
    public void execute(Runnable command) {
        TaskWrapper future=new TaskWrapper(command);
        this.enqueueTask(future);
    }
}