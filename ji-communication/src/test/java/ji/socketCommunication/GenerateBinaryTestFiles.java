package ji.socketCommunication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import ji.common.structures.ThrowingConsumer;

public class GenerateBinaryTestFiles {

	public static void main(String[] args) {
	
		generateBinary("binary/body-binary-req.txt", "POST /some/url HTTP/1.1");
		generateBinary("binary/body-binary-res.txt", "HTTP/1.1 200 OK");
		
		generateFormData("binary/body-multipart-with-file-req.txt", "POST /some/url HTTP/1.1");
		generateFormData("binary/body-multipart-with-file-res.txt", "HTTP/1.1 200 OK");
	}
	
	protected static void generateFormData(String filename, String firstLine) {
		generate(
			filename,
			firstLine,
			Arrays.asList(
				"Some-header: my header value",
				"Content-Length: 516",
				"Content-Type: multipart/form-data; boundary=item-separator"
			), 
			(os)->{
				os.write("--item-separator\r\n".getBytes());
				os.write("Content-Disposition: form-data; name=\"another\"\r\n".getBytes());
				os.write("\r\n".getBytes());
				os.write("value\r\n".getBytes());
				os.write("--item-separator\r\n".getBytes());
				os.write("Content-Disposition: form-data; name=\"fileinput\"; filename=\"filename.xyz\"\r\n".getBytes());
				os.write("Content-Type: IMG\r\n".getBytes());
				os.write("\r\n".getBytes());
				writeBinaryFile(os);
				os.write("\r\n".getBytes());
				os.write("--item-separator--".getBytes());
			}
		);
	}
	
	protected static void generateBinary(String filename, String firstLine) {
		generate(
			filename,
			firstLine,
			Arrays.asList(
				"Some-header: my header value",
				"Content-Length: 317",
				"Content-Type: image/x-icon"
			), 
			(os)->writeBinaryFile(os)
		);
	}
	
	private static void generate(
			String filename, String firstLine, List<String> headers, 
			ThrowingConsumer<OutputStream, IOException> body) {
		try (OutputStream os = new FileOutputStream(filename)) {

			os.write(firstLine.getBytes());
			os.write('\r');
			os.write('\n');
			for (String header : headers) {
				os.write(header.getBytes());
				os.write('\r');
				os.write('\n');
			}
			os.write('\r');
			os.write('\n');
			
			body.accept(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// file is 317
	private static void writeBinaryFile(OutputStream os) throws IOException {
		try(InputStream is = new FileInputStream("binary/icon.png")) {
			int i;
			while((i = is.read()) != -1) {
				os.write(i);
			}
		}
		
	}
	
}
