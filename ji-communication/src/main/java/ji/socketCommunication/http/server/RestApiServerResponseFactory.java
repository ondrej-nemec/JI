package ji.socketCommunication.http.server;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import ji.socketCommunication.http.HttpMethod;

public interface RestApiServerResponseFactory {

	RestApiResponse accept(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			RequestParameters params,
			String ipAddress,
			Optional<WebSocket> webSocket
	) throws IOException;

	default void catchException(Exception e) throws IOException {
		throw new IOException(e);
	}
	
	
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
