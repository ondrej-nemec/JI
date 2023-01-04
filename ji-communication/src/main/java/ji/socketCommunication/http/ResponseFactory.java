package ji.socketCommunication.http;

import java.io.IOException;
import java.util.Optional;

import ji.socketCommunication.http.structures.Request;
import ji.socketCommunication.http.structures.Response;
import ji.socketCommunication.http.structures.WebSocket;

public interface ResponseFactory {

	Response accept(Request request, String ipAddress, Optional<WebSocket> webSocket) throws IOException;
	
}
