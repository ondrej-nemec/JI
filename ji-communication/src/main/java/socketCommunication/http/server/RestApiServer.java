package socketCommunication.http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import common.Logger;
import socketCommunication.Servant;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.UrlEscape;

public class RestApiServer implements Servant {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
		
	private final Logger logger;

	private final RestApiServerResponseFactory createResponce;
	
	public RestApiServer(RestApiServerResponseFactory response, Logger logger) {
		this.logger = logger;
		this.createResponce = response;
	}
	
	@Override
	public void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException {
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
        response.createContent(bw, os); // bw.write(response.getMessage());
        bw.newLine();
        os.flush();
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
        int value;
        while((value = br.read()) != -1) {
            payload.append((char) value);
            if (!br.ready()) {
                break;
            }
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
		//TODO escape payload
		String[] params = payload.split("\\&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 1) {
				prop.put(UrlEscape.unEscapeText(keyValue[0]), "");
			} else if (keyValue.length == 2) {
				prop.put(UrlEscape.unEscapeText(keyValue[0]), UrlEscape.unEscapeText(keyValue[1]));
			} else {
	    		logger.warn("Invalid param " + param);
	    	}
		}
	}

}
