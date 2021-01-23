package su.grinev.FileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import su.grinev.FileUploader.dao.FileChunkRepository;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.dto.CreateFileResponse;
import su.grinev.FileUploader.dto.FileChunkDto;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.dto.JsonResponse;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.model.FileMetadata;
import su.grinev.FileUploader.service.FileStorageService;
import su.grinev.FileUploader.service.FileUploadServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value="/files/upload/chunked", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity chunkedFileUpload(@RequestBody FileChunkDto request,
                                     MultipartFile file) throws IOException {
        if (request.getFileId()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (fileMetadataRepository.findById(request.getFileId())==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        fileChunkRepository.save(new FileChunk(request));
        // to do: put chunk to file
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}