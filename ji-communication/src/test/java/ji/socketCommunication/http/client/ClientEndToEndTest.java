package ji.socketCommunication.http.client;

import java.util.Optional;
import java.util.Properties;

import ji.common.functions.Console;
import ji.socketCommunication.LoggerImpl;
import ji.socketCommunication.http.client.RestApiClient;

public class ClientEndToEndTest {
	
	private static final Console console = new Console();
	private static String ip = "127.0.0.1";

	public static void main(String[] args) {
		/*
		try {
			SpeakerClient client = new SpeakerClient(
					ip,
					10123,
					10000,
					60000,
					"UTF-8",
					new LoggerImpl()
			);
			
			for (int i = 0; i < 10; i++) {
                console.out(
                    client.communicate("message " + i)
                );
                Thread.sleep(1000);
            }
			client.disconnect();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		*/
		/***************************************/
		RestApiClient client = new RestApiClient(
			"https://" + ip + ":" + 10123 + "/server",
			Optional.empty(),
			"UTF-8",
			new LoggerImpl()
		);
		
		Properties header = new Properties();
		header.put("User-Agent", "Mozilla/5.0");
		header.put("User", "Chrome");
		Properties params = new Properties();
		params.put("userName", "sysadmin");
		params.put("password", "secret-password");
		
		try {
		//	client.send(HttpMethod.GET, "/", header, params);
		//*
			console.out("GET");
			console.out(client.get("/uri", header, params));
			console.out("POST");
			console.out(client.post("/uri", header, params));
			console.out("PUT");
			console.out(client.put("/uri", header, params));
			console.out("DELETE");
			console.out(client.delete("/uri", header, params));
		//*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
