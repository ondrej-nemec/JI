package socketCommunication.http.server.session;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import core.text.Text;
import core.text.basic.ReadText;
import core.text.basic.WriteText;

public class FileSessionStorage implements SessionStorage {
	
	private final String sessionPath;
	
	private final Set<String> sessions;
	
	public FileSessionStorage(String sessionPath) {
		this.sessionPath = sessionPath;
		this.sessions = new HashSet<>();
	}

	@Override
	public Session getSession(String sessionId) {
		if (!sessions.contains(sessionId)) {
			return null;
		}
		try {
			return Session.deserialize(Text.read((br)->{
				return ReadText.asString(br);
			}, getFileName(sessionId)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addSession(Session session) {
		try {
			Text.write((bw)->{
				WriteText.write(bw, session.serialize());
			}, getFileName(session.getSessionId()), false);
			sessions.add(session.getSessionId());
		} catch (IOException e) {
			sessions.remove(session.getSessionId());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeSession(String sessionId) {
		new File(getFileName(sessionId)).delete();
		sessions.remove(sessionId);
	}

	private String getFileName(String name) {
		return String.format("%s/%s.session", sessionPath, name);
	}

	@Override
	public void forEach(Consumer<Session> consumer) {
		sessions.forEach((sessionId)->{
			consumer.accept(getSession(sessionId));
		});
	}
	
}
