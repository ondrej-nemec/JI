package socketCommunication;

import java.util.Optional;
import java.util.Properties;

import socketCommunication.http.client.RestApiClient;
import socketCommunication.http.client.RestApiResponse;

public class RestClientEndToEndTest {

	public static void main(String[] args) {
		/*
		Optional<ClientSecuredCredentials> config = Optional.empty();
		/*/
		Optional<ClientSecuredCredentials> config = Optional.of(
			//new ClientSecuredCredentials(Optional.of("certificates/p2/server-keystore.jks"), Optional.of("123456"))
			new ClientSecuredCredentials(Optional.empty(), Optional.empty())
		);
		//*/
		
		RestApiClient c = new RestApiClient("https://localhost", config, "UTF-8", new LoggerImpl());
		try {
			RestApiResponse r = c.get("/entity/list", new Properties(), new Properties());
			System.out.println(r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
