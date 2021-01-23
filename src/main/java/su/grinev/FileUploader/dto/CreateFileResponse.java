package su.grinev.FileUploader.dto;

import org.springframework.stereotype.Component;

@Component
public class CreateFileResponse {
    private int tempFileId;

    public CreateFileResponse(){}

    public CreateFileResponse(int tempFileId) {
        this.tempFileId = tempFileId;
    }

    public int getFileId() {
        return tempFileId;
    }

    public void setFileId(int tempFileId) {
        this.tempFileId = tempFileId;
    }
}