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
			Properties params
	) throws IOException;

}
