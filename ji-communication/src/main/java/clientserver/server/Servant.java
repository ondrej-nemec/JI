package clientserver.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Servant {

	void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException;
	
}
