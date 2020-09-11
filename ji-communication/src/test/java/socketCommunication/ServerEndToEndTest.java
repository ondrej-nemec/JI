package socketCommunication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import common.Console;
import core.text.Binary;
import socketCommunication.Server;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.StatusCode;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.session.MemorySessionStorage;
import socketCommunication.http.server.session.Session;
import socketCommunication.http.server.RestApiResponse;

public class ServerEndToEndTest {
	
	public final static Console console = new Console();

	public static void main(String[] args) {
		try {
			//*	    	
			ServerSecuredCredentials cred = new ServerSecuredCredentials(
					"servercerts/keystore.jks",
					"abc123",
					Optional.empty(),
					Optional.empty()
			);
			
			Server server = Server.create(
					10123,
					5,
					120000,
					120000,
					apiResponse(),
					new MemorySessionStorage(),
					/*
					Optional.empty(),
					/*/
					Optional.of(cred),
					//*/
					"UTF-8",
					new LoggerImpl()
			);
			/*/
			Server server = Server.create(10123, 5, 60000, speakerFunction(), "UTF-8", new LoggerImpl());
			//*/
			
			server.start();
			for (int i = 0; i < 30; i++) {
                Thread.sleep(i * 10000);
            //    console.out("Server running");
            }
            server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static RestApiServerResponseFactory apiResponse() {
		return new RestApiServerResponseFactory() {

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, Properties params, Session session) throws IOException {
				/*if ("1".equals(header.get("Upgrade-Insecure-Requests"))) {
					return getCert();
				}*/
				if (url.equals("/redirect")) {
					return getRedirect();
				}
				if (url.equals("/favicon.ico")) {
					return getBinaryFile("favicon.ico", "image/ico");
				}
				if (url.equals("/final.gif")) {
					return getBinaryFile("final.gif", "image/gif");
				}
				return getHtml();
			}
			
			private RestApiResponse getRedirect() {
				return RestApiResponse.binaryResponse(
						StatusCode.TEMPORARY_REDIRECT,
						Arrays.asList(
								"Access-Control-Allow-Origin: *",
								"Content-Type: image/ico; charset=utf-8",
								"X-XSS-Protection: 1; mode=block",
								"Location: /test"
						),
						(bos)->{}
					);
			}
			private RestApiResponse getBinaryFile(String fileName, String contentType) {
				return RestApiResponse.binaryResponse(
					StatusCode.OK,
					Arrays.asList(
							// "Content-length: ",
							"Access-Control-Allow-Origin: *",
							"Content-Type: " + contentType, // ; charset=utf-8
							"X-XSS-Protection: 1; mode=block"
					),
					(bos)->{
							Binary.read((dis)->{
								/*
								int c;
								while ((c = dis.read()) != -1) {
									bos.write(c);
								}
								/*/
								byte[] cache = new byte[2048 * 8];
								int len;
								while ((len = dis.read(cache, 0, cache.length)) != -1) {
								//	System.out.println(len);
									bos.write(cache, 0, len);
								}
							//	bos.flush();
								//*/
							}, getClass().getResourceAsStream("/restapiserver/" + fileName));
					}
				);
			}
			
			private RestApiResponse getHtml() {
				Date today = new Date();
				return RestApiResponse.textResponse(
					StatusCode.OK,
					Arrays.asList(
							"Access-Control-Allow-Origin: *", 
							"Content-Type: text/html; charset=utf-8",
							"X-XSS-Protection: 1; mode=block"
						//	"Set-Cookie: Test=susenka"//,
						//	"Set-Cookie: jina=cookie"
					),
					(bw)->{
						bw.write(String.format(
								"<html> <head></head><body><h1>Time</h1>%s"
								+ "<br><form><label for='name'></label><input type='text' name='name'/>"
								+ "<input type='submit' value='submit'></form>"
								+ "<br><br>"
								+ "<img src='final.gif'/>"
								+ "<br>"
								+ "<img src='favicon.ico'/>"
								+ "<br>"
								+ "<form method=\"post\" enctype=\"multipart/form-data\" action='/file'>"
								+ "Select image to upload:"
								+ " <input type=\"file\" name=\"fileToUpload\" id=\"fileToUpload\">"
								+ "<input type=\"submit\" value=\"Upload Image\" name=\"submit\">"
								+ "</form>"
								+ "</body></html>", today.toString()
						));
					}
				);
			}

		};
	}
	
	protected static Function<String, String> speakerFunction() {
		return (message)-> {
			console.out("Message " + message);			
            return "Received: " + message;
        };
	}
}
