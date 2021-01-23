package su.grinev.FileUploader.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.model.FileMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileMetadataRepository {

    private int id;
    @Autowired
    private Map<Integer,FileMetadata> fileMetadata;

    public FileMetadataRepository(){
        id=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, FileMetadata> getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(Map<Integer, FileMetadata> fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public int save(FileMetadata fileMetadata){
        synchronized (this.fileMetadata){
            this.fileMetadata.put(++id, fileMetadata);
        }
        return id;
    }

    public void update(FileMetadata fileMetadata){
        synchronized (this.fileMetadata){
            this.fileMetadata.get(fileMetadata.getId()).setAll(fileMetadata);
        }
    }

    public FileMetadata findByHash(String hash){
        synchronized (this.fileMetadata) {
            return fileMetadata.values().stream().filter(t -> t.getHash().equalsIgnoreCase(hash)).findFirst().orElse(null);
        }
    }

    public FileMetadata findById(int id){
        synchronized (this.fileMetadata){
            return this.fileMetadata.get(id);
        }
    }

}