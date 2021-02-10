package su.grinev.FileUploader.utility;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable {

    private BlockingQueue<TaskWrapper> taskList;
    private Thread thread;
    private volatile boolean stop;
    private CountingSemaphore countingSemaphore;
    private TaskWrapper currentTask;
    private AtomicBoolean allowConsume;

    public Worker(BlockingQueue<TaskWrapper> taskList, CountingSemaphore countingSemaphore){
        this.taskList=taskList;
        this.countingSemaphore=countingSemaphore;
        this.stop=false;
        this.allowConsume=new AtomicBoolean();
        this.allowConsume.set(true);
    }

    public void stopConsume(){
        this.allowConsume.set(false);
    }

    public void continueConsume(){
        this.allowConsume.set(true);
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public TaskWrapper getCurrentTask() {
        return currentTask;
    }

    public synchronized void terminateWorker(){
        stop=true;
        thread.interrupt();
    }

    @Override
    public void run() {
        while (!stop){
            try {
                if (allowConsume.get()) {
                    currentTask = taskList.take();
                    currentTask.setWorkerThread(this.thread);
                    currentTask.setRunningState();
                    currentTask.getTask().run();
                    currentTask.setDoneState();
                    currentTask.setWorkerThread(null);
                    this.countingSemaphore.countDown();
                } else {
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                currentTask.setCancelledState();
                System.out.println("Interrupted exception");
            }
        }

    }
}
