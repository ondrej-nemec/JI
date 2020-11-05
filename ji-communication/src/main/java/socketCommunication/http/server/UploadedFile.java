package socketCommunication.http.server;

import java.io.IOException;
import java.util.List;

import core.text.Binary;

public class UploadedFile {

	private final String fileName;
	private final String contentType;
	private final List<Byte> content;

	public UploadedFile(String fileName, String contentType, List<Byte> content) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public List<Byte> getContent() {
		return content;
	}
	
	public void save(String path) throws IOException {
		save(path, fileName);
	}
	
	public void save(String path, String name) throws IOException {
		Binary.write((stream)->{
			for (int i = 0; i  < content.size(); i++) {
				stream.write(content.get(i));
			}
		}, name);
	}
	
	@Override
	public String toString() {
		return String.format("File[name: %s, contentType: %s, size: %s]", fileName, contentType, content.size());
	}
	
}
