package clientserver.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import common.Logger;
import common.structures.ThrowingBiConsumer;

public class Server {
	
	private final Logger logger;
    
    private final ServerSocket serverSocket;
    
    private final ExecutorService executor;
    private final ScheduledExecutorService sheduled;
    
    private int threadCount = 0;
    private final int maxThread;
    
    private final String charset;
    
    private final ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> servant;
    
    public static Server create(int port,
    		int threadPool,
    		long readTimeout,
    		CreateRestAPIResponce response,
    		String charset,
    		Logger logger) throws IOException {
    	return new Server(port, threadPool, readTimeout, new RestAPI(response, logger), charset, logger);
    }
    
    public static Server create(int port,
    		int threadPool,
    		long readTimeout,
    		Function<String, String> response,
    		String charset,
    		Logger logger) throws IOException {
    	return new Server(port, threadPool, readTimeout, new Speaker(response, logger), charset, logger);
    }
    
    protected Server(
    		int port,
    		int threadPool,
    		long readTimeout,
    		ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> servant,
    		String charset,
    		Logger logger) throws IOException {
        this.executor = Executors.newFixedThreadPool(threadPool);
        this.sheduled = Executors.newScheduledThreadPool(1);
        this.logger = logger;
        this.servant = servant;
        this.charset = charset;
        this.maxThread = threadPool;
        
        this.serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout((int)readTimeout);
        logger.info("Server prepared " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
    }

    public void start() {
        sheduled.scheduleAtFixedRate(getClientChacker(), 0, 10, TimeUnit.MILLISECONDS);
        logger.info("Server running");
    }
    
    public void stop() throws InterruptedException {
        sheduled.shutdown();
        wait();
        logger.info("Server stopped");
    }
    
    protected int getActualThreadCount() {
        return threadCount;
    }

    /****************************/
    
    private Runnable getClientChacker() {
        return ()->{
            try {
            	logger.info(String.format("Waiting for client(%d/%d)...", threadCount, maxThread));
                executor.execute(
                    serveToClient(
                        serverSocket.accept(), // accept is blocking
                        charset
                    )
                );
            } catch (SocketTimeoutException e) {
                logger.warn("Connection closed - reading timeout");
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
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), charset));
            		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), charset));) {
            	servant.accept(br, bw);
            } catch (SocketTimeoutException e) {
                logger.warn("Connection closed - reading timeout");
            } catch (IOException e) {
                logger.fatal("Reading from socket", e);
            } finally {
				threadCount--;
			}
            
            logger.info(
                "Client " + threadCount + " disconnected - " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
            );
        };
    }
}
