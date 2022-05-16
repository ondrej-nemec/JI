package ji.socketCommunication.http.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

public class StreamReader {
	
	private final Integer maxBodySize;
	
	public StreamReader(Integer maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	public void write(byte[] bytes, OutputStream os) throws IOException {
		os.write(bytes);
	}
	
	public void write(int b, OutputStream os) throws IOException {
		os.write(b);
	}
	
	public String readLine(InputStream bis, boolean forceRead) throws IOException {
		byte[] data = readData(null, bis, 0, (actual)->{
			return actual == '\n';
		}, true, forceRead);
		if (data.length == 0) {
			return null;
		}
		return new String(data);
	}

	public byte[] readData(Integer length, InputStream bis, 
			int readedBytes, Function<Integer, Boolean> close, boolean ignoreNewLine, boolean forceRead) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		if (length == null) {
			if (bis.available() > 0 || forceRead) { // always 0 before read start
		        int value;
		        while((value = bis.read()) != -1) {
		        	if (maxBodySize != null && bytes.size() + readedBytes > maxBodySize) {
		        		throw new IOException("Maximal body size overflow. Max size (b) " + maxBodySize);
		        	}
		        	if ( ! (ignoreNewLine && (value == '\n' || value == '\r'))) {
		        		 bytes.write(value);
		        	}
		            if (bis.available() == 0 || close.apply(value)) {
		               return bytes.toByteArray();
		            }
		        }
		    }
		} else {
			int readed = readedBytes;
			while (readed < length) {
				if (maxBodySize != null && readed > maxBodySize) {
					throw new IOException("Maximal body size overflow. Max size (b) " + maxBodySize);
				}
				int value = bis.read();
				if ( ! (ignoreNewLine && (value == '\n' || value == '\r'))) {
	        		 bytes.write(value);
	        		 readed = bytes.size();
	        	}
				if (bis.available() == 0 && length != readed + readedBytes && value != -1) {
	            	throw new IOException("Content length not match expecation. Expected: " + length + ", actual: " + (readed + readedBytes));
	            }
	            if (close.apply(value)) {
	            	return bytes.toByteArray();
	            }
			}
		}
		return bytes.toByteArray();
	}
}
