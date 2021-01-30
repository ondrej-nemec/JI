package socketCommunication.peerToPeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import common.Logger;
import common.structures.ThrowingBiConsumer;
import core.text.InputStreamLoader;
import socketCommunication.ClientSecuredCredentials;

public class SpeakerClient {

private final Logger logger;
    
    private BufferedReader br;
    private BufferedWriter bw;

	private final Optional<ClientSecuredCredentials> config;
	
    private final String ip;
    private final int port;
    private final int connectionTimeout;
    private final String charset;
    private Socket socket;
    
    public SpeakerClient(
    		String ip, int port, 
    		int connectionTimeout, int readTimeout, 
    		Optional<ClientSecuredCredentials> config, 
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

	private Socket createSslSocket(String ip, int port, ClientSecuredCredentials config) throws Exception {
		TrustManager[] trustManager = null;
		if (config.getClientTrustStore().isPresent()) {
	    	if (config.getClientTrustStore().isPresent()) {
	    		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		        InputStream tstore = InputStreamLoader.createInputStream(getClass(), config.getClientTrustStore().get());
		        trustStore.load(tstore, config.getClientTrustStorePassword().orElse("").toCharArray());
		        tstore.close();
		        TrustManagerFactory tmf = TrustManagerFactory
		            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
		        tmf.init(trustStore);
		        trustManager = tmf.getTrustManagers();
	    	}
		} else {
			// trust all certs
			trustManager = new TrustManager[]{
			    new X509TrustManager() {
					@Override public X509Certificate[] getAcceptedIssuers() { return null; }
					@Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
					@Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
				}
			};
		}
		SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustManager, SecureRandom.getInstanceStrong());
        return ctx.getSocketFactory().createSocket(ip, port);
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
