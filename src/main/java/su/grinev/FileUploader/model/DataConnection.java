package su.grinev.FileUploader.model;

import su.grinev.FileUploader.utility.SocketUploader;

public class DataConnection {

    private short port;
    private int state;
    private FileChunk fileChunk;
    private SocketUploader socketUploader;
    private Thread thread;

    public static final int DATA_CONNECTION_OPENED=0;
    public static final int DATA_CONNECTION_CLOSED=DATA_CONNECTION_OPENED+1;

    public DataConnection(short port, int state) {
        this.port = port;
        this.state = state;
        this.fileChunk=null;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setFileChunk(FileChunk fileChunk){
        this.fileChunk=fileChunk;
    }

    public FileChunk getFileChunk(){
        return this.fileChunk;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public SocketUploader getSocketUploader() {
        return socketUploader;
    }

    public void setSocketUploader(SocketUploader socketUploader) {
        this.socketUploader = socketUploader;
    }
}
