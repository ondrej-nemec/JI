package socketCommunication.http.server.session;

import java.util.function.Consumer;

public class NullSessiongStorage implements SessionStorage {

	@Override
	public Session getSession(String sessionId) {
		return null;
	}

	@Override
	public void addSession(Session session) {}

	@Override
	public void removeSession(String sessionId) {}

	@Override
	public void forEach(Consumer<Session> consumer) {}

}
