package ji.socketCommunication;

import java.util.Optional;

import ji.socketCommunication.http.ApiRequest;
import ji.socketCommunication.http.client.RestApiClient;

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
			ApiRequest r = c.get("/ping", (request)->{});
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End");
	}
	
}
