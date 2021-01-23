package su.grinev.FileUploader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileStorageService {

    String tempFilesDirectory;
    public FileStorageService(){}
    public FileStorageService(@Value(value = "${TMP_FILES_DIRECTORY}")
                                      String tempFilesDirectory){
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

    public int fileUpload(int fileId, int bytesCount, InputStream is) throws IOException {
        int bytesRead=0;
        byte[] buffer=new byte[65536];
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

    public void chunkedFileUpload(int fileId, long offset, long length, InputStream is) throws IOException {
        int bytesRead=0;
        byte[] buffer=new byte[256*1024];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFilesDirectory+"/"+fileId+".part", true));
        boolean uploaded = true;
        try {
            while ((bytesRead=is.read())!=-1){
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            uploaded = false;
            System.err.println("io exception");
        } finally {
            out.close();
        }
    }

}