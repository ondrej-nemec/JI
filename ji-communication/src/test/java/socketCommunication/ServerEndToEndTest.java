package socketCommunication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
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
			Server server = Server.create(
					10123,
					5,
					60000,
					120000,
					apiResponse(),
					new MemorySessionStorage(), 
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
					return getFavicon();
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
			private RestApiResponse getFavicon() {
				return RestApiResponse.binaryResponse(
					StatusCode.OK,
					Arrays.asList(
							"Access-Control-Allow-Origin: *",
							"Content-Type: image/ico; charset=utf-8",
							"X-XSS-Protection: 1; mode=block"
					),
					(bos)->{
							Binary.read((dis)->{
								byte[] cache = new byte[32];
								while (dis.read(cache) != -1) {
									bos.write(cache);
								}
							}, getClass().getResourceAsStream("/certs/favicon.ico"));
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
								+ "<br><br><br>"
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
