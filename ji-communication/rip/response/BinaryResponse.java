package endtoend.response;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import common.structures.ThrowingConsumer;
import core.text.Binary;
import socketCommunication.http.StatusCode;

public class BinaryResponse implements Response {
	
	private final InputStream input;
	
	public BinaryResponse(InputStream input) {
		this.input = input;
	}

	@Override
	public StatusCode getCode() {
		return StatusCode.OK;
	}

	@Override
	public List<String> getHeader() {
		return Arrays.asList(
				"Access-Control-Allow-Origin: *"
				// ,"Content-Type: application/zip; charset=utf-8"
		);
	}

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		/*try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/endtoend/files/zip.zip")))) {
			String line = br.readLine();
			while (line != null) {
				bw.wri
			}
		}*/
		//*
		Binary.read((dis)->{
			byte[] cache = new byte[32];
			while (dis.read(cache) != -1) {
				os.write(cache);
			}
			os.flush();
		}, input);
		//*/
	}

}
