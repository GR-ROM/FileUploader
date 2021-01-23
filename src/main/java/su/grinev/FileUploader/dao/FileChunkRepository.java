package su.grinev.FileUploader.dao;

import org.springframework.beans.factory.annotation.Autowired;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.model.FileMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileChunkRepository {

    private int id;
    @Autowired
    private Map<Integer, FileChunk> fileChunkMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, FileChunk> getFileChunkMap() {
        return fileChunkMap;
    }

    public void setFileChunkMap(Map<Integer, FileChunk> fileChunkMap) {
        this.fileChunkMap = fileChunkMap;
    }

    public void save(FileChunk fileChunk){
        synchronized (this.fileChunkMap){
            id++;
            fileChunk.setChunkId(id);
            this.fileChunkMap.put(id, fileChunk);
        }
    }

    public FileChunk findByHash(int hash){
        synchronized (this.fileChunkMap) {
            return fileChunkMap.values().stream().filter(t -> t.getHashcode()==hash).findFirst().orElse(null);
        }
    }

    public List<FileChunk> findAll(){
        synchronized (this.fileChunkMap) {
            return new ArrayList<>(fileChunkMap.values());
        }
    }

    public FileChunk findById(int id){
        synchronized (this.fileChunkMap){
            return this.fileChunkMap.get(id);
        }
    }
}
