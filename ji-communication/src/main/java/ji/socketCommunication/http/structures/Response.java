package ji.socketCommunication.http.structures;

import ji.socketCommunication.http.StatusCode;

public class Response extends Exchange {

	private final String protocol;
	private final StatusCode code;
	
	public Response(StatusCode code, String protocol) {
		super();
		this.protocol = protocol;
		this.code = code;
	}

	public String getProtocol() {
		return protocol;
	}

	public StatusCode getCode() {
		return code;
	}

	@Override
	public String getFirstLine() {
		return String.format("%s %s %s", protocol, code.getCode(), code.getDescription());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Response other = (Response) obj;
		if (code != other.code) {
			return false;
		}
		if (protocol == null) {
			if (other.protocol != null) {
				return false;
			}
		} else if (!protocol.equals(other.protocol)) {
			return false;
		}
		return true;
	}
	
}
