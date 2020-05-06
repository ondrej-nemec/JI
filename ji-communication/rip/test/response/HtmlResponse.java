package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import clientserver.StatusCode;

public class HtmlResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *",
				"Content-Type: text/html; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		bw.write("<!doctype html>");
		bw.write("<html lang='en'>");
		bw.write("<head>");
		bw.write("<meta charset='utf-8'>");
		bw.write("<link rel='icon' href='favicon.ico'>");
		bw.write("<link href='style.css' rel='stylesheet'>");
		bw.write("<title>Test</title>");
		bw.write("<script src='https://code.jquery.com/jquery-3.4.1.min.js'"
				+ " integrity='sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo='"
				+ " crossorigin='anonymous'></script>");
		bw.write("</head>");
		bw.write("<body>");
		bw.write("<h1>Testovaci stranka</h1>");
		bw.write("<div id='key'></div>");
		bw.write("<img src='icon.png' /><br>");
		bw.write("<a href='zip.zip' >zip.zip<a/>");
		bw.write("<script src='script.js' ></script>");
		bw.write("</body>");
		bw.write("</html>");
		bw.flush();
	}

}
