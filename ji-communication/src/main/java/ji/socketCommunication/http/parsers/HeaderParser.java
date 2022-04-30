package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import ji.socketCommunication.http.Exchange;

public interface HeaderParser extends Stream {

	default void writeHeaders(Exchange exchange, BufferedOutputStream bos) throws IOException {
		// TODO add missing heades content length and type?
		for (Entry<String, List<Object>> header : exchange.getHeaders().entrySet()) {
			for (Object headerValue : header.getValue()) {
				bos.write('\n');// bw.newLine();
				bos.write(String.format("%s: %s", header.getKey(), headerValue).getBytes());
			}
        }
		if (exchange.getBody() != null) {
			bos.write('\n');// bw.newLine();
		}
	}
	
	default List<String> readHeaders(Exchange exchange, BufferedInputStream bis) throws IOException {
		List<String> errors = new LinkedList<>();
		String line = readLine(bis);
        while (line != null && !line.isEmpty()) {
        	if (line.isEmpty()) { // TODO is required?
    			return errors;
    		}
    		String[] property = line.split(": ", 2);
        	if (property.length == 2 && ! property[0].isEmpty()) {
        		exchange.addHeader(property[0], property[1]);
        	} else if (property.length == 1){
        		exchange.addHeader(property[0], "");
        	} else {
        		errors.add("Invalid header line " + line);
        	}
        	line = readLine(bis);
        }
        return errors;
	}
}
