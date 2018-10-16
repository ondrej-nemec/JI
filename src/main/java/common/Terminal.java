package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import text.plaintext.PlainTextLoader;

public class Terminal {
	
	private final PlainTextLoader loader;
	
	public Terminal(PlainTextLoader loader) {
		this.loader = loader;
	}
	
	public Terminal() {
		this.loader = new PlainTextLoader();
	}
	
	public int run(Consumer<String> stdOut, Consumer<String> stdErr, String command) {
		try {
			Process pr = Runtime.getRuntime().exec(command);
			pr.waitFor();
			
			readsAndApplyConsumer(pr.getInputStream(), stdOut);
			readsAndApplyConsumer(pr.getErrorStream(), stdErr);
			
			return pr.exitValue();
		} catch (IOException | InterruptedException e) {
			//TODO what do with error
			e.printStackTrace();
		}
		//TODO find code of fatal error
		return -1;
	}
	
	private void readsAndApplyConsumer(InputStream stream, Consumer<String> consumer) {
		try (BufferedReader br = loader.buffer(stream)) {
			loader.read(br, consumer);
		} catch (IOException e1) {
			//TODO what do with error
			e1.printStackTrace();
		}
	}
}
