package socketCommunication;

import java.util.Optional;

public class ClientSecuredCredentials {

	private final Optional<String> clientTrustStore;
	private final Optional<String> clientTrustStorePassword;
	
	public ClientSecuredCredentials(Optional<String> clientTrustStore, Optional<String> clientTrustStorePassword) {
		this.clientTrustStore = clientTrustStore;
		this.clientTrustStorePassword = clientTrustStorePassword;
	}

	public Optional<String> getClientTrustStore() {
		return clientTrustStore;
	}

	public Optional<String> getClientTrustStorePassword() {
		return clientTrustStorePassword;
	}
	
}
