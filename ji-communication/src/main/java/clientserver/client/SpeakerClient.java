package clientserver.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import clientserver.Communication;
import common.Logger;

public class SpeakerClient {

private final Logger logger;
    
    private BufferedReader br;
    private BufferedWriter bw;
    private Socket socket;
    
    public SpeakerClient(String ip, int port, int connectionTimeout, int readTimeout, Logger logger) throws UnknownHostException, IOException {
        this.logger = logger;
        
        this.socket = new Socket(ip, port);
        this.socket.setSoTimeout(connectionTimeout);
        
        logger.info("Client running");
        
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logger.info("Connected to server");
    }
    
    public String communicate(String message) throws IOException {
        Communication.writeMessage(bw, message);
        return Communication.readMessage(br);
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
