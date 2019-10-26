package clientserver.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import clientserver.HttpMethod;
import clientserver.StatusCode;

public interface CreateRestAPIResponce {

	
	void accept(HttpMethod method, String url, String fullUrl, String protocol, Properties header, Properties params);
	
	StatusCode getStatusCode();
	
	void writeHeade(BufferedWriter bw) throws IOException;
	
	void writeContent(BufferedWriter bw) throws IOException;
	
}
