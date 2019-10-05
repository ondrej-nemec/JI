package clientserver.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import clientserver.Method;
import clientserver.StatusCode;

public interface CreateRestAPIResponce {

	
	void accept(Method method, String url, String fullUrl, String protocol, Properties header, Properties params);
	
	StatusCode getStatusCode();
	
	void writeHeade(BufferedWriter bw) throws IOException;
	
	void writeContent(BufferedWriter bw) throws IOException;
	
}
