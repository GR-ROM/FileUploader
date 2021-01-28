package su.grinev.FileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.grinev.FileUploader.SpringJdbcConfig;
import su.grinev.FileUploader.dao.FileChunkRepository;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.dto.CreateFileResponse;
import su.grinev.FileUploader.dto.DataConnectionProperties;
import su.grinev.FileUploader.dto.FileChunkDto;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.jdbc.dao.JdbcTemplateFileMetadataDaoImpl;
import su.grinev.FileUploader.jdbc.model.FileMetadata;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.service.DataConnectionPoolService;
import su.grinev.FileUploader.service.FileStorageService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChunkedFileUploader {

    private final FileStorageService fileStorageService;
    private final SpringJdbcConfig springJdbcConfig;
    private final JdbcTemplateFileMetadataDaoImpl jdbcTemplateFileMetadataDao;
    private final DataConnectionPoolService dataConnectionPoolService;
    private final FileChunkRepository fileChunkRepository;
    private final FileMetadataRepository fileMetadataRepository;

    @Autowired
    public ChunkedFileUploader(JdbcTemplateFileMetadataDaoImpl jdbcTemplateFileMetadataDao,
                               FileStorageService fileStorageService,
                               SpringJdbcConfig springJdbcConfig,
                               DataConnectionPoolService dataConnectionPoolService,
                               FileChunkRepository fileChunkRepository,
                               FileMetadataRepository fileMetadataRepository) {
        this.springJdbcConfig=springJdbcConfig;
        this.fileStorageService = fileStorageService;
        this.dataConnectionPoolService=dataConnectionPoolService;
        this.fileChunkRepository=fileChunkRepository;
        this.jdbcTemplateFileMetadataDao = jdbcTemplateFileMetadataDao;
        this.fileMetadataRepository = fileMetadataRepository;
        this.jdbcTemplateFileMetadataDao.setDatasource(springJdbcConfig.mysqlDataSource());
    }

    private boolean isValidFileChunkDto(FileChunkDto fileChunkDto){
        if (fileChunkDto.getFileId()==null || fileChunkDto.getOffset()==null
                        || fileChunkDto.getSize()==null) return false;
        return fileChunkDto.getOffset() >= 0 && fileChunkDto.getSize() > 0;
    }

    private boolean isExistingFileId(int fileId){
        return fileMetadataRepository.findById(fileId) != null;
    }

    private boolean isExistingChunkId(int chunkId){
        return fileChunkRepository.findById(chunkId)!= null;
    }

    @RequestMapping(path="/files/upload/create", consumes = "application/JSON", produces = "application/JSON",
                    method = RequestMethod.POST)
    public ResponseEntity<CreateFileResponse> createFile(@RequestBody FileMetadataDto request){
        if (request.getDisplayName()==null) request.setDisplayName(request.getFileName());
        if (request.getFileName()==null || request.getSize()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        FileMetadata fileMetadata=new FileMetadata(request);
        fileMetadataRepository.save(fileMetadata);
        fileStorageService.createFile(fileMetadata.getId());
        return new ResponseEntity<>(new CreateFileResponse(fileMetadata.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path="/files/upload/listchunks", consumes = "application/JSON", produces = "application/JSON",
            method = RequestMethod.GET)
    public ResponseEntity<List<FileChunkDto>> listChunks(@RequestParam Integer fileId){
        if (fileMetadataRepository.findById(fileId)==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<FileChunkDto> fileChunkDtoList=new ArrayList<>();
        fileChunkRepository.getChunkListByFileId(fileId)
                .stream()
                .forEach(t->fileChunkDtoList.add(new FileChunkDto(t)));
        return new ResponseEntity<>(fileChunkDtoList, HttpStatus.OK);
    }

    @RequestMapping(value="/files/upload/dataconnection/open",
                    method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> openDataConnection(@RequestBody FileChunkDto request){
        if (!isValidFileChunkDto(request)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!isExistingFileId(request.getFileId())) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // TO DO: add chunk intersection check
        FileChunk fileChunk=new FileChunk(request);
        fileChunkRepository.save(fileChunk);
        if (dataConnectionPoolService.openDataConnection(fileChunk)==null)
                    return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        return new ResponseEntity<>(new DataConnectionProperties(fileChunk.getChunkId()), HttpStatus.CREATED);
    }

    @RequestMapping(value="/files/upload/dataconnection/reset",
                    method=RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> updateChunk(@RequestParam Integer chunkId){
        if (!isExistingChunkId(chunkId)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        dataConnectionPoolService.resetDataConnection(chunkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/files/upload/chunk/verify",
                    method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> commitChunk(@RequestParam Integer chunkId){
        if (!isExistingChunkId(chunkId)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // TO DO: verify uploaded chunk data using CRC32/SHA-1/MD-5/etc...
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/files/upload/dataconnection/close",
                    method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> closeDataConnection(@RequestParam Integer chunkId){
        if (!isExistingChunkId(chunkId)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        dataConnectionPoolService.closeDataConnectionByPort(chunkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}