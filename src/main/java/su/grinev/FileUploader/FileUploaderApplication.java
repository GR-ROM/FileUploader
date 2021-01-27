package su.grinev.FileUploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import su.grinev.FileUploader.jdbc.dao.JdbcTemplateFileMetadataDaoImpl;

import javax.naming.Context;

@SpringBootApplication
public class FileUploaderApplication {

	public static void main(String[] args) {

		SpringApplication.run(FileUploaderApplication.class, args);
	}

}
