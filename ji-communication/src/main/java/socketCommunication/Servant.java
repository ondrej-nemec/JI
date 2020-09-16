package socketCommunication;

import java.io.IOException;
import java.net.Socket;

public interface Servant {

	//void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException;
	
	void serve(Socket socket, String charset) throws IOException;
	
}
