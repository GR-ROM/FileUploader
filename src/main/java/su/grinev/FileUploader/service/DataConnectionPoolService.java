package su.grinev.FileUploader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.grinev.FileUploader.model.DataConnection;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.utility.CustomFixedThreadPool;
import su.grinev.FileUploader.utility.SocketUploader;
import su.grinev.FileUploader.utility.TaskWrapper;
import su.grinev.FileUploader.utility.Watchdog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class DataConnectionPoolService {

    private final String tempFilesDirectory;
    private final Watchdog watchdog;
    private final Thread watchdogThread;
    private final List<DataConnection> dataConnectionList;
    private final int maxConcurentConnections;
    private final int lowPort;
    private final int highPort;
    private final CustomFixedThreadPool customFixedThreadPool;

    private Optional<DataConnection> getDataConnectionByFileChunkId(int chunkId) {
        return dataConnectionList
                .stream()
                .filter(t -> t.getFileChunk().getChunkId() == chunkId)
                .findFirst();
    }

    private Optional<DataConnection> getClosedDataConnectionFromPool() {
        return dataConnectionList
                .stream()
                .filter(t -> t.getState() == DataConnection.DATA_CONNECTION_CLOSED)
                .findFirst();
    }

    @Autowired
    public DataConnectionPoolService(@Value("${MAX_DATA_CONNECTIONS}")
                                             int maxConcurentConnections,
                                     @Value("${DATA_CONNECTIONS_LOW_PORT}")
                                             int lowPort,
                                     @Value("${DATA_CONNECTIONS_HIGH_PORT}")
                                             int highPort,
                                     @Value(value = "${TMP_FILES_DIRECTORY}")
                                             String tempFilesDirectory,
                                     CustomFixedThreadPool customFixedThreadPool) {
        this.customFixedThreadPool = customFixedThreadPool;
        this.maxConcurentConnections = maxConcurentConnections;
        this.lowPort = lowPort;
        this.highPort = highPort;
        this.tempFilesDirectory = tempFilesDirectory;
        dataConnectionList = new ArrayList<>();
        Stream<DataConnection> DataConnectionGenerator = Stream.generate(()
                -> (new DataConnection((short) (new Random().nextInt(highPort - lowPort) + lowPort),
                DataConnection.DATA_CONNECTION_CLOSED)));
        DataConnectionGenerator.limit(maxConcurentConnections).forEach(d -> dataConnectionList.add(d));
        this.watchdog=new Watchdog(dataConnectionList, this);
        this.watchdogThread=new Thread(this.watchdog);
        this.watchdogThread.start();
    }

    public synchronized Optional<DataConnection> openDataConnection(FileChunk fileChunk) {
        Optional<DataConnection> dataConnection = getClosedDataConnectionFromPool();
        if (!dataConnection.isPresent()) return null;
        // fill connection context and start a thread
        dataConnection.get().setFileChunk(fileChunk);
        SocketUploader socketUploader = new SocketUploader(fileChunk, dataConnection.get().getPort(), this);
        socketUploader.setTempFilesDirectory(this.tempFilesDirectory);
        dataConnection.get().setSocketUploader(socketUploader);
        dataConnection.get().setState(DataConnection.DATA_CONNECTION_OPENED);
        dataConnection.get().setTask(new TaskWrapper(dataConnection.get().getSocketUploader()));
        customFixedThreadPool.enqueueTask(dataConnection.get().getTask());
        return dataConnection;
    }

    public synchronized void closeDataConnectionByChunkId(int chunkId, boolean forceStopThread) {
        Optional<DataConnection> dataConnection = getDataConnectionByFileChunkId(chunkId);
        if (!dataConnection.isPresent())
            return;
        this.closeDataConnection(dataConnection.get(), forceStopThread);
    }

    public synchronized void closeDataConnection(DataConnection dataConnection, boolean forceStopThread){
        if (forceStopThread)
            dataConnection.getTask().cancel(true);
        // clear connection context
        dataConnection.setSocketUploader(null);
        dataConnection.setTask(null);
        dataConnection.setFileChunk(null);
        dataConnection.setState(DataConnection.DATA_CONNECTION_CLOSED);
    }
}
