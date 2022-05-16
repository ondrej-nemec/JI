package ji.socketCommunication.http.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import ji.common.Logger;
import ji.common.structures.ThrowingConsumer;
import ji.socketCommunication.SSL;
import ji.socketCommunication.SslCredentials;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.Request;
import ji.socketCommunication.http.Response;
import ji.socketCommunication.http.parsers.ExchangeFactory;

public class RestApiClient {
	
	private final String serverUrl;
	private final int port;
	
	private final Logger logger;
	
	@SuppressWarnings("unused")
	private final String charset; // TODO remove
	
	private final Optional<SslCredentials> ssl;
	private final ExchangeFactory factory;
	
	public RestApiClient(String serverUrl, Optional<SslCredentials> ssl, String charset, Logger logger) {
		this(serverUrl, ssl.isPresent() ? 443 : 80, ssl, charset, logger);
	}
	
	public RestApiClient(String serverUrl, int port, Optional<SslCredentials> ssl, String charset, Logger logger) {
		this.serverUrl = serverUrl;
		this.logger = logger;
		this.charset = charset;
		this.ssl = ssl;
		this.port = port;
		this.factory = ExchangeFactory.create(null, logger); // TODO response limit
	}
	
	public Response get(String uri, ThrowingConsumer<Request, Exception> createRequest) throws Exception {
		return send(HttpMethod.GET, uri, createRequest);
	}
	
	public Response post(String uri, ThrowingConsumer<Request, Exception> createRequest) throws Exception {
		return send(HttpMethod.POST, uri, createRequest);
	}
	
	public Response put(String uri, ThrowingConsumer<Request, Exception> createRequest) throws Exception {
		return send(HttpMethod.PUT, uri, createRequest);
	}
	
	public Response delete(String uri, ThrowingConsumer<Request, Exception> createRequest) throws Exception {
		return send(HttpMethod.DELETE, uri, createRequest);
	}
	
	public Response send(HttpMethod method, String uri, ThrowingConsumer<Request, Exception> createRequest) throws Exception {
		Request request = new Request(method, uri, "HTTP/1.1"); // TODO set protocol?
		createRequest.accept(request);
		Socket con = createSocket(serverUrl, port, ssl);
		return send(con, request);
	}
	
	public Response send(Request request) throws Exception {
		Socket con = createSocket(serverUrl, port, ssl);
		//HttpURLConnection con = getConnection(request.getFullUrl(), ssl);
		return send(con, request);
	}
	
	/**************/
    
    private Socket createSocket(String ip, int port, Optional<SslCredentials> ssl) throws Exception {
    	if (ssl.isPresent()) {
    		return SSL.getSSLContext(ssl.get()).getSocketFactory().createSocket(ip, port);
		} else {
			logger.warn("No secured credentials, connection is not secured");
			return new Socket(ip, port);
		}
    }
	
	protected Response send(Socket con, Request request) throws IOException {
		try (BufferedInputStream is = new BufferedInputStream(con.getInputStream());
			BufferedOutputStream os = new BufferedOutputStream(con.getOutputStream());) {
			factory.write(request, os);
			Response response = factory.readResponse(is);
			return response;
	    }
	}
}
