package su.grinev.FileUploader.jdbc.model;

import io.swagger.models.auth.In;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.model.FileChunk;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileMetadata {

    private Integer id;
    private String fileName;
    private String displayName;
    private Long size;
    private Integer hashcode;
    private Integer state;
    private LocalDateTime createdTime;
    private List<FileChunk> fileChunks;

    public FileMetadata(FileMetadataDto fileMetadataDto){
        this.displayName=fileMetadataDto.getDisplayName();
        this.fileName=fileMetadataDto.getFileName();
        this.size=fileMetadataDto.getSize();
        this.hashcode=null;
        this.state=0;
        this.createdTime=LocalDateTime.now();
        this.fileChunks=new ArrayList<>();
    }

    public FileMetadata(Integer id, String fileName, String displayName, Integer hashcode, Long size, Integer state) {
        this.id=id;
        this.hashcode = hashcode;
        this.displayName = displayName;
        this.size = size;
        this.state=state;
        this.fileName = fileName;
    }

    public void setAll(FileMetadata fileMetadata){
        this.id=fileMetadata.id;
        this.hashcode=fileMetadata.hashcode;
        this.displayName=fileMetadata.displayName;
        this.size=fileMetadata.size;
        this.fileName=fileMetadata.fileName;
        this.state=0;
        this.createdTime=fileMetadata.createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHash() {
        return hashcode;
    }

    public void setHash(Integer hash) {
        this.hashcode = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public List<FileChunk> getFileChunks() {
        return fileChunks;
    }

    public void setFileChunks(List<FileChunk> fileChunks) {
        this.fileChunks = fileChunks;
    }
}