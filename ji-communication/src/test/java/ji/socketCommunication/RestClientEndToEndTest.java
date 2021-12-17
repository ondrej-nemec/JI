package ji.socketCommunication;

import java.util.Optional;
import java.util.Properties;

import ji.socketCommunication.http.client.RestApiClient;
import ji.socketCommunication.http.client.RestApiResponse;

public class RestClientEndToEndTest {

	public static void main(String[] args) {
		System.out.println("Start");
		SslCredentials ssl = new SslCredentials();
		
		// ssl.setCertificateStore("certificates/test2/JKS-client-certificate.jks", "12345678");
		// ssl.setTrustAll(false);
		//ssl.setTrustedClientsStore("certificates/test2/JKS-client-accept.jks", "12345678");
		//ssl.setTrustedClientsStore("certificates/test3/jks-client-accept.jks", "123456");
		ssl.setTrustedClientsStore("certificates/test3/jks-server-certificate.jks", "123456");
		/*
		String url = "http://localhost:80";
		Optional<SslCredentials> config = Optional.empty();
		/*/
		String url = "https://localhost:443";
		Optional<SslCredentials> config = Optional.of(ssl);
		//*/
		//  
		// "http://localhost/linkedPages"  /ji-doc/index.html
		RestApiClient c = new RestApiClient(url, config, "UTF-8", new LoggerImpl());
		try {
			RestApiResponse r = c.get("/ping", new Properties(), new Properties());
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End");
	}
	
}
