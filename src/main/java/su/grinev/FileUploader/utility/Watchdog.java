package su.grinev.FileUploader.utility;

import su.grinev.FileUploader.model.DataConnection;
import su.grinev.FileUploader.service.DataConnectionPoolService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Watchdog implements Runnable {

    private final List<DataConnection> dataConnectionList;
    private final DataConnectionPoolService dataConnectionPoolService;

    public Watchdog(List<DataConnection> dataConnectionList, DataConnectionPoolService dataConnectionPoolService) {
        this.dataConnectionList = dataConnectionList;
        this.dataConnectionPoolService = dataConnectionPoolService;
    }

    @Override
    public void run() {
        Map<DataConnection, Integer> timeouts = new HashMap<>();
        dataConnectionList.forEach(t->timeouts.put(t, 0));
        while (true) {
            for(Map.Entry<DataConnection, Integer> entry: timeouts.entrySet()){
                if (entry.getKey().getState()==DataConnection.DATA_CONNECTION_OPENED &&
                !entry.getKey().getSocketUploader().isUploading()){
                    entry.setValue(entry.getValue()+1);
                    if (entry.getValue()==60) {
                        dataConnectionPoolService.closeDataConnection(entry.getKey(), true);
                    }
                } else {
                    entry.setValue(0);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}