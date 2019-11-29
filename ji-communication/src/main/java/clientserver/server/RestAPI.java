package clientserver.server;

import static common.MapInit.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import clientserver.HttpMethod;
import clientserver.server.restapi.CreateRestAPIResponce;
import clientserver.server.restapi.RestApiResponse;
import common.Logger;
import common.structures.ThrowingBiConsumer;

public class RestAPI implements ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
	
	private final static Map<String, String> ESCAPE = 
			//TODO
			hashMap(
					t("%3F", "?"),
					t("%2F", "/"),
					t("%5C", "\\\\"), // escaped \
					t("%3A", ":"),
					t("\\+", " "), // escaped +
					t("%3D", "="),
					t("%26", "&"),
					t("%25", "%")/*,
					t("", "\""),
					t("", "'"),
					t("", "+"),
					t("", "*")*/
			);
	
	private final Logger logger;

	private final CreateRestAPIResponce createResponce;
	
	public RestAPI(CreateRestAPIResponce response, Logger logger) {
		this.logger = logger;
		this.createResponce = response;
	}
	
	@Override
	public void accept(BufferedReader br, BufferedWriter bw) throws IOException {
		Properties params = new Properties();
		Properties header = new Properties();
		Properties request = new Properties();
		parseRequest(request, header, params, br);
		
		logger.debug("Request: " + request);
		RestApiResponse response = createResponce.accept(
				HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase()),
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
        for (String headerLine : response.getHeader()) {
        	bw.write(headerLine);
        	bw.newLine();
        }
		// end of header
        bw.newLine();
		// write context
        bw.write(response.getMessage());
        bw.newLine();
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

	/** protected for test only */
	protected void parseFirst(Properties request, Properties params, String first) {
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

	/** protected for test only */
	protected void parseHeaderLine(String line, Properties header) {
		if (line.isEmpty()) {
			return;
		}
		String[] property = line.split(": ", 2);
    	if (property.length == 2 && ! property[0].isEmpty()) {
    		header.put(property[0], property[1]);
    	} else if (property.length == 1){
    		header.put(property[0], "");
    	} else {
    		logger.warn("Invalid header line " + line);
    	}
	}

	/** protected for test only */
	protected void parsePayload(Properties prop, String payload) {
		if (payload.isEmpty()) {
			return;
		}
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
	
	/** protected for test only */
	protected String unEscapeText(String text) {
		for (String key : ESCAPE.keySet()) {
			text = text.replaceAll(key, ESCAPE.get(key));
		}
		return text;
	}

}
