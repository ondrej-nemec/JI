package clientserver.server.restapi;

import java.io.IOException;
import java.util.Properties;

import clientserver.HttpMethod;

public interface CreateRestAPIResponce {

	RestApiResponse accept(
			HttpMethod method,
			String url,
			String fullUrl,
			String protocol,
			Properties header,
			Properties params
	) throws IOException;

}
