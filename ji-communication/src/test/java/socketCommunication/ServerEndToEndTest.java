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
import socketCommunication.http.server.RestApiResponse;

public class ServerEndToEndTest {
	
	public final static Console console = new Console();

	public static void main(String[] args) {
		try {
			//*
			Server server = Server.create(10123, 5, 60000, 60000, apiResponse(), "UTF-8",  new LoggerImpl());
			/*/
			Server server = Server.create(10123, 5, 60000, speakerFunction(), "UTF-8", new LoggerImpl());
			//*/
			
			server.start();
			for (int i = 0; i < 30; i++) {
                Thread.sleep(i * 10000);
            //    console.out("Server running");
            }
            server.stop();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected static RestApiServerResponseFactory apiResponse() {
		return new RestApiServerResponseFactory() {

			@Override
			public RestApiResponse accept(HttpMethod method, String url, String fullUrl, String protocol,
					Properties header, Properties params) throws IOException {
				if (url.equals("/favicon.ico")) {
					return RestApiResponse.binaryResponse(
						StatusCode.OK,
						Arrays.asList("Access-Control-Allow-Origin: *", "Content-Type: image/ico; charset=utf-8"),
						(bos)->{
								Binary.read((dis)->{
									byte[] cache = new byte[32];
									while (dis.read(cache) != -1) {
										bos.write(cache);
									}
								}, getClass().getResourceAsStream("/socketCommunication/favicon.ico"));
						}
					);
				}
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
