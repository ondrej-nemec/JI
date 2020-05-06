package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import clientserver.StatusCode;

public class NullResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.INTERNAL_SERVER_ERROR;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {}

}
