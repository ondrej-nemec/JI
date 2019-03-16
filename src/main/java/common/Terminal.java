package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import text.BufferedReaderFactory;
import text.plaintext.PlainTextLoader;

public class Terminal {
	
	private final Logger logger = Logger.getLogger(Terminal.class);
	
	public int runFile(final Consumer<String> stdOut, final Consumer<String> stdErr, final String fileName) {
		return run(stdOut, stdErr, fileName + Os.getCliExtention());
	}
	
	public int runCommand(final Consumer<String> stdOut, final Consumer<String> stdErr, final String command) {
		return run(stdOut, stdErr, Os.getPreCommand() + command);
	}
	
	private int run(final Consumer<String> stdOut, final Consumer<String> stdErr, final String command) {
		try {
			Process pr = Runtime.getRuntime().exec(command);
			pr.waitFor();
			
			readsAndApplyConsumer(pr.getInputStream(), stdOut);
			readsAndApplyConsumer(pr.getErrorStream(), stdErr);
			
			int exitValue = pr.exitValue();
			logger.debug("Command: " + command + " will return: " + exitValue);
			return exitValue;
		} catch (IOException | InterruptedException e) {
			logger.error("Command could not run: " + command, e);
		}
		return -1;
	}
	
	private void readsAndApplyConsumer(final InputStream stream, final Consumer<String> consumer) throws IOException {
		try (BufferedReader br = BufferedReaderFactory.buffer(stream)) {
			new PlainTextLoader(br).read(consumer);
		} finally {}
	}
}
