package clientserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Communication {

	public static final String END = "\u0000\u0000";
	
	public static final String NEW_LINE = "\r\n";

	public static String readMessage(BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
		String line = br.readLine();
        while (line != null) {
        	builder.append(line);
        	builder.append(NEW_LINE);
            line = br.readLine();
        }
        return builder.toString();
    }
    
    public static void writeMessage(BufferedWriter bw, String... messages) throws IOException {
        for (String message : messages) {
            bw.write(message);
            bw.write(NEW_LINE);
        }
        bw.write(NEW_LINE);
        bw.flush();
    }
	
}
