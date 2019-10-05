package clientserver.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import clientserver.Method;
import common.Logger;
import common.structures.ThrowingBiConsumer;

public class RestAPI implements ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> {
	
	private final static String METHOD = "method";
	private final static String PROTOCOL = "protocol";
	private final static String URL = "url";
	private final static String FULL_URL = "full-url";
	
	private final Logger logger;

	private final CreateRestAPIResponce response;
	
	public RestAPI(CreateRestAPIResponce response, Logger logger) {
		this.logger = logger;
		this.response = response;
	}
	
	@Override
	public void accept(BufferedReader br, BufferedWriter bw) throws IOException {
		Properties params = new Properties();
		Properties header = new Properties();
		Properties request = new Properties();
		parseRequest(request, header, params, br);
		
		logger.debug("Request: " + request);
		response.accept(
				Method.fromString(request.getProperty(METHOD)),
				request.getProperty(URL),
				request.getProperty(FULL_URL),
				request.getProperty(PROTOCOL),
				header,
				params
		);
		String code = response.getStatusCode().toString();
		logger.debug("Response: " + code);
		
		// write first line
		bw.write(request.getProperty(PROTOCOL));
        bw.write(" ");
        bw.write(code);
        bw.newLine();
		// write heared
		response.writeHeade(bw);
		// end of header
        bw.newLine();
		// write context
		response.writeContent(bw);
		bw.flush();
	}
	
	/********* PARSE **************/
	
	private void parseRequest(Properties request, Properties header, Properties params, BufferedReader br) throws IOException {
		// url, method, protocol
		String first = br.readLine();
		parseFirst(request, params, first);
		
        // header
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
        	parseHeaderLine(line, header);
        	line = br.readLine();
        }
        
        // payload
        StringBuilder payload = new StringBuilder();
        while(br.ready()){
        	payload.append((char) br.read());
        }
        parsePayload(params, payload.toString());
	}

	private void parseFirst(Properties request, Properties params, String first) {
		String[] methods = first.split(" ");
		if (methods.length != 3) {
			logger.warn("Invalid request: " + first);
			return; //TODO what now?
		}
		
		String[] urlParst = methods[1].split("\\?");
		if (urlParst.length > 1) {
			parsePayload(params, urlParst[1]);
		}		
		
		request.put(METHOD, methods[0]);
		request.put(URL, urlParst[0]);
		request.put(FULL_URL, methods[1]);
		request.put(PROTOCOL, methods[2]);
	}

	private void parseHeaderLine(String line, Properties header) {
		String[] property = line.split(": ", 2);
    	if (property.length == 2) {
    		header.put(property[0], property[1]);
    	} else {
    		logger.warn("Invalid line " + line);
    	}
	}

	private void parsePayload(Properties prop, String payload) {
		String[] params = payload.split("\\&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 1) {
				prop.put(keyValue[0], "");
			} else if (keyValue.length == 2) {
				prop.put(keyValue[0], keyValue[1]);
			} else {
	    		logger.warn("Invalid param " + param);
	    	}
		}
	}

}
