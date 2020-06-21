package endtoend.response;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import common.structures.ThrowingConsumer;
import socketCommunication.http.StatusCode;

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
/*
	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		byte[] bytes = new byte[15];
		try (InputStream is = getClass().getResourceAsStream("/endtoend/files/favicon.ico")) {
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
*/

	@Override
	public void write(BufferedWriter bw, BufferedOutputStream os) throws IOException {
		/*try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/endtoend/files/zip.zip")))) {
			String line = br.readLine();
			while (line != null) {
				bw.wri
			}
		}*/
		//*
		read((dis)->{
			byte[] cache = new byte[32];
			while (dis.read(cache) != -1) {
				os.write(cache);
			}
		}, getClass().getResourceAsStream("/endtoend/files/favicon.ico"));
		//*/
	}
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	// https://stackoverflow.com/a/9855338
	public static String hexBytesToString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[bytes.length -1 - j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	// https://stackoverflow.com/a/140861
	public static byte[] hexStringToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public static void read(ThrowingConsumer<DataInputStream, IOException> consumer, InputStream stream) throws IOException {
		try (DataInputStream dis = new DataInputStream(stream)) {
			consumer.accept(dis);
		}
	}

	public static void write(ThrowingConsumer<DataOutputStream, IOException> consumer, OutputStream stream) throws IOException {
		try (DataOutputStream dis = new DataOutputStream(stream)) {
			consumer.accept(dis);
		}
	}

	
}
