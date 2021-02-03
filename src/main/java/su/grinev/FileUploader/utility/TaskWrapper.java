package su.grinev.FileUploader.utility;

public class TaskWrapper {

    private Runnable task;
    private Worker worker;

    public TaskWrapper(Runnable task) {
        this.task = task;
        this.worker=null;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Runnable getTask() {
        return task;
    }


    public void terminate(){
        if (worker!=null) worker.terminate(this);
    }
}
