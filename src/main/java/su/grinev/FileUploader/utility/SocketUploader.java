package su.grinev.FileUploader.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import su.grinev.FileUploader.model.FileChunk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Stream;

public class SocketUploader implements Runnable {

    private String tempFilesDirectory;
    private FileChunk fileChunk;
    private RandomAccessFile raf;
    private final short port;
    private boolean isUploading;
    private boolean stop;

    public void setTempFilesDirectory(String tempFilesDirectory){
        this.tempFilesDirectory=tempFilesDirectory;
    }

    public SocketUploader(FileChunk fileChunk, short port){
        this.fileChunk=fileChunk;
        this.port = port;
        this.isUploading=false;
        this.stop=false;
        this.raf=null;
    }

    public synchronized void setFileChunk(FileChunk fileChunk){
        if (!this.isUploading){
            this.fileChunk=fileChunk;
        }
    }

    public synchronized void resetFilePointer() {
        if (!this.isUploading && raf!=null) {
            try {
                raf.seek(fileChunk.getOffset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setStop() {
        this.stop=true;
    }

    @Override
    public void run() {
        ServerSocket servsock;
        try {
            servsock = new ServerSocket(port);
            RandomAccessFile raf=new RandomAccessFile(tempFilesDirectory+"/"+fileChunk.getFileId()+".part", "rw");
            raf.seek(fileChunk.getOffset());
            while (!this.stop) {
                Socket sock = servsock.accept();
                byte[] byteArray = new byte[65*1024];
                int bytesCount=0;
                int bytesReceived;
                InputStream is = sock.getInputStream();
                this.isUploading=true;
                while (((bytesReceived=is.read(byteArray))!=-1) || bytesCount==fileChunk.getSize()){
                    raf.write(byteArray, 0, bytesReceived);
                    bytesCount+=bytesReceived;
                }
                this.isUploading=false;
                sock.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}