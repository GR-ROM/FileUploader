package su.grinev.FileUploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import su.grinev.FileUploader.dto.FileChunkDto;
import su.grinev.FileUploader.dto.FileMetadataDto;

import java.io.*;
import java.net.Socket;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploaderIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private FileMetadataDto fileMetadataDto;
    private FileChunkDto fileChunkDto;

    @BeforeEach
    public void initTest() {
        this.fileMetadataDto = new FileMetadataDto();
        this.fileMetadataDto.setFileName("test");
        this.fileMetadataDto.setSize(1000l);

        this.fileChunkDto = new FileChunkDto();
        this.fileChunkDto.setOffset(0l);
        this.fileChunkDto.setSize(1000);
    }

    @Test
    public void shouldCreateFile() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/files/upload/create")
                .content(asJsonString(fileMetadataDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }

    @Test
    public void shouldCreateFileAndOpenDataConnectionUploadFileInOneChunkAndCloseConnection() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/files/upload/create")
                .content(asJsonString(fileMetadataDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        int fileId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.fileId");
        fileChunkDto.setFileId(fileId);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/files/upload/dataconnection/open")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fileChunkDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int chunkId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.chunkId");
        int dataPort = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.dataPort");

        Socket socket = new Socket("localhost", dataPort);
        byte[] byteArray = new byte[65*1024];

        int bytesCount=0;
        int bytesReceived;
        File file = new File("C:/srv/test/1.file");
        OutputStream os=socket.getOutputStream();
        FileInputStream fis=new FileInputStream(file);

        while ((bytesReceived=fis.read(byteArray))!=-1){
            os.write(byteArray, 0, bytesReceived);
            bytesCount+=bytesReceived;
        }
        socket.close();
        System.out.println(""+bytesCount);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/files/upload/dataconnection/close")
                .contentType(MediaType.APPLICATION_JSON)
                .param("chunkId", String.valueOf(chunkId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void shouldCreateFileAndOpenDataConnectionAndDoNothing() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/files/upload/create")
                .content(asJsonString(fileMetadataDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int fileId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.fileId");
        fileChunkDto.setFileId(fileId);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/files/upload/dataconnection/open")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fileChunkDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        int chunkId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.chunkId");
        int dataPort = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.dataPort");
        Thread.sleep(20000);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
