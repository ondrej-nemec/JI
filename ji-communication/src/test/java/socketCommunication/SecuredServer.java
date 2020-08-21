package socketCommunication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
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
import javax.net.ssl.TrustManagerFactory;

import common.Logger;
import core.text.Binary;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.StatusCode;
import socketCommunication.http.server.RestApiResponse;
import socketCommunication.http.server.RestApiServer;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.session.MemorySessionStorage;
import socketCommunication.http.server.session.Session;
import socketCommunication.peerToPeer.Speaker;

public class SecuredServer {
	

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
    
    
    private static final int SERVER_PORT = 8444;
    private static final String TLS_VERSION = "TLSv1.2";
    private static final int SERVER_COUNT = 1;
    private static final String SERVER_HOST_NAME = "127.0.0.1";
    // for host
    private static final String TRUST_STORE_NAME = "certs2/keystore.jks";
    private static final char[] TRUST_STORE_PWD = new char[] {'a', 'b', 'c', '1',
        '2', '3'};
    
    // for server
    private static final String KEY_STORE_NAME = "certs2/keystore.jks";
    private static final char[] KEY_STORE_PWD = new char[] {'a', 'b', 'c', '1',
        '2', '3'};
    
    
    public static SecuredServer create(int port,
    		int threadPool,
    		long readTimeout,
    		RestApiServerResponseFactory response,
    		String charset,
    		Logger logger) throws Exception {
    	return new SecuredServer(port, threadPool, readTimeout, new RestApiServer(0, response,
				new MemorySessionStorage(),  logger), charset, logger);
    }
    
    public SecuredServer(
    		int port,
    		int threadPool,
    		long readTimeOut,
    		Servant servant,
    		String charset,
    		Logger logger) throws Exception {
        this.executor = Executors.newFixedThreadPool(threadPool);
        this.sheduled = Executors.newScheduledThreadPool(1);
        this.logger = logger;
        this.servant = servant;
        this.charset = charset;
        this.maxThread = threadPool;
        this.readTimeOut = readTimeOut;
        /*************************/
        Objects.requireNonNull(TLS_VERSION, "TLS version is mandatory");

        if (port <= 0) {
          throw new IllegalArgumentException(
              "Port number cannot be less than or equal to 0");
        }

        	KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        InputStream tstore = getClass().getResourceAsStream("/" + TRUST_STORE_NAME);
	        trustStore.load(tstore, TRUST_STORE_PWD);
	        tstore.close();
	        TrustManagerFactory tmf = TrustManagerFactory
	            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        tmf.init(trustStore);
	
	        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        InputStream kstore = getClass().getResourceAsStream("/" + KEY_STORE_NAME);
	        keyStore.load(kstore, KEY_STORE_PWD);
	        KeyManagerFactory kmf = KeyManagerFactory
	            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        kmf.init(keyStore, KEY_STORE_PWD);
	        
	        
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
	            SecureRandom.getInstanceStrong());
	
	        SSLServerSocketFactory factory = ctx.getServerSocketFactory();
	        
	        ServerSocket listener = factory.createServerSocket(port);
          SSLServerSocket sslListener = (SSLServerSocket) listener;

          sslListener.setNeedClientAuth(false); // TODO true
          sslListener.setEnabledProtocols(new String[] {TLS_VERSION});
       		this.serverSocket = sslListener;
        /*******************************
        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
                .getDefault();
        SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory
                .createServerSocket(port);
        this.serverSocket = sslserversocket;
        
        /***********/
        //this.serverSocket = new ServerSocket(port);
     //   serverSocket.setSoTimeout((int)clientWaitTimeout);
     //   logger.info("Server prepared " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
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
    		serverSocket.close();
		} catch (IOException e) {
			logger.fatal("Server secket", e);
		}
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
            
            logger.info(
                "Client " + threadCount + " connected - " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()
            );
            try {
				clientSocket.setSoTimeout((int)readTimeOut);
				servant.serve(clientSocket, charset);
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
    
    /**************************/

	public static void main(String[] args) {
		try {
			//*
			SecuredServer server = SecuredServer.create(443, 5, 60000, apiResponse(), "UTF-8",  new LoggerImpl());
			/*/
			Server server = Server.create(10123, 5, 60000, speakerFunction(), "UTF-8", new LoggerImpl());
			//*/
			
			server.start();
			for (int i = 0; i < 30; i++) {
                Thread.sleep(i * 10000);
            //    console.out("Server running");
            }
            server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected static RestApiServerResponseFactory apiResponse() {
		return new RestApiServerResponseFactory() {

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, Properties params, Session session) throws IOException {
				/*if ("1".equals(header.get("Upgrade-Insecure-Requests"))) {
					return getCert();
				}*/
				if (url.equals("/redirect")) {
					return getRedirect();
				}
				if (url.equals("/favicon.ico")) {
					return getFavicon();
				}
				return getHtml();
			}
			
			private RestApiResponse getRedirect() {
				return RestApiResponse.binaryResponse(
						StatusCode.TEMPORARY_REDIRECT,
						Arrays.asList(
								"Access-Control-Allow-Origin: *",
								"Content-Type: image/ico; charset=utf-8",
								"X-XSS-Protection: 1; mode=block",
								"Location: /test"
						),
						(bos)->{}
					);
			}

			private RestApiResponse getCert() {
				return RestApiResponse.binaryResponse(
					StatusCode.OK,
					Arrays.asList(
							"Access-Control-Allow-Origin: *",
							"Content-Type: image/ico; charset=utf-8",
							"X-XSS-Protection: 1; mode=block"
					),
					(bos)->{
							Binary.read((dis)->{
								byte[] cache = new byte[32];
								while (dis.read(cache) != -1) {
									bos.write(cache);
								}
							}, getClass().getResourceAsStream("/socketCommunication/cert.pem"));
					}
				);
			}
			
			private RestApiResponse getFavicon() {
				return RestApiResponse.binaryResponse(
					StatusCode.OK,
					Arrays.asList(
							"Access-Control-Allow-Origin: *",
							"Content-Type: image/ico; charset=utf-8",
							"X-XSS-Protection: 1; mode=block"
					),
					(bos)->{
							Binary.read((dis)->{
								byte[] cache = new byte[32];
								while (dis.read(cache) != -1) {
									bos.write(cache);
								}
							}, getClass().getResourceAsStream("/certs/favicon.ico"));
					}
				);
			}
			
			private RestApiResponse getHtml() {
				Date today = new Date();
				return RestApiResponse.textResponse(
					StatusCode.OK,
					Arrays.asList(
							"Access-Control-Allow-Origin: *", 
							"Content-Type: text/html; charset=utf-8",
							"X-XSS-Protection: 1; mode=block"
					),
					(bw)->{
						bw.write(String.format(
								"<html> <head></head><body><h1>Time</h1>%s"
								+ "<br><form><label for='name'></label><input type='text' name='name'/>"
								+ "<input type='submit' value='submit'></form>"
								+ "</body></html>", today.toString()
						));
					}
				);
			}

		};
	}

}
