package socketCommunication.http.server.session;

public class FileSessionStorageEndToEndTest {

	public static void main(String[] args) {
		FileSessionStorage storage = new FileSessionStorage("text-session-storage");
		Session session = new Session("session-id", "id", 123456, "some;another;content");
	//	Session session2 = new Session("session-id-2", "id", 123456, "some;another;content");
		storage.addSession(session);
		System.out.println(storage.getSession("session-id"));
		storage.removeSession("session-id");
	}
	
}
