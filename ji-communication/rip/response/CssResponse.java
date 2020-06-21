package endtoend.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import socketCommunication.http.StatusCode;

public class CssResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *",
				"Content-Type: text/css; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		bw.write("h1 {");
		bw.write("color: blue;");
		bw.write("}");
		bw.flush();
	}

}
