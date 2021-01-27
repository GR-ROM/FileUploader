package su.grinev.FileUploader.jdbc.model;

import io.swagger.models.auth.In;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.model.FileChunk;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileMetadata {

    private Integer id;
    private String fileName;
    private String displayName;
    private Long fileSize;
    private Integer hashcode;
    private Integer state;
    private Instant createdTime;
    private List<String> tags;

    public static enum FileState{
        FILE_EMPTY,
        FILE_UPLOADED,
        FILE_CONVERTED
    }

    public FileMetadata(FileMetadataDto fileMetadataDto){
        this.displayName=fileMetadataDto.getDisplayName();
        this.fileName=fileMetadataDto.getFileName();
        this.fileSize=fileMetadataDto.getSize();
        this.hashcode=null;
        this.state=0;
        this.createdTime=null;
    }

    public FileMetadata(Integer id, String fileName, String displayName, Integer hashcode, Long size, Integer state, Instant createdTime) {
        this.id=id;
        this.hashcode = hashcode;
        this.displayName = displayName;
        this.fileSize = size;
        this.state=state;
        this.fileName = fileName;
        this.createdTime=createdTime;
    }

    public void setAll(FileMetadata fileMetadata){
        this.id=fileMetadata.id;
        this.hashcode=fileMetadata.hashcode;
        this.displayName=fileMetadata.displayName;
        this.fileSize=fileMetadata.fileSize;
        this.fileName=fileMetadata.fileName;
        this.state=0;
        this.createdTime=fileMetadata.createdTime;
    }

    public Integer getHashcode() {
        return hashcode;
    }

    public void setHashcode(Integer hashcode) {
        this.hashcode = hashcode;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
        return fileSize;
    }

    public void setSize(Long size) {
        this.fileSize = size;
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

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

}