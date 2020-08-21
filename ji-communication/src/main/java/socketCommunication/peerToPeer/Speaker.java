package socketCommunication.peerToPeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
	/*
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
*/
	@Override
	public void serve(Socket socket, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
        //   	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
        //   	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
		) {
			String message = null;
	        do {
	        	message = Communication.readMessage(br);
	            logger.debug("Receive message: " + message);
	            Communication.writeMessage(bw, response.apply(message));
	        } while (!message.equals(Communication.END));
	        Communication.writeMessage(bw, Communication.END);
       }
	}
	@Override
	public void start() {}
	
	@Override
	public void stop() {}

}
