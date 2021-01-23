package su.grinev.FileUploader.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.model.FileMetadata;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    public FileUploadServiceImpl(){}

    @Override
    public void saveFile(FileMetadata fileMetadata) {
        this.fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public void updateFile(FileMetadata fileMetadata) {
        this.fileMetadataRepository.update(fileMetadata);
    }

    @Override
    public FileMetadata findByHash(String hash) {
        return this.fileMetadataRepository.findByHash(hash);
    }

    @Override
    public FileMetadata findById(int Id) {
        return this.fileMetadataRepository.findById(Id);
    }

    @Override
    public void saveChunk(FileChunk fileChunk) {

    }

}