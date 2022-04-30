package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.function.Function;

import ji.socketCommunication.http.Exchange;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.ParsingException;
import ji.socketCommunication.http.Request;
import ji.socketCommunication.http.Response;
import ji.socketCommunication.http.StatusCode;

public interface FirstLineParser extends Stream {
	
	default void writeFirstLine(Exchange exchange, BufferedOutputStream bos) throws IOException {
		bos.write(exchange.getFirstLine().getBytes());
	}

	// response: HTTP/1.1 200 OK
	default Response createResponse(BufferedInputStream bis) throws IOException {
		return create(bis, (methods)->new Response(
			StatusCode.valueOf(methods[2].toUpperCase().replace(" ", "_").replace("-", "_")),
			methods[0]
		));
	}

	// request: POST /cgi-bin/process.cgi HTTP/1.1
	default Request createRequest(BufferedInputStream bis) throws IOException {
		return create(bis, (methods)->new Request(
			HttpMethod.valueOf(methods[0].toUpperCase()), 
			methods[1], 
			methods[2]
		));
	}
	
	default <T> T create(BufferedInputStream bis, Function<String[], T> create) throws IOException {
		String first = readLine(bis);
		if (first == null) {
			throw new ParsingException("Wrong request - empty first line.");
		}
		String[] methods = first.split(" ", 3);
		if (methods.length != 3) {
			throw new ParsingException("Invalid request: " + first); // what now?
		}
		return create.apply(methods);
	}
}
