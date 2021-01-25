package su.grinev.FileUploader.dao;

import org.springframework.stereotype.Component;
import su.grinev.FileUploader.jdbc.model.FileMetadata;

import java.util.*;

@Component
public class FileMetadataRepository {

    private int id;
    private Map<Integer,FileMetadata> fileMetadata;

    public FileMetadataRepository(){
        id=0;
        fileMetadata = new HashMap<>();
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

    public void save(FileMetadata fileMetadata){
        synchronized (this.fileMetadata){
            id++;
            fileMetadata.setId(id);
            this.fileMetadata.put(id, fileMetadata);
        }
    }

    public void update(FileMetadata fileMetadata){
        synchronized (this.fileMetadata){
            this.fileMetadata.get(fileMetadata.getId()).setAll(fileMetadata);
        }
    }

    public FileMetadata findByHash(int hash){
        synchronized (this.fileMetadata) {
            return fileMetadata.values().stream().filter(l -> l.getHash()==hash).findFirst().orElse(null);
        }
    }

    public List<FileMetadata> findAll(){
        synchronized (this.fileMetadata) {
            return new ArrayList<>(fileMetadata.values());
        }
    }

    public FileMetadata findById(int id){
        synchronized (this.fileMetadata){
            return this.fileMetadata.get(id);
        }
    }

}