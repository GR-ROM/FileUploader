package su.grinev.FileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.grinev.FileUploader.SpringJdbcConfig;
import su.grinev.FileUploader.dao.FileChunkRepository;
import su.grinev.FileUploader.dao.FileMetadataRepository;
import su.grinev.FileUploader.dto.DataConnectionProperties;
import su.grinev.FileUploader.dto.FileChunkDto;
import su.grinev.FileUploader.dto.FileMetadataDto;
import su.grinev.FileUploader.jdbc.dao.JdbcTemplateFileMetadataDaoImpl;
import su.grinev.FileUploader.jdbc.model.FileMetadata;
import su.grinev.FileUploader.model.DataConnection;
import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.service.DataConnectionPoolService;
import su.grinev.FileUploader.service.FileStorageService;

import java.io.IOException;

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
                               FileChunkRepository fileChunkRepository, FileMetadataRepository fileMetadataRepository) {
        this.springJdbcConfig=springJdbcConfig;
        this.fileStorageService = fileStorageService;
        this.dataConnectionPoolService=dataConnectionPoolService;
        this.fileChunkRepository=fileChunkRepository;
        this.jdbcTemplateFileMetadataDao = jdbcTemplateFileMetadataDao;
        this.fileMetadataRepository = fileMetadataRepository;
        this.jdbcTemplateFileMetadataDao.setDatasource(springJdbcConfig.mysqlDataSource());
    }

    @RequestMapping(path="/files/upload/create", consumes = "application/JSON", produces = "application/JSON",
            method = RequestMethod.POST)
    public ResponseEntity createFile(@RequestBody FileMetadataDto request){
        if (request.getDisplayName()==null) request.setDisplayName(request.getFileName());
        if (request.getFileName()==null || request.getSize()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        FileMetadata fileMetadata=new FileMetadata(request);
        fileMetadataRepository.save(fileMetadata);
        fileStorageService.createFile(fileMetadata.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/files/upload/dataconnection/open", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> openDataConnection(@RequestBody FileChunkDto request) throws IOException {
        if (request.getFileId()==null || request.getOffset()==null || request.getSize()==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // TO DO: add chunk intersection check
        DataConnection dataConnection;
        FileChunk fileChunk=new FileChunk(request);
        fileChunkRepository.save(fileChunk);
        if ((dataConnection=dataConnectionPoolService.openDataConnection(fileChunk))==null){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
        return new ResponseEntity<DataConnectionProperties>(new DataConnectionProperties(dataConnection.getPort()), HttpStatus.CREATED);
    }

    @RequestMapping(value="/files/upload/dataconnection/reset", method=RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> updateChunk(@RequestBody DataConnectionProperties request) throws IOException {
        dataConnectionPoolService.resetDataConnection(request.getDataPort());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/files/upload/chunk/verify", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> commitChunk(@RequestBody FileChunkDto request) throws IOException {
        // TO DO: verify uploaded chunk CRC32!
        fileChunkRepository.save(new FileChunk(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/files/upload/dataconnection/close", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConnectionProperties> closeDataConnection(@RequestBody DataConnectionProperties request) throws IOException {
        dataConnectionPoolService.closeDataConnectionByPort(request.getDataPort());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}