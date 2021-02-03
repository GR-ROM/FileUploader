package su.grinev.FileUploader.dto;

import org.springframework.stereotype.Component;

@Component
public class CreateFileResponse {
    private int fileId;

    public CreateFileResponse(int tempFileId) {
        this.fileId = tempFileId;
    }

    public CreateFileResponse(){

    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}