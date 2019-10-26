package clientserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import clientserver.HttpMethod;
import common.Logger;

public class RestApiClient {
	
	private final String serverUrl;
	
	private final Logger logger;
	
	private final String charset;
	
	public RestApiClient(String serverUrl, String charset, Logger logger) {
		this.serverUrl = serverUrl;
		this.logger = logger;
		this.charset = charset;
	}
	
	public RestAPIResponse get(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.GET, header, params);
	}
	
	public RestAPIResponse post(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.POST, header, params);
	}
	
	public RestAPIResponse put(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.PUT, header, params);
	}
	
	public RestAPIResponse delete(String uri, Properties header, Properties params) throws IOException {
		return send(uri, HttpMethod.DELETE, header, params);
	}
	
	private RestAPIResponse send(String uri, HttpMethod method, Properties header, Properties params) throws IOException {
		String url = createUrl(serverUrl, uri, method, params);		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
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
		return new RestAPIResponse(responseCode, resposeMessage, response.toString());
	}

	private void addParams(HttpMethod method, Properties params, HttpURLConnection con) throws IOException {
		if (!HttpMethod.GET.equals(method)) {
			con.setDoOutput(true);
			try (OutputStream os = con.getOutputStream();) {
				StringBuilder b = new StringBuilder();
				params.forEach((name, value)->{
					b.append(String.format("%s=%s", name, value));
				});
				
				os.write(b.toString().getBytes());
				os.flush();
			}
		}
	}

	private String createUrl(String server, String uri, HttpMethod method, Properties params) {
		if (HttpMethod.GET.equals(method)) {
			StringBuilder b = new StringBuilder();
			params.forEach((name, value)->{
				if (b.toString().isEmpty()) {
					b.append(server);
					b.append(uri);
					b.append("?");
				} else {
					b.append("&");
				}
				b.append(String.format("%s=%s", name, value));
			});
			return b.toString();
		}
		return server;
	}
}
