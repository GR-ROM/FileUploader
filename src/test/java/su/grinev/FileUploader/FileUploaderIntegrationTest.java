package su.grinev.FileUploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import su.grinev.FileUploader.dto.FileChunkDto;
import su.grinev.FileUploader.dto.FileMetadataDto;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploaderIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private FileMetadataDto request;

    @BeforeEach
    public void initTest(){
        this.request=new FileMetadataDto();
        this.request.setFileName("test");
        this.request.setSize(1000l);
    }

    @Test
    public void shouldCreateFile() throws Exception {
        //mvc=new M
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/files/upload/create")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
