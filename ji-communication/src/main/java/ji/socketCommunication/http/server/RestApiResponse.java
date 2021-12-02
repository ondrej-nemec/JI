package ji.socketCommunication.http.server;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import ji.common.structures.ThrowingConsumer;
import ji.socketCommunication.http.StatusCode;

public class RestApiResponse {

	private final StatusCode statusCode;
	
	private final List<String> header;
	
	private final ThrowingConsumer<BufferedOutputStream, IOException> binaryContent;
	private final ThrowingConsumer<BufferedWriter, IOException> textContent;

	public static RestApiResponse binaryResponse(
			StatusCode statusCode, List<String> header,
			ThrowingConsumer<BufferedOutputStream, IOException> binaryContent) {
		return new RestApiResponse(statusCode, header, null, binaryContent);
	}
	
	public static RestApiResponse textResponse(StatusCode statusCode, List<String> header,
			ThrowingConsumer<BufferedWriter, IOException> textContent) {
		return new RestApiResponse(statusCode, header, textContent, null);
	}
	
	public static RestApiResponse webSocketResponse(List<String> header, WebSocket websocket,
			Consumer<String> onMessage, Consumer<IOException> onError) {
		List<String> responseHeaders = new LinkedList<>();
		responseHeaders.addAll(header);
		responseHeaders.addAll(websocket.getHeaders());
		return new RestApiResponse(
			StatusCode.SWITCH_PROTOCOL,
			responseHeaders,
			null, 
			(os)->{
				websocket.accept(onMessage, onError);
			}
		);
	}
	
	private RestApiResponse(
			StatusCode statusCode, List<String> header,
			ThrowingConsumer<BufferedWriter, IOException> textContent,
			ThrowingConsumer<BufferedOutputStream, IOException> binaryContent) {
		this.statusCode = statusCode;
		this.header = header;
		this.textContent = textContent;
		this.binaryContent = binaryContent;
	}
	
	public StatusCode getStatusCode() {
		return statusCode;
	}

	public List<String> getHeader() {
		return header;
	}
	
	public void createBinaryContent(BufferedOutputStream bos) throws IOException {
		if (binaryContent != null) {
			binaryContent.accept(bos);
			bos.flush();
		}
	}
	
	public void createTextContent(BufferedWriter bw)  throws IOException {
		if (textContent != null) {
			textContent.accept(bw);
		}
		bw.flush();
	}

}
