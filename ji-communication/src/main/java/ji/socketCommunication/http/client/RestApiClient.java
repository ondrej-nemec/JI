package ji.socketCommunication.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import ji.common.Logger;
import ji.files.text.Binary;
import ji.socketCommunication.SSL;
import ji.socketCommunication.SslCredentials;
import ji.socketCommunication.http.HttpMethod;

public class RestApiClient {
	
	private final String serverUrl;
	
	private final Logger logger;
	
	private final String charset;
	
	private final Binary binary;
	
	private final Optional<SslCredentials> ssl;
	
	public RestApiClient(String serverUrl, Optional<SslCredentials> ssl, String charset, Logger logger) {
		this(serverUrl, ssl, charset, logger, Binary.get());
	}
	
	protected RestApiClient(String serverUrl, Optional<SslCredentials> ssl, String charset, Logger logger, Binary binary) {
		this.serverUrl = serverUrl;
		this.logger = logger;
		this.charset = charset;
		this.ssl = ssl;
		this.binary = binary;
	}
	
	public RestApiResponse get(String uri, Properties header, Properties params) throws Exception {
		return send(uri, HttpMethod.GET, header, params);
	}
	
	public RestApiResponse post(String uri, Properties header, Properties params) throws Exception {
		return send(uri, HttpMethod.POST, header, params);
	}
	
	public RestApiResponse put(String uri, Properties header, Properties params) throws Exception {
		return send(uri, HttpMethod.PUT, header, params);
	}
	
	public RestApiResponse delete(String uri, Properties header, Properties params) throws Exception {
		return send(uri, HttpMethod.DELETE, header, params);
	}
	
	/**************/
	
	private RestApiResponse send(String uri, HttpMethod method, Properties header, Properties params) throws Exception {
		String url = createUrl(serverUrl, uri, method, params);
		HttpURLConnection con = getConnection(url, ssl);
		return send(con, method, header, params);
	}
	
	private HttpURLConnection getConnection(String url, Optional<SslCredentials> ssl) throws Exception {
		if (ssl.isPresent()) {
			return getSecuredConnection(url, ssl.get());
		} else {
			return getUnsecuredConnection(url);
		}
	}

	private HttpsURLConnection getSecuredConnection(String url, SslCredentials ssl) throws Exception {
		SSLContext sc = SSL.getSSLContext(ssl);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		//*
		if (ssl.trustAll()) {
			// trust all certs 2
			con.setHostnameVerifier(new HostnameVerifier() {
				@Override public boolean verify(String hostname, SSLSession session) { return true; }
			});
		} else if (ssl.useTrustedClients()) {
			con.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
		}
		/*/
		con.setHostnameVerifier(new HostnameVerifier() {
			@Override public boolean verify(String hostname, SSLSession session) {
				return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
			}
		});
		//*/
		return con;
	}
	
	private HttpURLConnection getUnsecuredConnection(String url) throws IOException {
		URL obj = new URL(url);
		return (HttpURLConnection) obj.openConnection();
	}
	
	/*************/
	
	protected RestApiResponse send(HttpURLConnection con, HttpMethod method, Properties header, Properties params) throws IOException {
		con.setRequestMethod(method.toString());
		header.forEach((name, value)->{
			con.setRequestProperty(name.toString(), value.toString());
		});
		
		addParams(method, params, con);
		
		int responseCode = con.getResponseCode();
		String resposeMessage = con.getResponseMessage();
		StringBuffer response = new StringBuffer();
		
		logger.debug("Response code: " + responseCode);
		logger.debug("Response message: " + resposeMessage);
				
		try (BufferedReader br  = new BufferedReader(new InputStreamReader(con.getInputStream(), charset))) {
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
		}
		return new RestApiResponse(responseCode, resposeMessage, response.toString());
	}

	/****************/
	
	private void addParams(HttpMethod method, Properties params, HttpURLConnection con) throws IOException {
		if (!HttpMethod.GET.equals(method)) {
			con.setDoOutput(true);
			binary.write((bos)->{
				bos.write(getParamsString(params).getBytes());
				bos.flush();
			}, con.getOutputStream());
		}
	}

	protected String createUrl(String server, String uri, HttpMethod method, Properties params) throws UnsupportedEncodingException {
		if (HttpMethod.GET.equals(method)) {
			return server + uri + "?" + getParamsString(params);
		}
		return server + uri;
	}
	
	// TODO test this method
	protected String getParamsString(Properties params) throws UnsupportedEncodingException {
		StringBuilder b = new StringBuilder();
		for (Object name : params.keySet()) {
			Object value = params.get(name);
			if (!b.toString().isEmpty()) {
				b.append("&");
			}
			b.append(String.format(
					"%s=%s",
					URLEncoder.encode(name + "", StandardCharsets.UTF_8.toString()),
					URLEncoder.encode(value + "", StandardCharsets.UTF_8.toString())/*
					UrlEscape.escapeText(name + ""), // + "" is fix, varialbe could be null
					UrlEscape.escapeText(value + "") // + "" is fix, varialbe could be null*/
			));
		}
		return b.toString();
	}
	
}
