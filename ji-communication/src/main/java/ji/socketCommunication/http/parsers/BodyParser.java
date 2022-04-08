package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ji.common.structures.DictionaryValue;
import ji.json.JsonReader;
import ji.json.JsonStreamException;
import ji.socketCommunication.http.ApiRequest;
import ji.socketCommunication.http.server.UploadedFile;

public class BodyParser {
	
	private final PayloadParser payloadParser;
	private final int maxUploadFileSize;
	private final Optional<List<String>> allowedFileTypes;
	
	public BodyParser(PayloadParser payloadParser, int maxUploadFileSize, Optional<List<String>> allowedFileTypes) {
		this.payloadParser = payloadParser;
		this.maxUploadFileSize = maxUploadFileSize;
		this.allowedFileTypes = allowedFileTypes;
	}
	
	public void writeBody(ApiRequest request, BufferedWriter bw, BufferedOutputStream os) throws IOException {
		// TODO
		if (!request.getBodyParameters().isEmpty()) {
			bw.write(payloadParser.parsePayload(request.getBodyParameters()));
		} else if (request.getBody() != null) {
			//String type = request.getHeader("Content-Type");
			// TODO request.getHeader("Content-Length", Integer.class);
			// TODO support for binary body
			bw.write(request.getBody());
		}
		//	os.flush(); //only with binary write
		bw.flush();
	}

	public void readBody(ApiRequest request, BufferedReader br, BufferedInputStream bis) throws IOException {
		Object type = request.getHeader("Content-Type");
        if (type != null && type.toString().contains("multipart/form-data")) {
			parseFileForm(type.toString(), request, br, bis);
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
	        if (type != null && type.toString().contains("application/json")) {
	        	try {
					request.addBodyParameters(new DictionaryValue(new JsonReader().read(payload.toString())).getMap());
				} catch (JsonStreamException e) {
					throw new IOException(e);
				}
	        } else if (type != null && type.toString().contains("application/xml")) { // TODO parser
	        	request.setBody(payload.toString());
	        } else if (type != null && type.toString().contains("text/")) {
	        	// plain, html, ....
	        	request.setBody(payload.toString());
	        } else if (type != null && type.toString().contains("application")) {
	        	// javascript
	        	request.setBody(payload.toString());
	        } else {
	        	payloadParser.parsePayload(
	        		(name, value)->request.addBodyParameter(name, value),
					name->request.getBodyParameter(name),
					payload.toString()
				);
	        }
		}
	}
	
	protected void parseFileForm(
			String type,
			ApiRequest request,
			BufferedReader br, BufferedInputStream bis) throws IOException {
		int contentLength = request.getHeader("Content-Length", Integer.class);
		
		boolean containsFile = bis.available() > 0;
		String boundary = "--" + type.split(";")[1].split("=")[1].trim();
		
		int readed = 0;
		boolean isElementValue = false;
		String elementName = null;
		String elementValue = null;
		String contentType = null;
		String filename = null;
		ByteArrayOutputStream fileContent = null;
		while(readed < contentLength) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] bytes;
			String requestLine;
			if (containsFile) {
				while (readed < contentLength) {
					if (bis.available() == 0) {
						throw new IOException("Content length not match expecation. Expected: " + contentLength + ", actual: " + readed);
	                }
					byte actual = (byte)bis.read();
					readed++;
					stream.write(actual);
					if (actual == '\n') {
						break;
					}
				}
				bytes = stream.toByteArray();
				requestLine = new String(bytes);
			} else {
				StringBuilder builder = new StringBuilder();
				while (readed < contentLength) {
					if (!br.ready()) {
						throw new IOException("Content length not match expecation. Expected: " + contentLength + ", actual: " + readed);
	                }
					char actual = (char)br.read();
					readed += (actual+"").getBytes().length;
					builder.append(actual);
					if (actual == '\n') {
						break;
					}
				}
				requestLine = builder.toString();
				bytes = null;
			}
			requestLine = requestLine.replace("\r", "").replace("\n", "");
			
			if (requestLine.startsWith(boundary)) {
				if (elementName != null && elementValue != null) {
					payloadParser.parseParams(
						(name, value)->request.addBodyParameter(name, value),
						name->request.getBodyParameter(name),
						elementName, 
						elementValue
					);
				} else if (elementName != null && fileContent != null) {
					request.addBodyParameter(elementName, new UploadedFile(filename, contentType, fileContent));
				}
			}
			if (boundary.equals(requestLine)) {
				isElementValue = false;
				elementName = null;
				contentType = null;
				filename = null;
				elementValue = null;
				fileContent = null;
			} else if (requestLine.isEmpty() && fileContent != null && isElementValue && bytes.length == 1) {
				// if bytes.lenght == 2 => start or end
				// if isElementValue == false => start
				fileContent.write(bytes); // fix
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
						fileContent = new ByteArrayOutputStream();
					}
					if (bytes.length + fileContent.size() > maxUploadFileSize) {
						throw new IOException("Maximal upload file size overflow " + maxUploadFileSize);
					}
					fileContent.write(bytes);
					fileContent.flush();
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
	}
	
}
