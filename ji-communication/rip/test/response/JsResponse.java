package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import clientserver.StatusCode;

public class JsResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *",
				"Content-Type: application/javascript; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		bw.write("$(document).ready(function() {");
		bw.write("$.ajax({");
		bw.write("url: \"index.php\",");
		bw.write("success: function(data) {");
		bw.write("$('#key').text(data['key']);");
		bw.write("}");
		bw.write("});");
		bw.write("});");
		bw.flush();
	}

}
