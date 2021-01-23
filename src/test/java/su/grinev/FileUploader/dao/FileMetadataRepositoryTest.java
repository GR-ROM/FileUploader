package su.grinev.FileUploader.dao;



import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import su.grinev.FileUploader.model.FileMetadata;

import java.util.Map;
import java.util.stream.Stream;
import static org.assertj.core.api.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void shouldFindByHash() throws Exception {
        fileMetadataRepository.save(testFileMetaData);
        assertNotNull(fileMetadataRepository.getFileMetadata());
        assertEquals(fileMetadata, fileMetadataRepository.findByHash("hashtest"));
    }

    @BeforeEach
    public void setUp(){
        testFileMetaData=new FileMetadata(1, "hashtest", "test", 1000l, "test");
        fileMetadataRepository= Mockito.mock(FileMetadataRepository.class);
        fileMetadataRepository.setFileMetadata(fileMetadata);
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
