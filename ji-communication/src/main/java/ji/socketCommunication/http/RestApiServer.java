package ji.socketCommunication.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import ji.socketCommunication.Servant;
import ji.socketCommunication.Server;
import ji.socketCommunication.SslCredentials;
import ji.socketCommunication.http.parsers.ExchangeFactory;
import ji.socketCommunication.http.profiler.HttpServerProfiler;
import ji.socketCommunication.http.profiler.HttpServerProfilerEvent;
import ji.socketCommunication.http.structures.Request;
import ji.socketCommunication.http.structures.Response;
import ji.socketCommunication.http.structures.WebSocket;

public class RestApiServer implements Servant {
	
	public static HttpServerProfiler PROFILER = null;
		
	private final Logger logger;
	private final Map<String, ResponseFactory> applications;
	
	private final ExchangeFactory factory;
	
	public RestApiServer(
			Integer maxRequestBodySize,
			Logger logger) {
		this.logger = logger;
		this.applications = new HashMap<>();
		this.factory = ExchangeFactory.create(maxRequestBodySize, logger);
	}
	
	@Override
	public void serve(Socket socket, String charset) throws IOException {
		try (BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
			serve(is, os, socket.getInetAddress().toString());
       } catch (Exception e) {
    	   logger.fatal("Uncaught exception", e);
       }
	}
    
    public RestApiServer addApplication(ResponseFactory servant, String hostname, String... aliases) {
    	applications.put(hostname, servant);
    	for (String alias : aliases) {
    		applications.put(alias, servant);
    	}
    	return this;
    }
    
    public void removeApplication(String hostname) {
    	applications.remove(hostname);
    }
    
    public Server createWebServer(int port,
    		int threadPool,
    		long readTimeout,
    		Optional<SslCredentials> ssl,
    		String charset) throws Exception {
    	return new Server(
    			port, 
    			threadPool,
    			readTimeout,
    			this,
    			ssl,
    			charset,
    			logger
    	);
    }
	
	protected void serve(BufferedInputStream is, BufferedOutputStream os, String clientIp) throws IOException {
		profile(HttpServerProfilerEvent.REQUEST_ACCEPT);
		Request request = factory.readRequest(is);
		profile(HttpServerProfilerEvent.REQUEST_PARSED);
		if (request == null) {
			logger.error("Unparsed request");
			factory.write(new Response(StatusCode.BAD_REQUEST, "HTTP/1.1"), os); // TODO protocol via request
			return;
		}
		profile(HttpServerProfilerEvent.RESPONSE_CREATED);
		
		Optional<WebSocket> websocket = Optional.empty();
		if ("websocket".equals(request.getHeader("Upgrade"))) {
			websocket = Optional.of(new WebSocket(os, is, request));
		}
		
		Object hostname = request.getHeader("Host");
		if (hostname != null) {
			String host = hostname.toString().split(":")[0];
			if (applications.containsKey(host)) {
				Response response = applications.get(host).accept(request, clientIp, websocket);
				if (websocket.isPresent() && websocket.get().isAccepted()) {
					response.setBodyWebsocket(websocket.get());
				}
				factory.write(response, os);
			} else {
				logger.warn("Request on not existing hostname: " + host);
				factory.write(new Response(StatusCode.BAD_REQUEST, "HTTP/1.1"), os); // TODO protocol via request
			}
		} else {
			logger.warn("Request Host header not specified");
			factory.write(new Response(StatusCode.BAD_REQUEST, "HTTP/1.1"), os); // TODO protocol via request
			return;
		}
		/*Response response = createResponce.accept(request, clientIp, websocket);
		if (websocket.isPresent() && websocket.get().isAccepted()) {
			response.setBodyWebsocket(websocket.get());
		}
		factory.write(response, os);*/
		profile(HttpServerProfilerEvent.RESPONSE_SENDED);
	}
	
	private void profile(HttpServerProfilerEvent event) {
		if (PROFILER != null) {
			PROFILER.log(event);
		}
	}
	
}
