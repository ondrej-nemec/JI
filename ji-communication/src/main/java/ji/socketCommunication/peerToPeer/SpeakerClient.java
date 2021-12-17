package ji.socketCommunication.peerToPeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import ji.common.Logger;
import ji.common.structures.ThrowingBiConsumer;import ji.socketCommunication.SSL;
import ji.socketCommunication.SslCredentials;

public class SpeakerClient {

private final Logger logger;
    
    private BufferedReader br;
    private BufferedWriter bw;

	private final Optional<SslCredentials> config;
	
    private final String ip;
    private final int port;
    private final int connectionTimeout;
    private final String charset;
    private Socket socket;
    
    public SpeakerClient(
    		String ip, int port, 
    		int connectionTimeout, int readTimeout, 
    		Optional<SslCredentials> config, 
    		String charset, Logger logger) throws UnknownHostException, IOException {
        this.logger = logger;
        this.ip = ip;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.charset = charset;
		this.config = config;
    }
    
    public void connect() throws Exception {
        logger.info("Connecting...");
    	this.socket = createSocket(ip, port);
        this.socket.setSoTimeout(connectionTimeout);
        
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
        logger.info("Connected to server");
    }
    
    private Socket createSocket(String ip, int port) throws Exception {
    	if (config.isPresent()) {
			return createSslSocket(ip, port, config.get());
		} else {
			logger.warn("No secured credentials, connection is not secured");
			return new Socket(ip, port);
		}
    }

	private Socket createSslSocket(String ip, int port, SslCredentials config) throws Exception {
        return SSL.getSSLContext(config).getSocketFactory().createSocket(ip, port);
	}

	@Deprecated
    public String communicate(String message) throws IOException {
        Communication.writeMessage(bw, message);
        return Communication.readMessage(br);
    }
    
    public void communicate(ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> consumer) throws IOException {
        consumer.accept(br, bw);
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
        
        logger.info("Disconnected from server");
    }
}
