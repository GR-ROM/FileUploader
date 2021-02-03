package su.grinev.FileUploader.dto;

public class DataConnectionProperties {

    private int chunkId;

    public DataConnectionProperties(int chunkId) {
        this.chunkId = chunkId;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }
}
