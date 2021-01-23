package su.grinev.FileUploader.dto;

import su.grinev.FileUploader.model.FileChunk;

import java.time.LocalDateTime;
import java.util.List;

public class FileMetadataDto {

    private Long size;
    private String displayName;
    private String fileName;

    public FileMetadataDto() {}

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
