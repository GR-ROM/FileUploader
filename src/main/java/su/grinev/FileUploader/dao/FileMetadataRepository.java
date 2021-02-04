package su.grinev.FileUploader.dao;

import org.springframework.stereotype.Component;
import su.grinev.FileUploader.jdbc.model.FileMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FileMetadataRepository {

    private final AtomicInteger id;
    private final Map<Integer, FileMetadata> fileMetadata;

    public FileMetadataRepository() {
        id=new AtomicInteger();
        fileMetadata = new ConcurrentHashMap<>();
    }


    public Map<Integer, FileMetadata> getFileMetadata() {
        return fileMetadata;
    }

    public void save(FileMetadata fileMetadata) {
        fileMetadata.setId(id.incrementAndGet());
        this.fileMetadata.put(id.get(), fileMetadata);
    }

    public void update(FileMetadata fileMetadata) {
        this.fileMetadata.get(fileMetadata.getId()).setAll(fileMetadata);
    }

    public FileMetadata findByHash(int hash) {
        return fileMetadata.values().stream().filter(l -> l.getHash() == hash).findFirst().orElse(null);
    }

    public List<FileMetadata> findAll() {
        return new ArrayList<>(fileMetadata.values());
    }

    public FileMetadata findById(int id) {
        return this.fileMetadata.get(id);
    }

}