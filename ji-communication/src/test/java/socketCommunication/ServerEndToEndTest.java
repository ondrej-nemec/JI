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
import socketCommunication.http.server.RequestParameters;
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
					80,
					5,
					120000,
					apiResponse(),
					//*
					Optional.empty(),
					/*/
					Optional.of(cred),
					//*/
					10 * 1024, // 10 kB
					Optional.empty(),
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
					Properties header, RequestParameters params, String ip) throws IOException {
				System.err.println("Params");
				params.forEach((key, value)->{
					System.err.println(key + ": " + value);
				});
				System.err.println();
				/*if ("1".equals(header.get("Upgrade-Insecure-Requests"))) {
					return getCert();
				}*/
				if (url.equals("/secured")) {
					return getSecured();
				}
				if (url.equals("/redirect")) {
					return getRedirect();
				}
				if (url.equals("/favicon.ico")) {
					return getBinaryFile("favicon.ico", "image/ico");
				}
				if (url.equals("/file")) {
					return getFile();
				}
				if (url.equals("/final.gif")) {
					return getBinaryFile("final.gif", "image/gif");
				}
				return getHtml();
			}
			
			private RestApiResponse getFile() {
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
							bw.write(
									"<html> <head></head><body>"
									+ "<img src='favicon.ico'/>"
									+ "</body></html>"
							);
						}
					);
			}

			private RestApiResponse getRedirect() {
				return RestApiResponse.binaryResponse(
						StatusCode.TEMPORARY_REDIRECT,
						Arrays.asList(
								"Access-Control-Allow-Origin: *",
								"Content-Type: image/ico; charset=utf-8",
								"X-XSS-Protection: 1; mode=block",
								"Access-Control-Allow-Credentials: true",
								"Location: /test"
						),
						(bos)->{}
					);
			}
			
			private RestApiResponse getSecured() {
				return RestApiResponse.binaryResponse(
						StatusCode.forCode(401),
						Arrays.asList(
								"Access-Control-Allow-Origin: *",
								"Content-Type: image/ico; charset=utf-8",
								"X-XSS-Protection: 1; mode=block",
								"WWW-Authenticate: basic realm=\"User Visible Realm\""
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
								+ "<br><form method='post' action='?aaa=asd *;'>"
								+ "<label for='name'></label><input type='text' name='name'/>"
								+ "<label for='name2'></label><input type='text' name='name2'/>"
								+ "<input type='submit' value='submit'></form>"
								+ "<br><br>"
								//+ "<img src='final.gif'/>"
								+ "<br>"
								//+ "<img src='favicon.ico'/>"
								+ "<br>"
								+ "<form method=\"post\" enctype=\"multipart/form-data\" action='/file'>"
								+ "Fill the name"
								+ "<input type='number' name='some-form-number'/><br>"
								+ "Select image to upload:"
								+ " <input type=\"file\" name=\"fileToUpload\" id=\"fileToUpload-id\"> <br>"
								+ "Fill the name"
								+ "<input type='text' name='some-form-text'/><br>"
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
