package su.grinev.FileUploader.dao;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import su.grinev.FileUploader.jdbc.model.FileMetadata;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileMetadataRepositoryTest {

    @InjectMocks
    FileMetadataRepository fileMetadataRepository;
    @MockBean
    Map<Integer, FileMetadata> fileMetadata;
    @InjectMocks
    FileMetadata testFileMetaData;

    @Test
    void testInjection(){
        assertNotNull(fileMetadataRepository.getFileMetadata());
    }

    @Test
    void shouldHaveItem(){
        assertNotNull(fileMetadataRepository.findById(1));
    }

    @Test
    void shouldFindByHash() throws Exception {
        fileMetadataRepository.save(testFileMetaData);
        assertEquals(fileMetadata, fileMetadataRepository.findByHash(111));
    }

    @BeforeEach
    public void setUp(){
        testFileMetaData=new FileMetadata(1, "test", "hashtest", 111, 1000l, 0, Instant.now());
        fileMetadataRepository= Mockito.mock(FileMetadataRepository.class);
        fileMetadataRepository.save(testFileMetaData);
    }
/*
    public static Stream<Arguments> values(){
        return Stream.of(
                Arguments.of(new FileMetadata(0, "1111", "test",  100l, "test"),
                Arguments.of(new FileMetadata(1, "1112", "test", 100l, "test"))),
                Arguments.of(new FileMetadata(2, "1113", "test", 100l, "test"))
        );
    }

 */
}
