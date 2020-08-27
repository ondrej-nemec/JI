package socketCommunication.http.server;

import java.io.IOException;
import java.util.Properties;

import socketCommunication.http.HttpMethod;
import socketCommunication.http.server.session.Session;

public interface RestApiServerResponseFactory {

	RestApiResponse accept(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			Properties params,
			Session session
	) throws IOException;
	
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

}
