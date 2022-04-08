package ji.socketCommunication.http.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;

import ji.common.Logger;
import ji.common.structures.ThrowingConsumer;
import ji.socketCommunication.SSL;
import ji.socketCommunication.SslCredentials;
import ji.socketCommunication.http.ApiRequest;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.parsers.Parser;

public class RestApiClient {
	
	private final String serverUrl;
	private final int port;
	
	private final Logger logger;
	
	private final String charset;
	
	private final Optional<SslCredentials> ssl;
	
	public RestApiClient(String serverUrl, Optional<SslCredentials> ssl, String charset, Logger logger) {
		this(serverUrl, ssl.isPresent() ? 443 : 80, ssl, charset, logger);
	}
	
	public RestApiClient(String serverUrl, int port, Optional<SslCredentials> ssl, String charset, Logger logger) {
		this.serverUrl = serverUrl;
		this.logger = logger;
		this.charset = charset;
		this.ssl = ssl;
		this.port = port;
	}
	
	public ApiRequest get(String uri, ThrowingConsumer<ApiRequest, Exception> createRequest) throws Exception {
		return send(HttpMethod.GET, uri, createRequest);
	}
	
	public ApiRequest post(String uri, ThrowingConsumer<ApiRequest, Exception> createRequest) throws Exception {
		return send(HttpMethod.POST, uri, createRequest);
	}
	
	public ApiRequest put(String uri, ThrowingConsumer<ApiRequest, Exception> createRequest) throws Exception {
		return send(HttpMethod.PUT, uri, createRequest);
	}
	
	public ApiRequest delete(String uri, ThrowingConsumer<ApiRequest, Exception> createRequest) throws Exception {
		return send(HttpMethod.DELETE, uri, createRequest);
	}
	
	public ApiRequest send(HttpMethod method, String uri, ThrowingConsumer<ApiRequest, Exception> createRequest) throws Exception {
		ApiRequest request = new ApiRequest(method, uri, "HTTP/1.1");
		createRequest.accept(request);
		Socket con = createSocket(serverUrl, port, ssl);
		return send(con, request);
	}
	
	public ApiRequest send(ApiRequest request) throws Exception {
		// TODO maybe parse url params to url but usualy in uri
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
	
	protected ApiRequest send(Socket con, ApiRequest request) throws IOException {
		Parser parser = new Parser(logger, 0, Optional.empty()); // TODO
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
	         	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), charset));
	         	 BufferedInputStream is = new BufferedInputStream(con.getInputStream());
	         	 BufferedOutputStream os = new BufferedOutputStream(con.getOutputStream());) {
			parser.writeRequest(request, bw, os);
			ApiRequest response = parser.readRequest(br, is);
			return response;
	    }
	}
}
