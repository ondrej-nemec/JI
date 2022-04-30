package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

public interface Stream {
	
	default String readLine(BufferedInputStream bis) throws IOException {
		ByteArrayOutputStream data = readData(null, bis, 0, (actual)->{
			return actual == '\n';
		}, true);
		if (data.size() == 0) {
			return null;
		}
		return new String(data.toByteArray());
	}

	default ByteArrayOutputStream readData(Integer length, BufferedInputStream bis, 
			int readedBytes, Function<Integer, Boolean> close, boolean ignoreNewLine) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		if (length == null) {
			if (bis.available() > 0) {
		        int value;
		        while((value = bis.read()) != -1) {
		        	if ( ! (ignoreNewLine && (value == '\n' || value == '\r'))) {
		        		 bytes.write(value);
		        	}
		            if (bis.available() == 0 || close.apply(value)) {
		               return bytes;
		            }
		        }
		    }
		} else {
			int readed = readedBytes;
			while (readed < length) {
				int value = bis.read();
				if ( ! (ignoreNewLine && (value == '\n' || value == '\r'))) {
	        		 bytes.write(value);
	        		 readed = bytes.size();
	        	}
				/*if (bis.available() == 0 && length != readed + readedBytes) {
					System.out.println("---> " + length + " " + (readed + readedBytes));
	            	throw new IOException("Content length not match expecation. Expected: " + length + ", actual: " + (readed + readedBytes));
	            }*/
	            if (close.apply(value)) {
	            	return bytes;
	            }
			}
		}
		return bytes;
	}
}
