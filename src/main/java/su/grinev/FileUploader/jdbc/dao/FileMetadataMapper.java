package su.grinev.FileUploader.jdbc.dao;

import org.springframework.jdbc.core.RowMapper;
import su.grinev.FileUploader.jdbc.model.FileMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileMetadataMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new FileMetadata(
                resultSet.getInt("id"),
                resultSet.getString("filename"),
                resultSet.getString("displayname"),
                resultSet.getInt("hashcode"),
                resultSet.getLong("size"),
                resultSet.getInt("state"),
                resultSet.getTimestamp("createdtime").toInstant());
    }
}
