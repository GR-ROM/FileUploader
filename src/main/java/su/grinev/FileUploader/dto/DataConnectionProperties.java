package su.grinev.FileUploader.dto;

public class DataConnectionProperties {

    private short dataPort;

    public DataConnectionProperties(short dataPort) {
        this.dataPort = dataPort;
    }

    public short getDataPort() {
        return dataPort;
    }

    public void setDataPort(short dataPort) {
        this.dataPort = dataPort;
    }
}
