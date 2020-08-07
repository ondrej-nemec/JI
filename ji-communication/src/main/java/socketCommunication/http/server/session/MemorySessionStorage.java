package socketCommunication.http.server.session;

import java.util.HashMap;
import java.util.Map;

public class MemorySessionStorage implements SessionStorage {

	private Map<String, Session> sessions = new HashMap<>();

	@Override
	public Session getSession(String sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	@Override
	public void addSession(Session session) {
		sessions.put(session.getSessionId(), session);
	}
	
}
