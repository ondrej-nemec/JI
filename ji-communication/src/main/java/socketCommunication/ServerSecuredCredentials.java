package socketCommunication;

import java.util.Optional;

public class ServerSecuredCredentials {

	private final String serverKeyStore;
	private final String serverKeystorePassword;
	
	private final Optional<String> clientTrustStore;
	private final Optional<String> clientTrustStorePassword;
	
	public ServerSecuredCredentials(String serverKeyStore, String serverKeystorePassword,
			Optional<String> clientTrustStore, Optional<String> clientTrustStorePassword) {
		this.serverKeyStore = serverKeyStore;
		this.serverKeystorePassword = serverKeystorePassword;
		this.clientTrustStore = clientTrustStore;
		this.clientTrustStorePassword = clientTrustStorePassword;
	}

	public String getServerKeyStore() {
		return serverKeyStore;
	}

	public String getServerKeystorePassword() {
		return serverKeystorePassword;
	}

	public Optional<String> getClientTrustStore() {
		return clientTrustStore;
	}

	public Optional<String> getClientTrustStorePassword() {
		return clientTrustStorePassword;
	}
	
}
