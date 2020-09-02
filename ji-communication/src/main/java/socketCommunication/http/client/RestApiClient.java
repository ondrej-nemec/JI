package socketCommunication.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import common.Logger;
import core.text.Binary;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.UrlEscape;

// TODO improve https://www.baeldung.com/java-http-request and https url request
// TODO https://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data
public class RestApiClient {
	
	private final String serverUrl;
	
	private final Logger logger;
	
	private final String charset;
	
	public RestApiClient(String serverUrl, String charset, Logger logger) {
		this.serverUrl = serverUrl;
		this.logger = logger;
		this.charset = charset;
	}
	
	public RestApiResponse get(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.GET, header, params);
	}
	
	public RestApiResponse post(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.POST, header, params);
	}
	
	public RestApiResponse put(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.PUT, header, params);
	}
	
	public RestApiResponse delete(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.DELETE, header, params);
	}
	
	private RestApiResponse send(String uri, HttpMethod method, Properties header, Properties params) throws IOException {
		String url = createUrl(serverUrl, uri, method, params);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		return send(con, method, header, params);
	}
	
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

	private void addParams(HttpMethod method, Properties params, HttpURLConnection con) throws IOException {
		if (!HttpMethod.GET.equals(method)) {
			con.setDoOutput(true);
			Binary.write((bos)->{
				StringBuilder b = new StringBuilder();
				params.forEach((name, value)->{
					if (!b.toString().isEmpty()) {
						b.append("&");
					}
					b.append(String.format(
							"%s=%s",
							UrlEscape.escapeText(name + ""), // + "" is fix, varialbe could be null
							UrlEscape.escapeText(value + "") // + "" is fix, varialbe could be null
					));
				});
				bos.write(b.toString().getBytes());
				bos.flush();
			}, con.getOutputStream());
			/*
			try (OutputStream os = con.getOutputStream();) {
				StringBuilder b = new StringBuilder();
				params.forEach((name, value)->{
					if (!b.toString().isEmpty()) {
						b.append("&");
					}
					b.append(String.format(
							"%s=%s",
							UrlEscape.escapeText(name + ""), // + "" is fix, varialbe could be null
							UrlEscape.escapeText(value + "") // + "" is fix, varialbe could be null
					));
				});
				os.write(b.toString().getBytes());
				os.flush();
			}*/
		}
	}

	protected String createUrl(String server, String uri, HttpMethod method, Properties params) {
		if (HttpMethod.GET.equals(method)) {
			StringBuilder b = new StringBuilder();
			params.forEach((name, value)->{
				if (b.toString().isEmpty()) {
				//	b.append(server);
				//	b.append(uri);
					b.append("?");
				} else {
					b.append("&");
				}
				b.append(String.format(
						"%s=%s",
						UrlEscape.escapeText(name + ""), // + "" is fix, varialbe could be null
						UrlEscape.escapeText(value + "") // + "" is fix, varialbe could be null
				));
			});
			return server + uri + b.toString();
		}
		return server + uri;
	}
}
