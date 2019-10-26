package clientserver.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

import clientserver.LoggerImpl;
import clientserver.HttpMethod;
import clientserver.StatusCode;
import common.Console;

public class ServerEndToEndTest {
	
	public final static Console console = new Console();

	public static void main(String[] args) {
		try {
			//*
			Server server = Server.create(10123, 5, 60000, apiResponse(), "UTF-8",  new LoggerImpl());
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
	
	protected static CreateRestAPIResponce apiResponse() {
		return new CreateRestAPIResponce() {
			
			@Override
			public void writeHeade(BufferedWriter bw) throws IOException {
				bw.write("Access-Control-Allow-Origin: *");
		        bw.newLine();
		        bw.write("Content-Type: text/html; charset=utf-8");
		        bw.newLine();				
			}
			
			@Override
			public void writeContent(BufferedWriter bw) throws IOException {
				Date today = new Date();
		    	bw.write("<html> <head></head><body><h1>Time</h1>");
		        bw.write(today.toString());
		        bw.write("</body></html>");
		        bw.newLine();
			}
			
			@Override
			public StatusCode getStatusCode() {
				return StatusCode.OK;
			}
			
			@Override
			public void accept(HttpMethod method, String url, String fullUrl, String protocol, Properties header,
					Properties params) {
				// not implemented
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
