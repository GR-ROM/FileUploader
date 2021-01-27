package su.grinev.FileUploader.model;

import su.grinev.FileUploader.dto.FileChunkDto;

import java.util.Objects;

public class FileChunk{
    private Integer chunkId;
    private Integer fileId;
    private Integer hashcode;
    private Long offset;
    private Integer size;
    private boolean validHashcode;

    public FileChunk(Integer fileId, Integer hashcode, Long offset, Integer size) {
        this.fileId = fileId;
        this.hashcode = hashcode;
        this.offset = offset;
        this.size = size;
        this.validHashcode=false;
        this.chunkId=null;
    }

    public FileChunk(FileChunkDto fileChunkDto){
        this.fileId=fileChunkDto.getFileId();
        this.hashcode=fileChunkDto.getHashcode();
        this.offset=fileChunkDto.getOffset();
        this.size=fileChunkDto.getSize();
        this.validHashcode=false;
        this.chunkId=null;
    }

    public Integer getChunkId() {
        return chunkId;
    }

    public void setChunkId(Integer chunkId) {
        this.chunkId = chunkId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getHashcode() {
        return hashcode;
    }

    public void setHashcode(Integer hashcode) {
        this.hashcode = hashcode;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isValidHashcode() {
        return validHashcode;
    }

    public void setValidHashcode(boolean validHashcode) {
        this.validHashcode = validHashcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileChunk fileChunk = (FileChunk) o;
        return validHashcode == fileChunk.validHashcode &&
                chunkId.equals(fileChunk.chunkId) &&
                fileId.equals(fileChunk.fileId) &&
                Objects.equals(hashcode, fileChunk.hashcode) &&
                offset.equals(fileChunk.offset) &&
                size.equals(fileChunk.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkId, fileId, hashcode, offset, size, validHashcode);
    }
}