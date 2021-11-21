package socketCommunication.http.server;

import java.io.IOException;
import java.util.Properties;

import socketCommunication.http.HttpMethod;

public interface RestApiServerResponseFactory {

	RestApiResponse accept(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			RequestParameters params,
			String ipAddress
	) throws IOException;
	
	RestApiResponse accept(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			RequestParameters params,
			String ipAddress,
			String host,
			WebSocket websocket
	) throws IOException;

	
	/*
	default RestApiResponse onException(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			Properties params,
			Session session,
			Throwable t
	) throws IOException {
		return null;
	}
*/
}
