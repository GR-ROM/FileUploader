package su.grinev.FileUploader.utility;

import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {

    private BlockingQueue<TaskWrapper> taskList;
    private Thread thread;
    private volatile boolean stop;
    private CountingSemaphore countingSemaphore;
    private TaskWrapper currentTask;

    public Worker(BlockingQueue<TaskWrapper> taskList, CountingSemaphore countingSemaphore){
        this.taskList=taskList;
        this.countingSemaphore=countingSemaphore;
        this.stop=false;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public synchronized void terminateWorker(){
        stop=true;
        thread.interrupt();
    }

    public synchronized void terminateTask(TaskWrapper taskWrapper){
        if (taskList.contains(taskWrapper)){
            taskList.remove(taskWrapper);
        } else {
            if (taskWrapper.equals(currentTask)) this.thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (!stop){
            try {
                currentTask=taskList.take();
                currentTask.setWorker(this);
                currentTask.getTask().run();
                this.countingSemaphore.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
