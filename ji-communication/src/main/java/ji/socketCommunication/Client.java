package ji.socketCommunication;

import java.net.Socket;
import java.util.Optional;

import ji.common.Logger;

public interface Client {

	default Socket createSocket(String ip, int port, Optional<SslCredentials> ssl, Logger logger) throws Exception {
    	if (ssl.isPresent()) {
    		return SSL.getSSLContext(ssl.get()).getSocketFactory().createSocket(ip, port);
		} else {
			logger.warn("No secured credentials, connection is not secured");
			return new Socket(ip, port);
		}
    }
    
}
