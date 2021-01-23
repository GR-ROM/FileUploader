package su.grinev.FileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.dto.CreateFileResponse;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.dto.JsonResponse;
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
        int id=fileMetadataRepository.save(new FileMetadata(request));
        fileStorageService.createFile(id);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

/*
    @RequestMapping(value = "/files/upload/upload", method = RequestMethod.POST, headers = "content-type!=multipart/form-data")
    @ResponseBody
    public ResponseEntity uploadChunked(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        int fileId=Integer.parseInt(request.getHeader("id"));
        request.getHeader("content-range");//Content-Range:bytes 737280-819199/845769
        request.getHeader("content-length"); //845769
        request.getHeader("content-disposition"); // Content-Disposition:attachment; filename="Screenshot%20from%202012-12-19%2017:28:01.png"
        int bytesUploaded= fileStorageService.fileUpload(fileId, Integer.parseInt(request.getHeader("content-length")), request.getInputStream());
        if (bytesUploaded>0) tempFileList.updateTempFileSize(fileId, bytesUploaded);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/files/upload/resume")
    public ResponseEntity uploadWithResume(
            @RequestPart("chunk")byte[] chunk,
            @RequestPart("fileId")int fileId,
            @RequestPart("length")int length) throws IOException {
        if (tempFileList.getTempFileDescription(fileId)!=null){
            fileStorageService.fileResumeUpload(chunk, fileId, length);
            tempFileList.updateTempFileSize(fileId, length);
        }
        else return new ResponseEntity<JsonResponse>(new JsonResponse("File not found!"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }
*/
}