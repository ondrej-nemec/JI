package socketCommunication.http.server;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.structures.ThrowingConsumer;
import socketCommunication.http.StatusCode;

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
		}
	}
	
	public void createTextContent(BufferedWriter bw)  throws IOException {
		if (textContent != null) {
			textContent.accept(bw);
		}
	}

}
