package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import clientserver.StatusCode;

public interface Response {
		
	StatusCode getCode();
	
	List<String> getHeader();
	
	void write(BufferedWriter bw, BufferedOutputStream os) throws IOException;
	
}
