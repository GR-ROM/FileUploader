package su.grinev.FileUploader.dao;

import org.springframework.stereotype.Component;
import su.grinev.FileUploader.model.FileChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class FileChunkRepository {

    private final AtomicInteger id;
    private final Map<Integer, FileChunk> fileChunkMap;

    public FileChunkRepository(){
        id=new AtomicInteger();
        fileChunkMap=new ConcurrentHashMap<>();
    }

    public Map<Integer, FileChunk> getFileChunkMap() {
        return fileChunkMap;
    }

    public List<FileChunk> getChunkListByFileId(int fileId){
        return fileChunkMap
                .values()
                .stream()
                .filter(t -> t.getFileId()==fileId)
                .collect(Collectors.toList());
    }

    public void save(FileChunk fileChunk){
            fileChunk.setChunkId(id.incrementAndGet());
            this.fileChunkMap.put(id.get(), fileChunk);
    }

    public List<FileChunk> findAll(){
        return new ArrayList<>(fileChunkMap.values());
    }

    public FileChunk findById(int id){
            return this.fileChunkMap.get(id);
    }

}
