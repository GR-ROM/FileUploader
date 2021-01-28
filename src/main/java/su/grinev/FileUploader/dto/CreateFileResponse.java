package su.grinev.FileUploader.dto;

import org.springframework.stereotype.Component;

@Component
public class CreateFileResponse {
    private int fileId;

    public CreateFileResponse(){}

    public CreateFileResponse(int tempFileId) {
        this.fileId = tempFileId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int tempFileId) {
        this.fileId = tempFileId;
    }
}