package ji.socketCommunication.http.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import ji.common.Logger;
import ji.socketCommunication.http.ApiRequest;

public class HeaderParser {
	
	private final Logger logger;
	
	public HeaderParser(Logger logger) {
		this.logger = logger;
	}

	public void writeHeaders(ApiRequest request, BufferedWriter bw) throws IOException {
		for (Entry<String, List<Object>> header : request.getHeaders().entrySet()) {
			for (Object headerValue : header.getValue()) {
				bw.write(String.format("%s: %s", header.getKey(), headerValue));
	        	bw.newLine();
			}
        }
		// end of header
        bw.newLine();
        bw.flush();
	}
	
	public void readHeaders(ApiRequest request, BufferedReader br) throws IOException {
		String line = br.readLine();
        while (line != null && !line.isEmpty()) {
        	parseHeaderLine(line, request);
        	line = br.readLine();
        }
	}

	private void parseHeaderLine(String line, ApiRequest request) {
		if (line.isEmpty()) {
			return;
		}
		String[] property = line.split(": ", 2);
    	if (property.length == 2 && ! property[0].isEmpty()) {
    		request.addHeader(property[0], property[1]);
    	} else if (property.length == 1){
    		request.addHeader(property[0], "");
    	} else {
    		logger.warn("Invalid header line " + line);
    	}
	}
}
