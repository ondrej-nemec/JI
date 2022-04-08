package ji.socketCommunication.http.parsers;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ji.common.Logger;
import ji.socketCommunication.http.ApiRequest;

public class Parser {
	
	private final PayloadParser payloadParser;
	private final HeaderParser headersParser;
	private final FirstLineParser firstLineParser;
	private final BodyParser bodyParser;
	
	public Parser(Logger logger, int maxUploadFileSize, Optional<List<String>> allowedFileTypes) {
		this.payloadParser = new PayloadParser(logger);
		this.headersParser = new HeaderParser(logger);
		this.firstLineParser = new FirstLineParser(logger, payloadParser);
		this.bodyParser = new BodyParser(payloadParser, maxUploadFileSize, allowedFileTypes);
	}

	public ApiRequest readRequest(BufferedReader br, BufferedInputStream bis) throws IOException {
		ApiRequest request = firstLineParser.readFirstLine(br);
		if (request == null) {
			return null;
		}
		headersParser.readHeaders(request, br);
		bodyParser.readBody(request, br, bis);
		return request;
	}
	
	public void writeRequest(ApiRequest request, BufferedWriter bw, BufferedOutputStream os) throws IOException {
		// TODO maybe make full url from URL params
		firstLineParser.writeFirstLine(request, bw);
		headersParser.writeHeaders(request, bw);
		bodyParser.writeBody(request, bw, os);
	}

}
