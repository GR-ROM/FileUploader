package su.grinev.FileUploader.dao;

import org.springframework.stereotype.Component;
import su.grinev.FileUploader.model.FileChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public synchronized List<FileChunk> getChunkListByFileId(int fileId){
        return fileChunkMap
                .values()
                .stream()
                .filter(t -> t.getFileId()==fileId)
                .collect(Collectors.toList());
    }

    public synchronized void save(FileChunk fileChunk){
            id++;
            fileChunk.setChunkId(id);
            this.fileChunkMap.put(id, fileChunk);
    }

    public synchronized FileChunk findByHash(int hash){
            return fileChunkMap
                    .values()
                    .stream()
                    .filter(t -> t.getHashcode()==hash)
                    .findFirst()
                    .orElse(null);
    }

    public synchronized List<FileChunk> findAll(){
        return new ArrayList<>(fileChunkMap.values());
    }

    public synchronized FileChunk findById(int id){
            return this.fileChunkMap.get(id);
    }

}
