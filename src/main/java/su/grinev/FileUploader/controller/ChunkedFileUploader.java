package su.grinev.FileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import su.grinev.FileUploader.dao.FileChunkRepository;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.jdbc.model.FileMetadata;
import su.grinev.FileUploader.service.FileStorageService;
import su.grinev.FileUploader.service.FileUploadServiceImpl;

import java.io.IOException;

@RestController
public class ChunkedFileUploader {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    @Autowired
    private FileUploadServiceImpl fileUploadService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FileChunkRepository fileChunkRepository;

    public ChunkedFileUploader(){}

    public ChunkedFileUploader(FileMetadataRepository fileMetadataRepository,
                               FileUploadServiceImpl fileUploadService,
                               FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
        this.fileMetadataRepository=fileMetadataRepository;
        this.fileUploadService=fileUploadService;
    }

    @RequestMapping(path="/files/upload/create", consumes = "application/JSON", produces = "application/JSON",
            method = RequestMethod.POST)
    public ResponseEntity createFile(@RequestBody FileMetadataDto request){
        if (request.getDisplayName()==null ||
            request.getFileName()==null ||
            request.getSize()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        FileMetadata fileMetadata=new FileMetadata(request);
        fileMetadataRepository.save(fileMetadata);
        fileStorageService.createFile(fileMetadata.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/files/upload/", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postFileUpload(@RequestPart(value = "fileId") String fileId,
                                            @RequestPart(value = "hashcode", required = false) String hashCode,
                                            @RequestPart(value = "file", required = true)
                                                    MultipartFile file) throws IOException {
        if (fileId==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        int ifileId=Integer.parseInt(fileId);
        if (fileMetadataRepository.findById(ifileId)==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        fileStorageService.fileUpload(ifileId, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/files/upload/", method=RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity putFileUpload(@RequestPart(value = "fileId") String fileId,
                                     @RequestPart(value = "hashcode", required = false) String hashCode,
                                     @RequestPart(value = "file", required = true)
                                             MultipartFile file) throws IOException {
        if (fileId==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        int ifileId=Integer.parseInt(fileId);
        if (fileMetadataRepository.findById(ifileId)==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        fileStorageService.fileUpload(ifileId, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}