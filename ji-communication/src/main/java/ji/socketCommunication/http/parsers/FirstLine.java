package ji.socketCommunication.http.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.StatusCode;
import ji.socketCommunication.http.structures.Exchange;
import ji.socketCommunication.http.structures.Protocol;
import ji.socketCommunication.http.structures.Request;
import ji.socketCommunication.http.structures.Response;

public class FirstLine {
	
	private final StreamReader stream;
	
	public FirstLine(StreamReader stream) {
		this.stream = stream;
	}
	
	public void write(Exchange exchange, OutputStream bos) throws IOException {
		bos.write(exchange.getFirstLine().getBytes());
		bos.write(stream.getNewLine()); // end of first line
	}

	// response: HTTP/1.1 200 OK
	public Response createResponse(InputStream bis) throws IOException {
		return create(bis, (methods)->new Response(
			StatusCode.valueOf(methods[2].toUpperCase().replace(" ", "_").replace("-", "_")),
			Protocol.valueOf(methods[0])
		));
	}

	// request: POST /cgi-bin/process.cgi HTTP/1.1
	public Request createRequest(InputStream bis) throws IOException {
		return create(bis, (methods)->new Request(
			HttpMethod.valueOf(methods[0].toUpperCase()), 
			methods[1], 
			Protocol.valueOf(methods[2])
		));
	}
	
	private <T> T create(InputStream bis, Function<String[], T> create) throws IOException {
		String first = stream.readLine(bis);
	//	System.err.println(Thread.currentThread().getName() + " First: " + first);
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
