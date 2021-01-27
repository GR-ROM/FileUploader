package su.grinev.FileUploader.jdbc.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import su.grinev.FileUploader.jdbc.model.FileMetadata;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JdbcTemplateFileMetadataDaoImpl implements FileMetadataDao{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDatasource(DataSource datasource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createFileMetadata(String fileName, String displayName, Integer hashcode, Long size, Integer state) {
        String SQL = "INSERT INTO filemetadata (filename, displayname, hashcode, size, state, datetime) VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(SQL, fileName, displayName, hashcode, size, state, Instant.now());
        System.out.println("FileMetadata successfully created.\nfilename: " + fileName + ";\ndisplayname: " +
                displayName + ";\nhashcode: " + hashcode + "\nsize: "+size+";\nstate: "+state+"");
    }

    @Override
    public FileMetadata getFileMetadataById(int id) {
        String SQL="SELECT * FROM filemetadata WHERE id=?";

        FileMetadata fileMetadata= (FileMetadata) jdbcTemplate.query(SQL, new FileMetadataMapper(), id);
        return fileMetadata;
    }

    @Override
    public List listFileMetadata() {
        String SQL="SELECT * FROM filemetadata";
        List fileMetadataList=jdbcTemplate.queryForList(SQL, new FileMetadataMapper());
        return null;
    }

    @Override
    public void removeFileMetadata(int id) {
        String SQL = "DELETE FROM filemetadata WHERE id = ?";
        jdbcTemplate.update(SQL, id);
        System.out.println("FileMetadata with id: " + id + " successfully removed");
    }

    @Override
    public void updateFileMetadata(String fileName, String displayName, String hashcode, Long size, Integer state, LocalDateTime dateTime, int id) {
        String SQL = "UPDATE filemetadata SET (filename, displayname, hashcode, size, state, datetime) VALUES (?,?,?,?,?,?) WHERE id=?";

        jdbcTemplate.update(SQL, fileName, displayName, hashcode, size, state, dateTime, id);
        System.out.println("FileMetadata successfully created.\nfilename: " + fileName + ";\ndisplayname: " +
                displayName + ";\nhashcode: " + hashcode + "\nsize: "+size+";\nstate: "+state+"");
    }
}
