package clientserver.server;

import java.io.BufferedWriter;
import java.util.Properties;

import clientserver.Method;
import clientserver.StatusCode;

public interface CreateRestAPIResponce {

	
	void accept(Method method, String url, String fullUrl, String protocol, Properties header, Properties params);
	
	StatusCode getStatusCode();
	
	void writeHeade(BufferedWriter bw);
	
	void writeContent(BufferedWriter bw);
	
}
