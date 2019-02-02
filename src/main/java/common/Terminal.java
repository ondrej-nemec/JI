package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Logger;

import text.BufferedReaderFactory;
import text.plaintext.PlainTextLoader;

public class Terminal {
	
	private final Logger logger;
	
	public Terminal(final Logger logger) {
		this.logger = logger;
	}
	
	public int run(final Consumer<String> stdOut, final Consumer<String> stdErr, final String command) {
		try {
			Process pr = Runtime.getRuntime().exec(command);
			pr.waitFor();
			
			readsAndApplyConsumer(pr.getInputStream(), stdOut);
			readsAndApplyConsumer(pr.getErrorStream(), stdErr);
			
			return pr.exitValue();
		} catch (IOException | InterruptedException e) {
			logger.severe(this.getClass() + ": " + e.getClass() + " " + e.getMessage());
		}
		return -1;
	}
	
	private void readsAndApplyConsumer(final InputStream stream, final Consumer<String> consumer) throws IOException {
		try (BufferedReader br = BufferedReaderFactory.buffer(stream)) {
			new PlainTextLoader(br).read(consumer);
		} finally {}
	}
}
