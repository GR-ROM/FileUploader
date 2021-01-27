package su.grinev.FileUploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import su.grinev.FileUploader.jdbc.dao.JdbcTemplateFileMetadataDaoImpl;

@SpringBootApplication
public class FileUploaderApplication {

	public static void main(String[] args) {

		SpringApplication.run(FileUploaderApplication.class, args);

	}

}
