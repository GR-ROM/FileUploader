package su.grinev.FileUploader.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CustomThreadPool  {

    private final BlockingQueue<TaskWrapper> tasks;
    private final ArrayList<Thread> threadList;
    private final ArrayList<Worker> workerList;
    private final CountingSemaphore countingSemaphore;

    public CustomThreadPool(
            @Value("2") // TO DO: AUTO - get actual number of physical CPU cores!
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

    public void enqueueTask(TaskWrapper task){
        try {
            countingSemaphore.countUp();
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
        purgeQueue();
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

}