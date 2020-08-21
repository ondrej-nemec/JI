package socketCommunication.http.server.session;

import java.util.function.Consumer;

public interface SessionStorage {

	Session getSession(String sessionId);
	
	void addSession(Session session);
	
	void removeSession(String sessionId);
	
	void forEach(Consumer<Session> consumer);
	
}
