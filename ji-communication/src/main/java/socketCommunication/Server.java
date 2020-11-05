package socketCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import common.Logger;
import core.text.InputStreamLoader;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.RestApiServer;
import socketCommunication.peerToPeer.Speaker;

public class Server {
	
	private final String tlsVersion = "TLSv1.2";
	
	private final Logger logger;
    
    private final ServerSocket serverSocket;
    
    private final ExecutorService executor;
    private final ScheduledExecutorService sheduled;
    private ScheduledFuture<?> clientWaitingFuture;
    
    private int threadCount = 0;
    private final int maxThread;
    
    private final long readTimeOut;
    
    private final String charset;
    
    private final Servant servant;
        
    public static Server create(int port,
    		int threadPool,
    		long readTimeout,
    		RestApiServerResponseFactory response,
    		Optional<ServerSecuredCredentials> config,
    		int maxUploadFileSize,
    		Optional<List<String>> allowedFileContentType,
    		String charset,
    		Logger logger) throws Exception {
    	return new Server(
    			port, 
    			threadPool,
    			readTimeout,
    			new RestApiServer(response, maxUploadFileSize, allowedFileContentType, logger),
    			config,
    			charset,
    			logger
    	);
    }
    
    public static Server create(int port,
    		int threadPool,
    		long readTimeout,
    		Function<String, String> response,
    		Optional<ServerSecuredCredentials> config,
    		String charset,
    		Logger logger) throws Exception {
    	return new Server(port, threadPool, readTimeout, new Speaker(response, logger), config, charset, logger);
    }
    
    public Server(
    		int port,
    		int threadPool,
    		long readTimeOut,
    		Servant servant,
    		Optional<ServerSecuredCredentials> config,
    		String charset,
    		Logger logger) throws Exception {
        this.executor = Executors.newFixedThreadPool(threadPool);
        this.sheduled = Executors.newScheduledThreadPool(1);
        this.logger = logger;
        this.servant = servant;
        this.charset = charset;
        this.maxThread = threadPool;
        this.readTimeOut = readTimeOut;
        
        this.serverSocket = getSocket(port, config);
        logger.info("Server prepared " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
    }
    
    private ServerSocket getSocket(int port, Optional<ServerSecuredCredentials> config) throws Exception {
    	if (config.isPresent()) {
    		return getSecuredSocket(port, config.get());
    	}
    	logger.warn("No Secured connection credentials, so unsecured server socket used");
    	return getUnsecuredSocket(port);
    }
    
    private ServerSocket getUnsecuredSocket(int port) throws IOException {
    	return new ServerSocket(port);
    }
    
    private ServerSocket getSecuredSocket(int port, ServerSecuredCredentials config) throws Exception {
    	/***/
    	TrustManager[] trustManager = null;
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
        /***/
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream kstore = InputStreamLoader.createInputStream(getClass(), config.getServerKeyStore());
        keyStore.load(kstore, config.getServerKeystorePassword().toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, config.getServerKeystorePassword().toCharArray());
        /***/
        
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), trustManager, SecureRandom.getInstanceStrong());

        SSLServerSocketFactory factory = ctx.getServerSocketFactory();
        
        ServerSocket listener = factory.createServerSocket(port);
        SSLServerSocket sslListener = (SSLServerSocket) listener;

        if (config.getClientTrustStore().isPresent()) {
        	sslListener.setNeedClientAuth(true); // client certs required
        } else {
        	sslListener.setNeedClientAuth(false); // client certs not required
        }
        sslListener.setEnabledProtocols(new String[] {tlsVersion});
   		return sslListener;
    }

    public synchronized void start() {
    	try {
			serverSocket.setSoTimeout(0);
		} catch (SocketException e) {
			logger.fatal("Server secket waiting time cannot be setted", e);
		}
    	if (clientWaitingFuture == null) {
    		clientWaitingFuture = sheduled.scheduleAtFixedRate(getClientChacker(), 0, 10, TimeUnit.MILLISECONDS);
    	}
        logger.info("Server running");
    }
    
    public synchronized void pause() {
        try {
			serverSocket.setSoTimeout(1);
		} catch (SocketException e) {
			logger.fatal("Server secket waiting time cannot be setted", e);
		}
    	if (clientWaitingFuture != null) {
    		clientWaitingFuture.cancel(true);
	    	clientWaitingFuture = null;
	    	logger.info("Server paused - no serving to clients");
    	}
    	
    }
    
    public void stop() throws InterruptedException {
    	try {
			serverSocket.setSoTimeout(1);
		} catch (SocketException e) {
			logger.fatal("Server secket waiting time cannot be setted", e);
		}
    	logger.info("Stopping server");
    	executor.shutdownNow();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        sheduled.shutdownNow();
        sheduled.awaitTermination(30, TimeUnit.SECONDS);
        logger.info("Server stopped");
    }
    
    protected int getActualThreadCount() {
        return threadCount;
    }

    /****************************/
    
    private Runnable getClientChacker() {
        return ()->{
        	if (Thread.currentThread().isInterrupted()) {
        		return;
        	}
            try {
            	logger.trace(String.format("Waiting for client(%d/%d)...", threadCount, maxThread));
            	Socket clientSocket = serverSocket.accept(); // accept is blocking
            	if (Thread.currentThread().isInterrupted()) {
            		return;
            	}
                executor.execute(serveToClient(clientSocket,  charset));
            } catch (SocketTimeoutException e) {
                try {
					logger.warn("Connection closed - reading timeout: " + serverSocket.getSoTimeout());
				} catch (IOException e1) {
					logger.fatal("Preparing sockets", e);
				}
            } catch (IOException e) {
                logger.fatal("Preparing sockets", e);
            }
        };
    }
    
    /*************************/
    
    private Runnable serveToClient(Socket clientSocket, String charset) {
        return ()->{
            threadCount++;
            String client =  clientSocket.getInetAddress() + ":" + clientSocket.getPort();
            logger.trace("Client " + threadCount + " connected - " + client);
            try {
				clientSocket.setSoTimeout((int)readTimeOut);
				servant.serve(clientSocket, charset);
				
		//	} catch (SocketException e) {
		//		logger.error("Read Timeout cannot be setted", e);
			} catch (SocketTimeoutException e) {
                logger.warn("Connection closed - reading timeout");
            } catch (IOException e) {
                logger.fatal("Reading from socket", e);
            } catch (Exception e) {
            	logger.fatal("Problem in Server", e);
            } finally {
            	try {
					clientSocket.close();
				} catch (IOException e) {
					logger.info("Socket closing problem", e);
				}
				threadCount--;
			}
            
            logger.trace("Client " + threadCount + " disconnected - " + client);
        };
    }
}
