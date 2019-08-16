package utils.serverclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Logger;

public class Client {
    
    private final Logger logger;
    
    private BufferedReader br;
    private BufferedWriter bw;
    private Socket socket;
    
    private final Communication com;
    
    public Client(String ip, int port, int connectionTimeout, int readTimeout, Logger logger) throws UnknownHostException, IOException {
        this.logger = logger;
        this.com = new Communication(readTimeout);
        
        this.socket = new Socket(ip, port);
        this.socket.setSoTimeout(connectionTimeout);
        
        logger.info("Client running");
        
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logger.info("Connected to server");
    }
    
    public String communicate(String message) throws IOException {
        com.writeMessage(bw, message);
        return com.readMessage(br);
    }
    
    public void disconnect() throws IOException {
        communicate(Communication.END);
        if (bw != null) {
            bw.close();
        }
        if (br != null) {
            br.close();
        }
        if (socket != null) {
            socket.close();
        }
        
        logger.info("Client shutdown");
    }

}
