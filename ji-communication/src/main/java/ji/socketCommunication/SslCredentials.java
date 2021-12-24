package ji.socketCommunication;

public class SslCredentials {

	private String certificateStore;
	private String certificateStorePassword;
	
	private String trustedClientsStore;
	private String clientTrustStorePassword;
	
	private boolean trustAll = true;
	
	public String getCertificateStore() {
		return certificateStore;
	}
	public String getCertificateStorePassword() {
		return certificateStorePassword;
	}
	public String getTrustedClientsStore() {
		return trustedClientsStore;
	}
	public String getClientTrustStorePassword() {
		return clientTrustStorePassword;
	}
	
	public boolean useCertificate() {
		return certificateStore != null;
	}
	
	public boolean useTrustedClients() {
		return trustedClientsStore != null;
	}
	
	public boolean trustAll() {
		return trustAll;
	}
	
	public void setCertificateStore(String certificateStore, String certificateStorePassword) {
		this.certificateStore = certificateStore;
		this.certificateStorePassword = certificateStorePassword;
	}
	
	public void setTrustAll() {
		this.trustAll = true;
	}
	
	public void setTrustedClientsStore(String trustedClientsStore, String clientTrustStorePassword) {
		this.trustedClientsStore = trustedClientsStore;
		this.clientTrustStorePassword = clientTrustStorePassword;
		this.trustAll = false;
	}

	
}
