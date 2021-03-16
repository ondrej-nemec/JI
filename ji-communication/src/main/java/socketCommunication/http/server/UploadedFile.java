package socketCommunication.http.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import core.text.Binary;

public class UploadedFile {

	private final String fileName;
	private final String contentType;
	private final ByteArrayOutputStream content;
	private final Binary binary;
	
	public UploadedFile(String fileName, String contentType, ByteArrayOutputStream content) {
		this(fileName, contentType, content, Binary.get());
	}
	
	protected UploadedFile(String fileName, String contentType, ByteArrayOutputStream content, Binary binary) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.content = content;
		this.binary = binary;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public ByteArrayOutputStream getContent() {
		return content;
	}
	
	public void save(String path) throws IOException {
		save(path, fileName);
	}
	
	public void save(String path, String name) throws IOException {
		if (!path.endsWith("/")) {
			path += "/";
		}
		binary.write((stream)->{
			stream.write(content.toByteArray());
			stream.flush();
		}, path + name);
	}
	
	@Override
	public String toString() {
		return String.format("File[name: %s, contentType: %s, size: %sb]", fileName, contentType, content.size());
	}
	
}
