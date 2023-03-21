package ji.socketCommunication.http;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Logger;

import ji.common.Log4j2LoggerTestImpl;
import ji.socketCommunication.Server;
import ji.socketCommunication.http.profiler.HttpServerProfiler;
import ji.socketCommunication.http.profiler.HttpServerProfilerEvent;
import ji.socketCommunication.http.structures.Request;
import ji.socketCommunication.http.structures.Response;
import ji.socketCommunication.http.structures.UploadedFile;
import ji.socketCommunication.http.structures.WebSocket;

public class RestApiServerEndToEndTest {
	private static final ExecutorService pool = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		try {
			Logger logger = new Log4j2LoggerTestImpl(null);
			/*RestApiServer.PROFILER = new HttpServerProfiler() {
				@Override
				public void log(Map<HttpServerProfilerEvent, Long> events) {
					logger.debug("Profiler");
					for (HttpServerProfilerEvent event : HttpServerProfilerEvent.values()) {
						logger.debug(" -> " + event + ": " + events.get(event));
					}
				}
			};*/
			
			RestApiServer restApi = new RestApiServer(10 * 1000 * 1000, logger);
			 restApi.addApplication(apiResponse(logger), "localhost", "example.com");
			Server server = restApi.createWebServer(80, 5, 120000, Optional.empty(), "UTF-8");
			/*Server server = Server.createWebServer(
				80,
				5,
				120000,
				apiResponse(),
				Optional.empty(),
				10 * 1000 * 1000, // 10 MB
				//Optional.empty(),
				"UTF-8",
				logger
			);*/
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
	
	protected static ResponseFactory apiResponse(Logger logger) {
		return new ResponseFactory() {
			@Override
			public Response accept(Request request, String ipAddress, Optional<WebSocket> websocket) throws IOException {
				switch (request.getPlainUri()) {
					case "/image.png": 
					case "/favicon.ico": return getFileResponse(request, "icon.png", "image/icon");
					case "/": // return getTextResponse(request, "OK");
					case "/index": return getFileResponse(request, "index.html", "text/html");
					case "/form": return getFileResponse(request, "form.html", "text/html");
					case "/fileform": return getFileResponse(request, "fileform.html", "text/html");
					case "/websocket": return getFileResponse(request, "websockets.html", "text/html");
					case "/redirect":
						Response redirect = new Response(StatusCode.TEMPORARY_REDIRECT, request.getProtocol());
						redirect.addHeader("Location", "/params");
						return redirect;
					case "/savefile":
						UploadedFile file = request.getBodyInParameters().getUploadedFile("fileToUpload");
						file.save("binary");
						Response fileResponse = getTextResponse(request,"File: " + file.getFileName() + " (" + file.getContent().length + "b)");
						return fileResponse;
					case "/params":
						StringBuilder params = new StringBuilder();
						params
							.append("URI params:")
							.append("<br>")
							.append(request.getQueryParameters())
							.append("<br><hr><br>")
							.append("Body parameters:")
							.append("<br>")
							.append(request.getBodyInParameters())
							.append("<br>")
							.append(request.getBody());
						Response paramsResponse = getTextResponse(request, params.toString());
						paramsResponse.addHeader("Content-Type", "text/html");
						return paramsResponse;
					case "/ws":
						if (websocket.isPresent()) {
							pool.execute(()->{
								System.err.println("Task start");
								int i = 0;
								while(i < 100 && !websocket.get().isClosed()) {
									try {
										Thread.sleep(5000);
										if (websocket.get().isRunning()) {
											websocket.get().send("Message #" + i++);
											System.err.println("Message sent");
										}
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
								System.err.println("Task finished");
							});
							Response wbRes = new Response(StatusCode.SWITCHING_PROTOCOL, request.getProtocol());
							websocket.get().accept(
								(message)->{
									try {
										if (message.equals("end")) {
											websocket.get().close();
										} else {
											websocket.get().send("Response: " + message);
										}
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								},
								(e)->{
									e.printStackTrace();
								}
							);
							return wbRes;
						}
					default:
						return new Response(StatusCode.BAD_REQUEST, request.getProtocol());
				}
			}
			
			@Override
			public HttpServerProfiler getProfiler() {
				return new HttpServerProfiler() {
					@Override
					public void log(Map<HttpServerProfilerEvent, Long> events) {
						logger.debug("Profiler");
						for (HttpServerProfilerEvent event : HttpServerProfilerEvent.values()) {
							logger.debug(" -> " + event + ": " + events.get(event));
						}
					}
				};
			}
		};
	}
	
	private static Response getTextResponse(Request request, String data) {
		Response response = new Response(StatusCode.OK, request.getProtocol());
		response.setBody(data.getBytes());
		return response;
	}
	
	private static Response getFileResponse(Request request, String file, String contentType) throws IOException {
		Response response = new Response(StatusCode.OK, request.getProtocol());
		response.addHeader("Content-Type", contentType);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (InputStream is = new FileInputStream("index/" + file)) {
			int i;
			while((i = is.read()) != -1) {
				os.write(i);
			}
		}
		response.setBody(os.toByteArray());
		return response;
	}
	
}
