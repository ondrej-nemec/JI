package ji.socketCommunication.http.server;

import java.io.IOException;
import java.util.Optional;

import ji.socketCommunication.http.Request;
import ji.socketCommunication.http.Response;
import ji.socketCommunication.http.WebSocket;

public interface ResponseFactory {

	Response accept(Request request, String ipAddress, Optional<WebSocket> webSocket) throws IOException;

	default void catchException(Exception e) throws IOException {
		throw new IOException(e);
	}
	
}
