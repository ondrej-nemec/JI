package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import clientserver.StatusCode;

public class JsonResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *",
				"Content-Type: application/json; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		bw.write("{");
		bw.write("\"key\": \"value\"");
		bw.write("}");
		bw.flush();
	}
}
