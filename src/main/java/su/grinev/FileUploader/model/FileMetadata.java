package su.grinev.FileUploader.model;

import su.grinev.FileUploader.dto.FileMetadataDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileMetadata {

    private Integer id;
    private String hash;
    private String fileName;
    private Long size;
    private Long actualSize;
    private String displayName;
    private boolean uploaded;
    private LocalDateTime createdTime;
    private List<FileChunk> fileChunks;

    public FileMetadata(FileMetadataDto fileMetadataDto){
        this.displayName=fileMetadataDto.getDisplayName();
        this.fileName=fileMetadataDto.getFileName();
        this.size=fileMetadataDto.getSize();
        this.hash=null;
        this.actualSize=0l;
        this.uploaded=false;
        this.createdTime=LocalDateTime.now();
        this.fileChunks=new ArrayList<>();
    }

    public FileMetadata(Integer id, String hash, String displayName, Long size, String fileName) {
        this.id = id;
        this.hash = hash;
        this.displayName = displayName;
        this.size = size;
        this.fileName = fileName;
    }

    public void setAll(FileMetadata fileMetadata){
        this.id=fileMetadata.id;
        this.hash=fileMetadata.hash;
        this.displayName=fileMetadata.displayName;
        this.size=fileMetadata.size;
        this.fileName=fileMetadata.fileName;
        this.actualSize=fileMetadata.actualSize;
        this.createdTime=fileMetadata.createdTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getActualSize() {
        return actualSize;
    }

    public void setActualSize(Long actualSize) {
        this.actualSize = actualSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
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