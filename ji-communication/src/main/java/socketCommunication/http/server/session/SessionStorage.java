package socketCommunication.http.server.session;

public interface SessionStorage {

	Session getSession(String sessionId);
	
	void addSession(Session session);
	
	void removeSession(String sessionId);
	
}
