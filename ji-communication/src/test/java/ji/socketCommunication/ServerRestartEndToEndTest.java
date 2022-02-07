package ji.socketCommunication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import ji.common.functions.Console;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.StatusCode;
import ji.socketCommunication.http.server.RequestParameters;
import ji.socketCommunication.http.server.RestApiResponse;
import ji.socketCommunication.http.server.RestApiServerResponseFactory;
import ji.socketCommunication.http.server.WebSocket;

public class ServerRestartEndToEndTest {
	
	public final static Console console = new Console();

	public static void main(String[] args) {
		try {
			//*
			Server server = Server.createWebServer(10123, 5, 60000, apiResponse(), Optional.empty(), 10 * 1024, Optional.empty(),  "UTF-8",  new LoggerImpl());
			/*/
			Server server = Server.create(10123, 5, 60000, speakerFunction(), "UTF-8", new LoggerImpl());
			//*/
			
			server.start();
			Thread.sleep(30000);
			
			server.pause();
			System.err.println("server paused");
			Thread.sleep(30000);
			server.start();
			System.err.println("server restarted");
			Thread.sleep(30000);
			
			// server.stop();
			server.stop();
			/*
            Thread.sleep(10000);
			server.start();
			Thread.sleep(5000);
			// server.stop();
			server.stop(3, TimeUnit.SECONDS);
			//*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static RestApiServerResponseFactory apiResponse() {
		return new RestApiServerResponseFactory() {

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, RequestParameters params, String ip, Optional<WebSocket> websocket) throws IOException {
				Date today = new Date();
				return RestApiResponse.textResponse(
					StatusCode.OK,
					Arrays.asList("Access-Control-Allow-Origin: *", "Content-Type: text/html; charset=utf-8"),
					(bw)->{
						bw.write(String.format(
								"<html> <head></head><body><h1>Time</h1>%s</body></html>", today.toString()
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
