package su.grinev.FileUploader.dto;

public class FileChunkDto {
    private Integer fileId;
    private Integer hashcode;
    private Long offset;
    private Integer size;

    public FileChunkDto() {
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
}
