package su.grinev.FileUploader.utility;

@FunctionalInterface
public interface WorkerCallback {

    abstract void cancel(TaskWrapper taskWrapper);

}
