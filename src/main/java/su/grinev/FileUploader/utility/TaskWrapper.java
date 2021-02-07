package su.grinev.FileUploader.utility;

import java.util.function.Consumer;

public class TaskWrapper {

    private Runnable task;
    private WorkerCallback workerCallback;

    public TaskWrapper(Runnable task) {
        this.task = task;
        this.workerCallback=null;
    }

    public void setWorkerCallback(WorkerCallback workerCallback){
        this.workerCallback=workerCallback;
    }

    public Runnable getTask() {
        return task;
    }

    public void cancel(){
        workerCallback.cancel(this);
    }
}
