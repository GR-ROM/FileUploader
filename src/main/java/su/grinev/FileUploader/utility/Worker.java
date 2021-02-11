package su.grinev.FileUploader.utility;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable {

    private final BlockingQueue<TaskWrapper> taskList;
    private Thread thread;
    private volatile boolean stop;
    private final CountingSemaphore countingSemaphore;
    private TaskWrapper currentTask;
    private final AtomicBoolean allowConsume;

    public Worker(BlockingQueue<TaskWrapper> taskList,
                  CountingSemaphore countingSemaphore) {
        this.taskList = taskList;
        this.countingSemaphore = countingSemaphore;
        this.stop = false;
        this.currentTask=null;
        this.allowConsume = new AtomicBoolean();
        this.allowConsume.set(true);
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void start() {
        this.thread.start();
    }

    public void stopConsume() {
        this.allowConsume.set(false);
    }

    public void continueConsume() {
        this.allowConsume.set(true);
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
                    this.currentTask = taskList.take();
                    this.currentTask.setWorkerThread(this.thread);
                    this.currentTask.setRunningState();
                    this.currentTask.getTask().run();
                    this.currentTask.setWorkerThread(null);
                    this.currentTask.setDoneState();
                    this.currentTask=null;
                    this.countingSemaphore.countDown();
                } else {
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                if (this.currentTask!=null) this.currentTask.setCancelledState();
                System.out.println("Interrupted exception");
            }
        }

    }
}
