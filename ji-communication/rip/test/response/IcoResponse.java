package test.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import clientserver.StatusCode;

public class IcoResponse implements Response {

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *",
				"Content-Type: image/x-icon; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		byte[] bytes = new byte[15];
		try (InputStream is = new FileInputStream("res/favicon.ico")) {
			int length = is.read(bytes);
			while (length != 0) {
				os.write(bytes, 0, length);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
