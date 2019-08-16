package utils.serverclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class Communication {
    
    public static final String END = "\u0000\u0000";

    private long readTimeout;
    
    public Communication(long readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public Communication(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public String readMessage(BufferedReader br) throws IOException {
        long start = System.currentTimeMillis(); //ms
        String message = null;
        while (message == null) {
            if (System.currentTimeMillis() - start >= readTimeout) {
                throw new SocketTimeoutException("Reading from client");
            }
            message = br.readLine();
        }
        return message;
    }
    
    public void writeMessage(BufferedWriter bw, String... messages) throws IOException {
        for (String message : messages) {
            bw.write(message);
        }
        bw.write("\r\n");
        bw.flush();
    }
}
