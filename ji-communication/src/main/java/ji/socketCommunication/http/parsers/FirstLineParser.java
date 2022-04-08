package ji.socketCommunication.http.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import ji.common.Logger;
import ji.common.structures.DictionaryValue;
import ji.socketCommunication.http.ApiRequest;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.StatusCode;

public class FirstLineParser {
	
	private final Logger logger;
	private final PayloadParser payloadParser;
	
	public FirstLineParser(Logger logger, PayloadParser payloadParser) {
		this.logger = logger;
		this.payloadParser = payloadParser;
	}

	public void writeFirstLine(ApiRequest request, BufferedWriter bw) throws IOException {
		if (request.getStatusCode() == null) { // request
			bw.write(request.getMethod().toString());
	        bw.write(" ");
	        bw.write(request.getFullUrl());
	        bw.write(" ");
	        bw.write(request.getProtocol());
		} else {
			bw.write(request.getProtocol());
	        bw.write(" ");
	        bw.write(request.getStatusCode().toString());
		}
        bw.newLine();
	}
	
	public ApiRequest readFirstLine(BufferedReader br) throws IOException {
		String first = br.readLine();
		if (first == null) {
			logger.warn("Wrong request - empty first line.");
			return null;
		}
		String[] methods = first.split(" ", 3);
		if (methods.length != 3) {
			logger.warn("Invalid request: " + first);
			return null; // what now?
		}
		if (new DictionaryValue(methods[0].toUpperCase()).is(HttpMethod.class)) {
			// request: POST /cgi-bin/process.cgi HTTP/1.1
			ApiRequest request = new ApiRequest(
				HttpMethod.valueOf(methods[0].toUpperCase()), 
				methods[1], 
				methods[2]
			);
			String[] urlParst = methods[1].split("\\?");
			request.setUrl(urlParst[0]);
			if (urlParst.length > 1) {
				payloadParser.parsePayload(
					(name, value)->request.addUrlParameter(name, value),
					name->request.getUrlParameter(name),
					urlParst[1]
				);
			}
			return request;
		}
		// response: HTTP/1.1 200 OK
		ApiRequest request = new ApiRequest(
			StatusCode.valueOf(methods[2].toUpperCase().replace(" ", "_").replace("-", "_")),
			// StatusCode.forCode(Integer.parseInt(methods[1])),
			methods[0]
		);
		return request;
	}
}
