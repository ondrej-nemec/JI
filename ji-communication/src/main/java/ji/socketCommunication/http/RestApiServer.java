package ji.socketCommunication.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import ji.common.Logger;
import ji.socketCommunication.Servant;
import ji.socketCommunication.http.parsers.ExchangeFactory;
import ji.socketCommunication.http.profiler.HttpServerProfiler;
import ji.socketCommunication.http.profiler.HttpServerProfilerEvent;
import ji.socketCommunication.http.structures.Request;
import ji.socketCommunication.http.structures.Response;
import ji.socketCommunication.http.structures.WebSocket;

public class RestApiServer implements Servant {
	
	public static HttpServerProfiler PROFILER = null;
		
	private final Logger logger;
	private final ResponseFactory createResponce;
	
	private final ExchangeFactory factory;
	
	public RestApiServer(
			ResponseFactory response,
			Integer maxUploadFileSize,
			Logger logger) {
		this.logger = logger;
		this.createResponce = response;
		this.factory = ExchangeFactory.create(maxUploadFileSize, logger);
	}
	
	@Override
	public void serve(Socket socket, String charset) throws IOException {
		try (BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
			serve(is, os, socket.getInetAddress().toString());
       } catch (Exception e) {
    	   createResponce.catchException(e);
       }
	}
	
	protected void serve(BufferedInputStream is, BufferedOutputStream os, String clientIp) throws IOException {
		profile(HttpServerProfilerEvent.REQUEST_ACCEPT);
		Request request = factory.readRequest(is);
		profile(HttpServerProfilerEvent.REQUEST_PARSED);
		if (request == null) {
			logger.error("Unparsed request");
			factory.write(new Response(StatusCode.BAD_REQUEST, "HTTP/1.1"), os);
			return;
		}
		profile(HttpServerProfilerEvent.RESPONSE_CREATED);
		
		Optional<WebSocket> websocket = Optional.empty();
		if ("websocket".equals(request.getHeader("Upgrade"))) {
			websocket = Optional.of(new WebSocket(os, is, request));
		}
		
		Response response = createResponce.accept(request, clientIp, websocket);
		if (websocket.isPresent() && websocket.get().isAccepted()) {
			response.setBodyWebsocket(websocket.get());
		}
		factory.write(response, os);
		profile(HttpServerProfilerEvent.RESPONSE_SENDED);
	}
	
	private void profile(HttpServerProfilerEvent event) {
		if (PROFILER != null) {
			PROFILER.log(event);
		}
	}
	
}
