package su.grinev.FileUploader.jdbc.dao;

import su.grinev.FileUploader.jdbc.model.FileMetadata;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public interface FileMetadataDao {

    void setDatasource(DataSource datasource);

    void createFileMetadata(String fileName, String displayName, Integer hashcode, Long size, Integer state);

    FileMetadata getFileMetadataById(int id);

    List<FileMetadata> listFileMetadata();

    void removeFileMetadata(int id);

    void updateFileMetadata(String fileName, String displayName, String hashcode, Long size, Integer state, LocalDateTime dateTime, int id);

}
