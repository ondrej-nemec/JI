package clientserver.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import clientserver.HttpMethod;
import clientserver.LoggerImpl;
import clientserver.StatusCode;
import clientserver.server.restapi.CreateRestAPIResponce;
import clientserver.server.restapi.RestApiResponse;
import common.Console;

public class ServerRestartEndToEndTest {
	
	public final static Console console = new Console();

	public static void main(String[] args) {
		try {
			//*
			Server server = Server.create(10123, 5, 8000, apiResponse(), "UTF-8",  new LoggerImpl());
			/*/
			Server server = Server.create(10123, 5, 60000, speakerFunction(), "UTF-8", new LoggerImpl());
			//*/
			
			server.start();
			Thread.sleep(10000);
			
			server.pause(true);
			Thread.sleep(10000);
			server.pause(false);
			
			
			// server.stop();
			server.stop(3, TimeUnit.SECONDS);
			/*
            Thread.sleep(10000);
			server.start();
			Thread.sleep(5000);
			// server.stop();
			server.stop(3, TimeUnit.SECONDS);
			//*/
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected static CreateRestAPIResponce apiResponse() {
		return new CreateRestAPIResponce() {

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, Properties params) throws IOException {
				Date today = new Date();
				return new RestApiResponse(
					StatusCode.OK,
					Arrays.asList("Access-Control-Allow-Origin: *", "Content-Type: text/html; charset=utf-8"),
					String.format("<html> <head></head><body><h1>Time</h1>%s</body></html>", today.toString())
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