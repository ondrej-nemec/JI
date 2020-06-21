package socketCommunication.http.server;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.structures.ThrowingBiConsumer;
import socketCommunication.http.StatusCode;

public class RestApiResponse {

	private final StatusCode statusCode;
	
	private final List<String> header;
	
	private final ThrowingBiConsumer<BufferedWriter, BufferedOutputStream, IOException> content;

	public RestApiResponse(
			StatusCode statusCode, List<String> header,
			ThrowingBiConsumer<BufferedWriter, BufferedOutputStream, IOException> content) {
		this.statusCode = statusCode;
		this.header = header;
		this.content = content;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public List<String> getHeader() {
		return header;
	}

	public void createContent(BufferedWriter bw, BufferedOutputStream bos) throws IOException {
		content.accept(bw, bos);
	}

}
