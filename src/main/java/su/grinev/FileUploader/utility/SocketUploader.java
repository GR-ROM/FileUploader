package su.grinev.FileUploader.utility;

import su.grinev.FileUploader.model.FileChunk;
import su.grinev.FileUploader.service.DataConnectionPoolService;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketUploader implements Runnable {

    private final DataConnectionPoolService dataConnectionPoolService;
    private String tempFilesDirectory;
    private FileChunk fileChunk;
    private final short port;
    private boolean isUploading;

    public boolean isUploading() {
        return isUploading;
    }

    public void setTempFilesDirectory(String tempFilesDirectory) {
        this.tempFilesDirectory = tempFilesDirectory;
    }

    public SocketUploader(FileChunk fileChunk, short port, DataConnectionPoolService dataConnectionPoolService) {
        this.fileChunk = fileChunk;
        this.port = port;
        this.isUploading = false;
        RandomAccessFile raf = null;
        this.dataConnectionPoolService = dataConnectionPoolService;
    }

    public synchronized void setFileChunk(FileChunk fileChunk) {
        if (!this.isUploading) {
            this.fileChunk = fileChunk;
        }
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            RandomAccessFile raf = new RandomAccessFile(tempFilesDirectory + "/" + fileChunk.getFileId() + ".part", "rw");
            raf.seek(fileChunk.getOffset());

            Socket sock = serverSocket.accept();
            byte[] byteArray = new byte[65 * 1024];
            int bytesCount = 0;
            int bytesReceived;
            InputStream is = sock.getInputStream();
            this.isUploading = true;
            while (((bytesReceived = is.read(byteArray)) != -1) || bytesCount == fileChunk.getSize()) {
                raf.write(byteArray, 0, bytesReceived);
                bytesCount += bytesReceived;
            }
            this.isUploading = false;
            sock.close();
            dataConnectionPoolService.closeDataConnectionByChunkId(fileChunk.getChunkId(), false);
        } catch (IOException e) {
            dataConnectionPoolService.closeDataConnectionByChunkId(fileChunk.getChunkId(), false);
            e.printStackTrace();
        }
    }
}