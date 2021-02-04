package su.grinev.FileUploader.dto;

import su.grinev.FileUploader.model.FileChunk;

public class FileChunkDto {
    private Integer fileId;
    private Integer hashcode;
    private Long offset;
    private Integer size;

    public FileChunkDto(){

    }

    public FileChunkDto(FileChunk fileChunk) {
        this.size=fileChunk.getSize();
        this.offset=fileChunk.getOffset();
        this.fileId=fileChunk.getFileId();
        this.hashcode=fileChunk.getHashcode();
    }

    public Integer getFileId() {
        return fileId;
    }

    public Integer getHashcode() {
        return hashcode;
    }

    public Long getOffset() {
        return offset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public void setHashcode(Integer hashcode) {
        this.hashcode = hashcode;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
