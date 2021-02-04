package su.grinev.FileUploader.dto;

public class DataConnectionProperties {

    private int chunkId;
    private int dataPort;

    public DataConnectionProperties(int chunkId, int dataPort) {
        this.chunkId = chunkId;
        this.dataPort = dataPort;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public int getDataPort() {
        return dataPort;
    }

    public void setDataPort(int dataPort) {
        this.dataPort = dataPort;
    }
}
