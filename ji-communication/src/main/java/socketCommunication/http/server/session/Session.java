package socketCommunication.http.server.session;

public class Session {
	
	private final String sessionId;
	
	private final String ip;
	
	private long expirationTime;
	
	private final String content;
	
	private final boolean isEmpty;

	public Session(String sessionId, String ip, long expirationTime, String content) {
		this(sessionId, ip, expirationTime, content, false);
	}

	public Session(String sessionId, String ip, long expirationTime, String content, boolean isEmpty) {
		this.sessionId = sessionId;
		this.expirationTime = expirationTime;
		this.content = content;
		this.ip = ip;
		this.isEmpty = isEmpty;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public String getIp() {
		return ip;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public String getContent() {
		return content;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String serialize() {
		return String.format("%s;%s;%s;%s", sessionId, ip, expirationTime, content);
	}
	
	public static Session deserialize(String session) {
		 String[] fields = session.split(";", 4);
		 try {
			 return new Session(
					 fields[0],
					 fields[1],
					 Long.parseLong(fields[2]),
					 fields.length < 4 ? "" : fields[3]
			);
		 } catch (Exception e) {
			 return empty();
		 }
	}
	
	public static Session empty() {
		return new Session("", "", 0, "", true);
	}
	
	@Override
	public String toString() {
		return serialize();
	}
	
	@Override
	public boolean equals(Object obj) {
		// FIX
		return toString().equals(obj.toString());
	}

}
