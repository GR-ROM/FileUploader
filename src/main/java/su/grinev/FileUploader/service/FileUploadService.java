package su.grinev.FileUploader.service;

import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.model.FileMetadata;

public interface FileUploadService {
    void saveFile(FileMetadata fileMetadata);

    void updateFile(FileMetadata fileMetadata);

    FileMetadata findByHash(String hash);

    FileMetadata findById(int id);

    void saveChunk(FileChunk fileChunk);

}
