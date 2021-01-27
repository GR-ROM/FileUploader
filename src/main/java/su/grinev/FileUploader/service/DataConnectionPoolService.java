package su.grinev.FileUploader.service;

import org.springframework.stereotype.Service;
import su.grinev.FileUploader.model.DataConnection;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.utility.SocketUploader;

import java.util.Arrays;
import java.util.List;

@Service
public class DataConnectionPoolService {

    private final List<DataConnection> dataConnectionList;

    public DataConnectionPoolService(){
        dataConnectionList=Arrays.asList(
                new DataConnection((short) 1000, DataConnection.DATA_CONNECTION_CLOSED),
                new DataConnection((short) 2000, DataConnection.DATA_CONNECTION_CLOSED),
                new DataConnection((short) 3000, DataConnection.DATA_CONNECTION_CLOSED),
                new DataConnection((short) 4000, DataConnection.DATA_CONNECTION_CLOSED)
        );
    }

    public void resetDataConnection(short port){
        DataConnection dataConnection=dataConnectionList.stream().filter(t -> t.getPort()==port)
                .findFirst().orElse(null);
        if (dataConnection==null) return;
        dataConnection.getSocketUploader().resetFilePointer();
    }

    public DataConnection openDataConnection(FileChunk fileChunk){
        DataConnection dataConnection=dataConnectionList.stream().filter(t -> t.getState()==DataConnection.DATA_CONNECTION_CLOSED)
                .findFirst().orElse(null);
        if (dataConnection==null) return null;
        // fill connection context and start a thread
        dataConnection.setFileChunk(fileChunk);
        dataConnection.setSocketUploader(new SocketUploader(fileChunk, dataConnection.getPort()));
        dataConnection.setThread(new Thread(dataConnection.getSocketUploader()));
        dataConnection.setState(DataConnection.DATA_CONNECTION_OPENED);
        dataConnection.getThread().start();
        return dataConnection;
    }

    public void closeDataConnectionByPort(int port){
        DataConnection dataConnection=dataConnectionList.stream().filter(t -> t.getPort()==port)
                .findFirst().orElse(null);
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
