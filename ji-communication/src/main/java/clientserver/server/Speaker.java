package clientserver.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Function;

import clientserver.Communication;
import common.Logger;
import common.structures.ThrowingBiConsumer;

public class Speaker implements ThrowingBiConsumer<BufferedReader, BufferedWriter, IOException> {
	
	private final Logger logger;
	
	private final Function<String, String> response;
	
	public Speaker(Function<String, String> response, Logger logger) {
		this.logger = logger;
		this.response = response;
	}
	
	@Override
	public void accept(BufferedReader br, BufferedWriter bw) throws IOException {
		String message = null;
        do {
        	message = Communication.readMessage(br);
            logger.debug("Receive message: " + message);
            Communication.writeMessage(bw, response.apply(message));
        } while (!message.equals(Communication.END));
        Communication.writeMessage(bw, Communication.END);
	}

}
