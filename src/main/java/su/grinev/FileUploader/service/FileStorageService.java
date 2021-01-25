package su.grinev.FileUploader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import su.grinev.FileUploader.model.FileChunk;

import java.io.*;

@Service
public class FileStorageService {
    @Value(value = "${TMP_FILES_DIRECTORY}")
    String tempFilesDirectory;
    public FileStorageService(){}
    public FileStorageService(String tempFilesDirectory){
        this.tempFilesDirectory=tempFilesDirectory;
    }

    public void createFile(int id){
        String fileName=tempFilesDirectory+"/"+id+".part";
        try {
            File f = new File(fileName);
            if(f.exists() && !f.isDirectory())
                if (!f.delete()) throw new IOException();

            File myObj = new File(fileName);
            if (myObj.createNewFile()) System.out.println("File created: " + myObj.getName());
            else System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
    }

    public int fileUpload(int fileId, MultipartFile file) throws IOException {
        int bytesRead=0;
        int bytesCount=0;
        byte[] buffer=new byte[65536];
        InputStream is=file.getInputStream();
        FileOutputStream out = new FileOutputStream(tempFilesDirectory + "/" + fileId + ".part", true);
        try {
            while ((bytesRead = is.read(buffer))!=-1) {
                out.write(buffer, 0, bytesRead);
                bytesCount+=bytesRead;
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
            return bytesCount;
        }
        finally {
            out.close();
        }
        return bytesCount;
    }

    public boolean validChunkHashcode(FileChunk fileChunk) throws FileNotFoundException {
        //InputStream is = new BufferedOutputStream(new FileOutputStream(tempFilesDirectory+"/"+fileChunk.getChunkId()+".part", true));
        // TO DO: calcChunkHashcodeFromFile(...
        return true;
    }

    public void putChunkToFile(FileChunk fileChunk, MultipartFile chunkData) throws IOException {
        int bytesRead=0;
        byte[] buffer=new byte[256*1024];
        RandomAccessFile raf=new RandomAccessFile(tempFilesDirectory+"/"+fileChunk.getFileId()+".part", "rw");
        raf.seek(fileChunk.getOffset());

        InputStream is=chunkData.getInputStream();
        boolean uploaded = true;
        try {
            while ((bytesRead=is.read())!=-1){
                raf.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            uploaded = false;
            System.err.println("io exception");
        } finally {
            raf.close();
        }
    }

}