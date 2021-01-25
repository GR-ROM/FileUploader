package su.grinev.FileUploader.dao;

import org.springframework.stereotype.Component;
import su.grinev.FileUploader.model.FileChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileChunkRepository {

    private int id;
    private Map<Integer, FileChunk> fileChunkMap;

    public FileChunkRepository(){
        id=0;
        fileChunkMap=new HashMap<>();
    }

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
