package ji.socketCommunication.http.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import ji.common.exceptions.LogicException;
import ji.common.structures.DictionaryValue;
import ji.common.structures.IntegerBuilder;
import ji.json.JsonReader;
import ji.json.JsonWritter;
import ji.socketCommunication.http.Exchange;
import ji.socketCommunication.http.server.UploadedFile;

public class BodyParser implements Stream {
	
	private class ExceptionWrapper extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public ExceptionWrapper(IOException e) {
			super(e);
		}
		@Override
		public synchronized IOException getCause() {
			return (IOException)super.getCause();
		}
	}
	
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	public static final String CONTENT_LENGTH_HEADER = "Content-Length";
	public static final String WEBSOCKET_HEADER = "websocket";
	
	public static final String UPDATE_WEBSOCKET = "update";
	
	public static final String TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String TYPE_FORM = "multipart/form-data";
	public static final String TYPE_JSON = "application/json";
	
	private final PayloadParser payloadParser;
	private final int maxUploadFileSize;
	private final Optional<List<String>> allowedFileTypes;
	private final Supplier<String> createBoundary;
	
	public BodyParser(PayloadParser payloadParser, int maxUploadFileSize, Optional<List<String>> allowedFileTypes) {
		this(payloadParser, maxUploadFileSize, allowedFileTypes, ()->RandomStringUtils.randomAlphanumeric(50));
	}
	
	protected BodyParser(
			PayloadParser payloadParser, int maxUploadFileSize, 
			Optional<List<String>> allowedFileTypes,
			Supplier<String> createBoundary) {
		this.payloadParser = payloadParser;
		this.maxUploadFileSize = maxUploadFileSize;
		this.allowedFileTypes = allowedFileTypes;
		this.createBoundary = createBoundary;
	}
	
	public void writeBody(Exchange exchange, BufferedOutputStream bos) throws IOException {
		if (exchange.getBody() != null) {
			// end of header section
			bos.write('\n');// bw.newLine();
	        bos.flush();
	        
	        switch (exchange.getBodyType()) {
				case FORM_DATA:
					try {
						writeFileForm(new DictionaryValue(exchange.getBody()).getMap(), bos);
					} catch (ExceptionWrapper e) {
						throw e.getCause();
					}
					break;
				case JSON:
					bos.write(new JsonWritter().write(exchange.getBody()).getBytes());
					break;
				case PLAIN_TEXT_OR_BINARY:
					if (exchange.getBody() instanceof ByteArrayOutputStream) {
						bos.write(ByteArrayOutputStream.class.cast(exchange.getBody()).toByteArray());
					} else if (exchange.getBody() instanceof byte[]) {
						bos.write((byte[])exchange.getBody());
					} else {
						bos.write(exchange.getBody().toString().getBytes());
					}
					break;
				case URLENCODED_DATA:
					DictionaryValue body = new DictionaryValue(exchange.getBody());
					if (!body.is(Map.class)) {
						throw new LogicException("Body must be Map for URL Encoding");
					}
					bos.write(payloadParser.createPayload(body.getMap()).getBytes());
					break;
				case EMPTY:
		    		// TODO support for application/xml
				default:
					// ignore empty or not supported type
					break;
	        }
		}
		bos.flush();
	}

	private void writeFileForm(Map<String, Object> map, BufferedOutputStream bos) throws IOException {
		String boundary = createBoundary.get();
		IntegerBuilder size = new IntegerBuilder(0);
		map.forEach((name, value)->{
			payloadParser.createPayload((n, v)->{
				try {
					byte[] bytes = String.format("--%s", boundary).getBytes();
					byte[] newLine = "\n".getBytes();

					bos.write(bytes);
					bos.write(newLine);
					size.add(bytes.length + newLine.length);
					
					if (v instanceof UploadedFile) {
						UploadedFile file = UploadedFile.class.cast(v);
						bytes = String.format(
							"Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
							n, file.getFileName()
						).getBytes();
						bos.write(bytes);
						bos.write(newLine);
						size.add(bytes.length + newLine.length);
						
						bytes = String.format("Content-Type: %s", file.getContentType()).getBytes();
						bos.write(bytes);
						bos.write(newLine);
						size.add(bytes.length + newLine.length);
						
						bos.write(newLine);
						size.add(newLine.length);
						
						bos.write(file.getContent());
						size.add(file.getContent().length);
					} else {
						bytes = String.format("Content-Disposition: form-data; name=\"%s\"", n).getBytes();
						bos.write(bytes);
						bos.write(newLine);
						size.add(bytes.length + newLine.length);
						
						bos.write(newLine);
						size.add(newLine.length);

						bytes = v.toString().getBytes();
						bos.write(bytes);
						size.add(bytes.length);
					}
					
					bos.write(newLine);
					size.add(newLine.length);
				} catch (IOException e) {
					throw new ExceptionWrapper(e);
				}
			}, name, value);
		});
		byte[] bytes = String.format("--%s--", boundary).getBytes();
		bos.write(bytes);
		size.add(bytes.length); // TODO use size
	}

	public void readBody(Exchange exchange, BufferedInputStream bis) throws IOException {
		if (exchange.containsHeader(WEBSOCKET_HEADER)) {
			// TODO
			return;
		}
		Object type = exchange.getHeader(CONTENT_TYPE_HEADER);
		Integer length = exchange.getHeader(CONTENT_LENGTH_HEADER, Integer.class);
		if (type != null && type.toString().startsWith(TYPE_FORM)) {
			parseFileForm(type.toString(), exchange, bis);
			return;
		}
		ByteArrayOutputStream data = readData(length, bis, 0, c->false, false);
		if (data.size() == 0) {
			return; // no body
		}
		if (type == null) {
			// TODO parse url?
			exchange.setBodyPlainOrBinary(data.toByteArray());
			return;
		}
		// TODO support for application/xml
		switch (type.toString()) {
			case TYPE_JSON:
				exchange.setBodyJson(
					new DictionaryValue(new JsonReader().read(new String(data.toByteArray()))).getMap()
				);
				break;
			case TYPE_FORM_URLENCODED:
				exchange.setBodyUrlEncoded(parseUrl(new String(data.toByteArray())));;
				break;
			default:
				exchange.setBodyPlainOrBinary(data.toByteArray());
				break;
		}
	}
	
	private Map<String, Object> parseUrl(Object payload) throws UnsupportedEncodingException {
		Map<String, Object> data = new HashMap<>();
		payloadParser.parsePayload(
        	(name, value)->data.put(name, value),
			name->data.get(name),
			payload.toString()
		);
		return data;
	}
	

	private void parseFileForm(String type, Exchange exchange, BufferedInputStream bis) throws IOException {
		int contentLength = exchange.getHeader(CONTENT_LENGTH_HEADER, Integer.class);
		String boundary = "--" + type.replace(TYPE_FORM + ";", "").trim().replace("boundary=", "").trim();

		int readed = 0;
		Map<String, Object> data = new HashMap<>();
		exchange.setBodyFormData(data);
		
		String elementName = null;
		String elementValue = null;
		String filename = null;
		String contentType = null;
		boolean isValue = false;
		ByteArrayOutputStream fileContent = null;
		Boolean useRN = null;

		while(readed < contentLength) {
			ByteArrayOutputStream readedData = readData(contentLength, bis, readed, actual->actual == '\n', false);
			readed += readedData.size();
			String requestLine = new String(readedData.toByteArray());
			if (useRN == null) {
				useRN = requestLine.endsWith("\r\n");
			}
			requestLine = requestLine.replace("\r", "").replace("\n", "");

			if (requestLine.startsWith(boundary)) { // start element
				if (elementName != null && fileContent != null) {
					data.put(elementName, new UploadedFile(
						filename, contentType, 
						Arrays.copyOfRange(fileContent.toByteArray(), 0, fileContent.size() - (useRN ? 2 : 1)))
					);
				} else if (elementName != null && elementValue != null) {
					payloadParser.parseParams(
						(name, value)->data.put(name, value),
						name->data.get(name),
						elementName, 
						elementValue
					);
				}
				elementName = null;
				filename = null;
				contentType = null;
				isValue = false;
				fileContent = null;
				elementValue = null;
			//} else if (requestLine.equals(boundary + "--")) { // close
			} else if (!isValue && requestLine.isEmpty()) { // start element value
				isValue = true;
			} else if (isValue && filename == null) {
				if (elementValue == null) {
					elementValue = "";
				} else {
					elementValue += "\n";
				}
				elementValue += requestLine;
			} else if (isValue) {
				if (fileContent == null) {
					fileContent = new ByteArrayOutputStream();
					// TODO add this
					/*if (requestLine.startsWith("‰")) {
						contentType = requestLine.replace("‰", "");
					}*/
				}
				if (readedData.size() + fileContent.size() > maxUploadFileSize) {
					throw new IOException("Maximal upload file size overflow " + maxUploadFileSize);
				}
				fileContent.write(readedData.toByteArray());
				fileContent.flush();
			} else if (requestLine.startsWith("Content-Disposition: form-data; name=") && !isValue) {
				Matcher m = Pattern.compile(" name=\\\"(([^\\\"])+)\\\"(; (filename=\\\"(([^\\\"])+)\\\")?)?")
						.matcher(requestLine);
				m.find();
				elementName = m.group(1);
				filename = m.group(5);
				if (filename != null && (filename.contains("..") || filename.contains("/") || filename.contains("\\"))) {
					throw new IOException("Filename is probably corrupted " + filename);
				}
			} else if (requestLine.startsWith("Content-Type: ") && !isValue) {
				contentType = requestLine.replace("Content-Type: ", "");
				if (allowedFileTypes.isPresent() && !allowedFileTypes.get().contains(contentType)) {
					throw new IOException("Uploading file content type is not allowed " + contentType);
				}
			}
		}
	}
	/*
	private int readData(StringBuilder payload, Integer length, BufferedReader br, int readedBytes, Function<Character, Boolean> close) throws IOException {
		int readed = readedBytes;
		if (length == null) {
			if (br.ready()) {
		        char value;
		        while((value =(char) br.read()) != -1) {
		            payload.append(value);
					readed += new String(new char[] { value }).getBytes().length;
		            if (!br.ready() || close.apply(value)) {
		               return readed;
		            }
		        }
		    }
		} else {
			while (readed < length) {
				char value = (char)br.read();
	            payload.append(value);
				readed += new String(new char[] { value }).getBytes().length;
				if (!br.ready() && readed != length) {
	            	throw new IOException("Content length not match expecation. Expected: " + length + ", actual: " + readed);
	            }
	            if (close.apply(value)) {
	            	return readed;
	            }
			}
		}
		return readed;
	}*/
}
