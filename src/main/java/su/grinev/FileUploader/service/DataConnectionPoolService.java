package su.grinev.FileUploader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.grinev.FileUploader.model.DataConnection;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.utility.SocketUploader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class DataConnectionPoolService {

    private final List<DataConnection> dataConnectionList;
    private final int maxConcurentConnections;
    private final int lowPort;
    private final int highPort;

    private DataConnection getDataConnectionByFileChunkId(int chunkId){
        return dataConnectionList
                .stream()
                .filter(t -> t.getFileChunk().getChunkId()==chunkId)
                .findFirst()
                .orElse(null);
    }

    private DataConnection getClosedDataConnectionFromPool(){
        return dataConnectionList
                .stream()
                .filter(t -> t.getState()==DataConnection.DATA_CONNECTION_CLOSED)
                .findFirst()
                .orElse(null);
    }

    @Autowired
    public DataConnectionPoolService(@Value("${MAX_DATA_CONNECTIONS}")
                                             int maxConcurentConnections,
                                     @Value("${DATA_CONNECTIONS_LOW_PORT}")
                                             int lowPort,
                                     @Value("${DATA_CONNECTIONS_HIGH_PORT}")
                                             int highPort){
        this.maxConcurentConnections=maxConcurentConnections;
        this.lowPort = lowPort;
        this.highPort = highPort;
        dataConnectionList=new ArrayList<>();
        Stream<DataConnection> DataConnectionGenerator = Stream.generate(()
                        ->  (new DataConnection((short)(new Random().nextInt(highPort-lowPort)+lowPort),
                        DataConnection.DATA_CONNECTION_CLOSED)));
        DataConnectionGenerator.limit(maxConcurentConnections).forEach(d -> dataConnectionList.add(d));
    }

    public void resetDataConnection(int chunkId){
        DataConnection dataConnection=getDataConnectionByFileChunkId(chunkId);
        if (dataConnection==null) return;
        dataConnection.getSocketUploader().resetFilePointer();
    }

    public DataConnection openDataConnection(FileChunk fileChunk){
        DataConnection dataConnection=getClosedDataConnectionFromPool();
        if (dataConnection==null) return null;
        // fill connection context and start a thread
        dataConnection.setFileChunk(fileChunk);
        dataConnection.setSocketUploader(new SocketUploader(fileChunk, dataConnection.getPort()));
        dataConnection.setThread(new Thread(dataConnection.getSocketUploader()));
        dataConnection.setState(DataConnection.DATA_CONNECTION_OPENED);
        dataConnection.getThread().start();
        return dataConnection;
    }

    public void closeDataConnectionByPort(int chunkId){
        DataConnection dataConnection=getDataConnectionByFileChunkId(chunkId);
        if (dataConnection==null) return;
        dataConnection.getSocketUploader().setStop();
        try {
            dataConnection.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // clear connection context
        dataConnection.setSocketUploader(null);
        dataConnection.setThread(null);
        dataConnection.setFileChunk(null);
        dataConnection.setState(DataConnection.DATA_CONNECTION_CLOSED);
    }
}
