package su.grinev.FileUploader.jdbc.dao;

import su.grinev.FileUploader.jdbc.model.FileMetadata;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public interface FileMetadataDao {

    public void setDatasource(DataSource datasource);

    public void createFileMetadata(String fileName, String displayName, Integer hashcode, Long size, Integer state);

    public FileMetadata getFileMetadataById(int id);

    public List<FileMetadata> listFileMetadata();

    public void removeFileMetadata(int id);

    public void updateFileMetadata(String fileName, String displayName, String hashcode, Long size, Integer state, LocalDateTime dateTime, int id);

}
