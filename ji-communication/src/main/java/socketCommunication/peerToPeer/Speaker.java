package socketCommunication.peerToPeer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Function;

import common.Logger;
import socketCommunication.Servant;

public class Speaker implements Servant {
	
	private final Logger logger;
	
	private final Function<String, String> response;
	
	public Speaker(Function<String, String> response, Logger logger) {
		this.logger = logger;
		this.response = response;
	}
	
	@Override
	public void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException {
		String message = null;
        do {
        	message = Communication.readMessage(br);
            logger.debug("Receive message: " + message);
            Communication.writeMessage(bw, response.apply(message));
        } while (!message.equals(Communication.END));
        Communication.writeMessage(bw, Communication.END);
	}

}
