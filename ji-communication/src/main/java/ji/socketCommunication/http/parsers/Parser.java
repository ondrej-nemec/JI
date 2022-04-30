package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import ji.common.Logger;
import ji.common.structures.ThrowingSupplier;
import ji.socketCommunication.http.Exchange;
import ji.socketCommunication.http.ParsingException;
import ji.socketCommunication.http.Request;
import ji.socketCommunication.http.Response;

public class Parser implements HeaderParser, FirstLineParser {
	
	private final PayloadParser payloadParser;
	private final BodyParser bodyParser;
	
	private Logger logger;
	
	public Parser(Logger logger, int maxUploadFileSize, Optional<List<String>> allowedFileTypes) {
		this.payloadParser = new PayloadParser(logger);
		this.bodyParser = new BodyParser(payloadParser, maxUploadFileSize, allowedFileTypes);
		this.logger = logger;
	}
	
	protected Parser(Logger logger, int maxUploadFileSize, Optional<List<String>> allowedFileTypes, Supplier<String> createBoundary) {
		this.payloadParser = new PayloadParser(logger);
		this.bodyParser = new BodyParser(payloadParser, maxUploadFileSize, allowedFileTypes, createBoundary);
		this.logger = logger;
	}
	
	public Response readResponse(BufferedInputStream bis) throws IOException {
		return read(bis, ()->createResponse(bis));
	}
	
	public Request readRequest(BufferedInputStream bis) throws IOException {
		return read(bis, ()->createRequest(bis));
	}
	
	private <T extends Exchange> T read(BufferedInputStream bis, ThrowingSupplier<T, IOException> create) throws IOException {
		try {
			T exchange = create.get();
			if (exchange == null) {
				return null;
			}
			List<String> errors = readHeaders(exchange, bis);
			if (errors.size() > 0) {
				errors.forEach((err)->{
					logger.warn(err);
				});
				return null;
			}
			bodyParser.readBody(exchange, bis);
			return exchange;
		} catch (ParsingException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
	
	public void write(Exchange exchange, BufferedOutputStream bos) throws IOException {
		writeFirstLine(exchange, bos);
		writeHeaders(exchange, bos);
		bodyParser.writeBody(exchange, bos);
	}

}
