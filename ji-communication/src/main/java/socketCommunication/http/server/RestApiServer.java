package socketCommunication.http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Logger;
import socketCommunication.Servant;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.UrlEscape;

public class RestApiServer implements Servant {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
		
	private final Logger logger;
	private final int maxUploadFileSize;
	private final Optional<List<String>> allowedFileTypes;
	private final RestApiServerResponseFactory createResponce;
	
	public RestApiServer(
			RestApiServerResponseFactory response,
			int maxUploadFileSize,
			Optional<List<String>> allowedFileTypes,
			Logger logger) {
		this.logger = logger;
		this.createResponce = response;
		this.allowedFileTypes = allowedFileTypes;
		this.maxUploadFileSize = maxUploadFileSize;
	}
	
	@Override
	public void serve(Socket socket, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
           	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
			serve(br, bw, is, os, socket.getInetAddress().toString());
       }
	}
	
	protected void serve(
			BufferedReader br, 
			BufferedWriter bw,
			BufferedInputStream is,
			BufferedOutputStream os,
			String clientIp) throws IOException {
		Properties params = new Properties();
		Properties header = new Properties();
		Properties request = new Properties();
		parseRequest(request, header, params, br, is);
		/***********/
		logger.debug("Request: " + request);
		RestApiResponse response = createResponce.accept(
			HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase()),
			request.getProperty(URL),
			request.getProperty(FULL_URL),
			request.getProperty(PROTOCOL),
			header,
			params,
			clientIp
		);
		sendResponse(response, request, bw, os);
	}
	
	private void sendResponse(
			RestApiResponse response,
			Properties request, 
			BufferedWriter bw,
			BufferedOutputStream os
		) throws IOException {
		String code = response.getStatusCode().toString();
		logger.debug("Response: " + code);
				
		// write first line
		bw.write(request.getProperty(PROTOCOL));
        bw.write(" ");
        bw.write(code);
        bw.newLine();
		// write headers
        for (String headerLine : response.getHeader()) {
        	bw.write(headerLine);
        	bw.newLine();
        }
		// end of header
        bw.newLine();
        bw.flush();
		// write binary context
        response.createBinaryContent(os);
		// write text context
        response.createTextContent(bw);
	}

	/********* PARSE **************/
	
	private void parseRequest(
			Properties request, Properties header, Properties params, 
			BufferedReader br, BufferedInputStream bis) throws IOException {
		// url, method, protocol
		String first = br.readLine();
		parseFirst(request, params, first);
        // header
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
        	parseHeaderLine(line, header);
        	line = br.readLine();
        }
        String type = header.getProperty("Content-Type");
		if (type != null && type.contains("multipart/form-data")) {
			int contentLength = Integer.parseInt(header.getProperty("Content-Length"));
			String boundary = "--" + type.split(";")[1].split("=")[1].trim();
			int readed = 0;
			boolean isElementValue = false;
			String elementName = null;
			String elementValue = null;
			String contentType = null;
			String filename = null;
			List<Byte> fileContent = null;
			while(readed < contentLength) {
				List<Byte> reqLine = new LinkedList<>();
				while (readed < contentLength) {
					byte actual = (byte)bis.read();
					readed++;
					reqLine.add(actual);
					if (actual == '\n') {
						break;
					}
				}
				
				byte[] bytes = new byte[reqLine.size()];
				for (int i = 0; i < reqLine.size(); i++) {
					bytes[i] = reqLine.get(i);
				}
				
				String requestLine = new String(bytes).replace("\r", "").replace("\n", "");
				if (requestLine.startsWith(boundary)) {
					if (elementName != null && elementValue != null) {
						params.put(elementName, elementValue);
					} else if (elementName != null && fileContent != null) {
						params.put(elementName, new UploadedFile(filename, contentType, fileContent));
					}
				}
				if (boundary.equals(requestLine)) {
					isElementValue = false;
					elementName = null;
					contentType = null;
					filename = null;
					elementValue = null;
					fileContent = null;
				} else if (requestLine.isEmpty()) {
					isElementValue = true;
				} else if (isElementValue) {
					if (filename == null) { // text element
						if (elementValue == null) {
							elementValue = "";
						} else {
							elementValue += "\n";
						}
						elementValue += requestLine;
					} else { // file
						if (fileContent == null) {
							fileContent = new LinkedList<>();
						}
						for (byte b : reqLine) {
							if (fileContent.size() > maxUploadFileSize) {
								throw new IOException("Maximal upload file size overflow " + maxUploadFileSize);
							}
							fileContent.add(b);
						}
					}
				} else if (requestLine.startsWith("Content-Disposition: form-data; name=") && !isElementValue) {
					Matcher m = Pattern.compile(" name=\\\"(([^\\\"])+)\\\"(; (filename=\\\"(([^\\\"])+)\\\")?)?")
							.matcher(requestLine);
					m.find();
					elementName = m.group(1);
					filename = m.group(5);
					if (filename != null && (filename.contains("..") || filename.contains("/") || filename.contains("\\"))) {
						throw new IOException("Filename is probably corrupted " + filename);
					}
				} else if (requestLine.startsWith("Content-Type: ") && !isElementValue) {
					contentType = requestLine.replace("Content-Type: ", "");
					if (allowedFileTypes.isPresent() && !allowedFileTypes.get().contains(contentType)) {
						throw new IOException("Uploading file content type is not allowed " + contentType);
					}
				}
            }
		} else {
	        // payload
	        StringBuilder payload = new StringBuilder();
	       	// stream ready is fix - before close stream does not wrote -1
	        // using header Content-Length is not reliable - does not works f.e. with arabic chars
	        if (br.ready()) {
	            int value;
	            while((value = br.read()) != -1) {
	                payload.append((char) value);
	                if (!br.ready()) {
	                    break;
	                }
	            }
	        }
	        parsePayload(params, payload.toString());
		}
	}

	/** protected for test only */
	protected void parseFirst(Properties request, Properties params, String first) {
		String[] methods = first.split(" ");
		if (methods.length != 3) {
			logger.warn("Invalid request: " + first);
			return; //TODO what now?
		}
		
		String[] urlParst = methods[1].split("\\?");
		if (urlParst.length > 1) {
			parsePayload(params, urlParst[1]);
		}		
		
		request.put(METHOD, methods[0]);
		request.put(URL, urlParst[0]);
		request.put(FULL_URL, methods[1]);
		request.put(PROTOCOL, methods[2]);
	}

	/** protected for test only */
	protected void parseHeaderLine(String line, Properties header) {
		if (line.isEmpty()) {
			return;
		}
		String[] property = line.split(": ", 2);
    	if (property.length == 2 && ! property[0].isEmpty()) {
    		header.put(property[0], property[1]);
    	} else if (property.length == 1){
    		header.put(property[0], "");
    	} else {
    		logger.warn("Invalid header line " + line);
    	}
	}

	/** protected for test only */
	protected void parsePayload(Properties prop, String payload) {
		if (payload.isEmpty()) {
			return;
		}
		String[] params = payload.split("\\&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 1) {
				prop.put(UrlEscape.unEscapeText(keyValue[0]), "");
			} else if (keyValue.length == 2) {
				prop.put(UrlEscape.unEscapeText(keyValue[0]), UrlEscape.unEscapeText(keyValue[1]));
			} else {
	    		logger.warn("Invalid param " + param);
	    	}
		}
	}

}
