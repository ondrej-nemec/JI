package socketCommunication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import common.Logger;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.RestApiServer;
import socketCommunication.peerToPeer.Speaker;

public class Server {
	
	private final Logger logger;
    
    private final ServerSocket serverSocket;
    
    private final ExecutorService executor;
    private final ScheduledExecutorService sheduled;
    private ScheduledFuture<?> clientWaitingFuture;
    
    private int threadCount = 0;
    private final int maxThread;
    
    private final long clientWaitTimeout;
    private final long readTimeOut;
    
    private final String charset;
    
    private final Servant servant;
    
    public static Server create(int port,
    		int threadPool,
    		long clientWaitTimeout,
    		long readTimeout,
    		RestApiServerResponseFactory response,
    		String charset,
    		Logger logger) throws IOException {
    	return new Server(port, threadPool, clientWaitTimeout, readTimeout, new RestApiServer(response, logger), charset, logger);
    }
    
    public static Server create(int port,
    		int threadPool,
    		long clientWaitTimeout,
    		long readTimeout,
    		Function<String, String> response,
    		String charset,
    		Logger logger) throws IOException {
    	return new Server(port, threadPool, clientWaitTimeout, readTimeout, new Speaker(response, logger), charset, logger);
    }
    
    public Server(
    		int port,
    		int threadPool,
    		long clientWaitTimeout,
    		long readTimeOut,
    		Servant servant,
    		String charset,
    		Logger logger) throws IOException {
        this.executor = Executors.newFixedThreadPool(threadPool);
        this.sheduled = Executors.newScheduledThreadPool(1);
        this.logger = logger;
        this.servant = servant;
        this.charset = charset;
        this.maxThread = threadPool;
        this.clientWaitTimeout = clientWaitTimeout;
        this.readTimeOut = readTimeOut;
        
        this.serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout((int)clientWaitTimeout);
        logger.info("Server prepared " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
    }

    public synchronized void start() {
    	clientWaitingFuture = sheduled.scheduleAtFixedRate(getClientChacker(), 0, 10, TimeUnit.MILLISECONDS);
        logger.info("Server running");
    }
    
    public synchronized void pause() {
    	if (clientWaitingFuture != null) {
    		clientWaitingFuture.cancel(true);
	    	clientWaitingFuture = null;
	    	logger.info("Server paused - no serving to clients");
    	}
    	
    }
    
    public void stop() throws InterruptedException {
    	logger.info("Stopping server");
    	executor.shutdownNow();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        sheduled.shutdownNow();
        sheduled.awaitTermination(30, TimeUnit.SECONDS);
        logger.info("Server stopped");
    }
    
    @Deprecated
    public void pause(boolean isPaused) {
    	if (isPaused) {
    		pause();
    	} else {
    		start();
    	}
    }
    
    @Deprecated
    public void stop(long timeout, TimeUnit unit) throws InterruptedException {
    	stop();
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
            	logger.info(String.format("Waiting for client(%d/%d)...", threadCount, maxThread));
            	Socket clientSocket = serverSocket.accept(); // accept is blocking
            	if (Thread.currentThread().isInterrupted()) {
            		return;
            	}
                executor.execute(serveToClient(clientSocket,  charset));
            } catch (SocketTimeoutException e) {
                logger.warn("Connection closed - reading timeout: " + clientWaitTimeout);
            } catch (IOException e) {
                logger.fatal("Preparing sockets", e);
            }
        };
    }
    
    /*************************/
    
    private Runnable serveToClient(Socket clientSocket, String charset) {
        return ()->{
            threadCount++;
            
            logger.info(
                "Client " + threadCount + " connected - " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
            );
            try {
				clientSocket.setSoTimeout((int)readTimeOut);
				try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), charset));
		            	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), charset));
		            	 BufferedInputStream is = new BufferedInputStream(clientSocket.getInputStream());
		            	 BufferedOutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());) {
		            servant.serve(br, bw, is, os);
		        }
			} catch (SocketException e) {
				logger.error("Read Timeout cannot be setted", e);
			} catch (SocketTimeoutException e) {
                logger.warn("Connection closed - reading timeout");
            } catch (IOException e) {
                logger.fatal("Reading from socket", e);
            } catch (Exception e) {
            	logger.fatal("Problem in Server", e);
            } finally {
				threadCount--;
			}
            
            logger.info(
                "Client " + threadCount + " disconnected - " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
            );
        };
    }
}
