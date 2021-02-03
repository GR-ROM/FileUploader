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
    private int activeThreads;

    public CustomThreadPool(
            @Value("4")
            int threads) {
        if (threads==0) throw new IllegalArgumentException();
        this.activeThreads=0;
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
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startExecutor(){
        threadList.forEach(t->{
            t.start();
            activeThreads++;
            countingSemaphore.countUp();
        });
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