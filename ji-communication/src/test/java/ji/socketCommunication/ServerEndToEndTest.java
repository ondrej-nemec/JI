package ji.socketCommunication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ji.common.functions.Console;
import ji.files.text.Binary;
import ji.files.text.Text;
import ji.files.text.basic.ReadText;
import ji.socketCommunication.Servant;
import ji.socketCommunication.Server;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.StatusCode;
import ji.socketCommunication.http.server.RequestParameters;
import ji.socketCommunication.http.server.RestApiResponse;
import ji.socketCommunication.http.server.RestApiServerResponseFactory;
import ji.socketCommunication.http.server.UploadedFile;
import ji.socketCommunication.http.server.WebSocket;

public class ServerEndToEndTest {
	
	public final static Console console = new Console();
	private static final ExecutorService pool = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		try {
			SslCredentials ssl = new SslCredentials();
			
		//	ssl.setCertificateStore("certificates/test2/JKS-server-certificate.jks", "12345678");
			ssl.setCertificateStore("certificates/test3/jks-server-certificate.jks", "123456");
			//ssl.setTrustAll(false);
			//ssl.setTrustedClientsStore("certificates/test2/JKS-server-accept.jks", "12345678");
			
			/*
			Optional<SslCredentials> cred = Optional.empty();
			int port = 80;
			/*/
			Optional<SslCredentials> cred = Optional.of(ssl);
			int port = 443;
			//*/
			
			//*	
			Server server = Server.createWebServer(
					port,
					5,
					120000,
					apiResponse(),
					cred,
					10 * 1000 * 1000, // 10 MB
					Optional.empty(),
					"UTF-8",
					new LoggerImpl()
			);
			/*/
			Server server = new Server(
					port, 
					5,
					60000,
					speakerFunction(), 
					cred,
					"UTF-8",
					new LoggerImpl()
			);
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
				if (url.equals("/ping")) {
					return getText("OK");
				}
				System.err.println("Params:");
				System.err.println("> Body: " + params.getPlainBody());
				params.forEach((key, value)->{
					System.err.println("* " + key + ": " + value);
				});
				System.err.println();
				/*if ("1".equals(header.get("Upgrade-Insecure-Requests"))) {
					return getCert();
				}*/
				if (url.equals("/websocket")) {
					return getFilePage("index/web-sockets.html");
				}
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
					System.err.println("File to upload");
					System.err.println(params.getValue("fileToUpload"));
					((UploadedFile)params.getValue("fileToUpload")).save("index");
					return getFile();
				}
				if (url.equals("/final.gif")) {
					return getBinaryFile("final.gif", "image/gif");
				}
				if (url.equals("/fileindex")) {
					return getFilePage("index/index.html");
				}
				return getHtml();
			}
			
			private RestApiResponse getText(String text) {
				return RestApiResponse.textResponse(
						StatusCode.OK,
						Arrays.asList(
								"Access-Control-Allow-Origin: *", 
								"Content-Type: text/html; charset=utf-8",
								"X-XSS-Protection: 1; mode=block"
						),
						(bw)->{
							bw.write(text);
						}
					);
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
			
			private RestApiResponse getFilePage(String file) {
				return RestApiResponse.textResponse(
					StatusCode.OK,
					Arrays.asList(
						"Access-Control-Allow-Origin: *", 
						"Content-Type: text/html; charset=utf-8"
					),
					(bw)->{
						bw.write(Text.get().read((br)->{
							return ReadText.get().asString(br);
						}, file));
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
							Binary.get().read((dis)->{
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
								+ ""
								+ "<form >"
								+ "	<input type=\"text\" name=\"aa[]\"><br>" 
								+ "	<input type=\"text\" name=\"aa[]\"><br>" 
								+ "	<input type=\"text\" name=\"aa[]\"><br>" 
								+ "	<input type=\"text\" name=\"b[a]\"><br>" 
								+ "	<input type=\"text\" name=\"b[b]\"><br>" 
								+ "	<input type=\"text\" name=\"b[c]\"><br>" 
								+ "	<input type=\"text\" name=\"c[a][]\"><br>" 
								+ "	<input type=\"text\" name=\"c[a][]\"><br>" 
								+ "	<input type=\"text\" name=\"c[a][]\"><br>"
								+ "	<input type=\"submit\">"
								+ "</form>"
								+ ""
								+ "<form method=\"post\">" // 
								+ "Fill the name"
								+ "<input type='text' name='some-form-text'/><br>"
								+ "<input type=\"submit\" value=\"Diakritic\" name=\"submit\">"
								+ "</form>"
								+ ""
								+ "<form method=\"post\" enctype=\"multipart/form-data\" >" // accept-charset=\"utf-8\"
								+ "Fill the name"
								+ "<input type='text' name='some-form-text'/><br>"
								+ "Select image to upload:"
								+ " <input type=\"file\" name=\"fileToUpload\" id=\"fileToUpload-id\"> <br>"
								+ "<input type=\"submit\" value=\"Diakritic\" name=\"submit\">"
								+ "</form>"
								+ "" // štěstí
								+ "</body></html>", today.toString()
						));
					}
				);
			}

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, RequestParameters params, String ipAddress, String host, WebSocket websocket)
					throws IOException {
				if (url.equals("/ws")) {
					pool.execute(()->{
						int i = 0;
						while(i < 100 && !websocket.isClosed()) {
							try {
								Thread.sleep(5000);
								if (websocket.isRunning()) {
									websocket.send("Message #" + i++);
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						System.err.println("Task finished");
					});
					return RestApiResponse.webSocketResponse(
						Arrays.asList(),
						websocket, 
						(message)->{
							try {
								if (message.equals("end")) {
									websocket.close();
								} else {
									websocket.send("Response: " + message);
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						},
						(e)->{
							e.printStackTrace();
						}
					);
				}
				return RestApiResponse.textResponse(StatusCode.BAD_REQUEST, Arrays.asList(), (os)->os.write(""));
			}

		};
	}
	
	protected static Servant speakerFunction() {
		return (socket, charset)->{
			try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
		           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
		           	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
		           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
				for (int i = 0; i < 100; i++) {
					bw.write("Int #" + i + "");
					bw.newLine();
					if (i%10 == 0) {
						bw.write(SpeakerEndToEndTest.MESSAGE_END);
						bw.flush();
						
						//StringBuilder message = new StringBuilder();
						int c = br.read();
						while((char)c != SpeakerEndToEndTest.MESSAGE_END && c != -1) {
							System.out.println(i + "--> " + (char)c);
							c = br.read();
						}
						
						try {Thread.sleep(1000);
						} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
				/*// V1
				StringBuilder message = new StringBuilder();
				char c;
				while((c = (char)br.read()) != SpeakerEndToEndTest.MESSAGE_END) {
					message.append(c);
				}
				System.out.println(message);
				
				bw.write("ahoj");
				bw.newLine();
				bw.write("rád tě slyším" + SpeakerEndToEndTest.MESSAGE_END);
				bw.flush();
				
				message = new StringBuilder();
				while((c = (char)br.read()) != SpeakerEndToEndTest.MESSAGE_END) {
					message.append(c);
				}
				System.out.println(message);
				
				bw.write("Sbohem" + SpeakerEndToEndTest.MESSAGE_END);
				bw.flush();
				//*/  
			}
		};
	}
}
