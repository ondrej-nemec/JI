package socketCommunication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.OptionChecker;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import common.Logger;
import common.structures.Tuple2;
import core.text.Text;
import core.text.basic.ReadText;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.StatusCode;
import socketCommunication.http.server.RequestParameters;
import socketCommunication.http.server.RestApiResponse;
import socketCommunication.http.server.UploadedFile;

public class WebSocketServerTest {

	public static void main(String[] args) {
		try {
			Server server = new Server(80, 5, 120000, getServant(), Optional.empty(), "UTF-8", new LoggerImpl());
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Servant getServant() {
		return new Servant() {
			
			protected final static String METHOD = "method";
			protected final static String PROTOCOL = "protocol";
			protected final static String URL = "url";
			protected final static String FULL_URL = "full-url";
				
			private final Logger logger = new LoggerImpl();
			
			@Override
			public void serve(Socket socket, String charset) throws IOException {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
		           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
		           	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
		           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
					serve(br, bw, is, os, socket.getInetAddress().toString());
		       } catch(IOException e) {
		    	   throw e;
		       } catch(Exception e) {
		    	   throw new RuntimeException(e);
		       }
			}
			
			protected void serve(
					BufferedReader br, 
					BufferedWriter bw,
					BufferedInputStream is,
					BufferedOutputStream os,
					String clientIp) throws Exception {
				RequestParameters params = new RequestParameters();
				Properties header = new Properties();
				Properties request = new Properties();
				parseRequest(request, header, params, br, is);
				/***********/
				HttpMethod method = HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase());
				String url = request.getProperty(URL);
				String fullUrl = request.getProperty(FULL_URL);
				String protocol = request.getProperty(PROTOCOL);
				logger.debug("Request: " + request);
				
				if (url.equals("/index.html")) {
					sendResponse(RestApiResponse.textResponse(
							StatusCode.OK,
							Arrays.asList(
								"Access-Control-Allow-Origin: *", 
								"Content-Type: text/html; charset=utf-8"
							),
							(writer)->{
								writer.write(Text.get().read((reader)->{
									return ReadText.get().asString(reader);
								}, "index/web-sockets.html"));
							}
						), request, bw, os);
					return;
				}
				if (request.get(URL).equals("/ws")) {
					//System.err.println(String.format("key: '%s'", header.getProperty("Sec-WebSocket-Key")));
					//String key = new String(Base64.getDecoder().decode(header.getProperty("Sec-WebSocket-Key").getBytes()));
					
					
					
					
		        } 
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
					Properties request, Properties header, RequestParameters params, 
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
		        
		        if (request.get(URL).equals("/ws")) {
		        	return;
		        }
		        
		        String type = header.getProperty("Content-Type");
		        if (type != null && type.contains("multipart/form-data")) {
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
			protected void parseFirst(Properties request, RequestParameters params, String first) throws UnsupportedEncodingException {
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
			protected void parsePayload(RequestParameters prop, String payload) throws UnsupportedEncodingException {
				if (payload.isEmpty()) {
					return;
				}
				/*
				String url = URLDecoder.decode(payload, StandardCharsets.UTF_8.toString());
				String[] params = url.split("\\&");
				/*/
				String[] params = payload.split("\\&");
				//*/
				for (String param : params) {
					String[] keyValue = param.split("=");
					if (keyValue.length == 1) {
						parseParams(prop, keyValue[0], "");
						//prop.put(keyValue[0], "");
					} else if (keyValue.length == 2) {
						parseParams(prop, keyValue[0], keyValue[1]);
						//prop.put(keyValue[0], keyValue[1]);
					} else {
				    	logger.warn("Invalid param " + param);
				    }
				}
			}

			private void parseParams(RequestParameters params, String key, String value) throws UnsupportedEncodingException {
				//key = key.replace("[]", "&=").replace("][", "&").replace("[", "&").replace("]", "&");
				key = URLDecoder.decode(key, StandardCharsets.UTF_8.toString());
				key = key.replace("[]", "[=]").replace("[", "&").replace("]", "");
				String[] keys = key.split("\\&");
				value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
				int keyCount = StringUtils.countMatches(key, "&");
				if (keyCount == 0) {
					params.put(key, value);
					return;
				}
				parseParams(params, keys, value);
			}

			private void parseParams(RequestParameters params, String[] keys, String value) throws UnsupportedEncodingException {
				String key = keys[0]; // URLDecoder.decode(keys[0], StandardCharsets.UTF_8.toString());
				Object o = params.getValue(key);
				if (o == null && keys[1].equals("=")) {
					params.put(key, new LinkedList<>());
				} else if (o == null) {
					params.put(key, new HashMap<>());
				}
				parseParams(params.getValue(key), keys, 1, value);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			private void parseParams(Object o, String[] keys, int index, String value) throws UnsupportedEncodingException {
				if (index == keys.length) {
					return;
				}
				
				Object sub = null;
				if (o instanceof Map) {
					// sub = Map.class.cast(o).get(URLDecoder.decode(keys[index], StandardCharsets.UTF_8.toString()));
					sub = Map.class.cast(o).get(keys[index]);
				} else if (o instanceof List) {
					List l = List.class.cast(o);
					if (!l.isEmpty()) {
						sub = l.get(l.size() - 1);
					}
				}
				boolean needInsert = false;
				if (index == keys.length - 1) {
					needInsert = true;
					sub = value;
				} else if (sub == null && keys[index+1].equals("=")) {
					needInsert = true;
					sub = new LinkedList<>();
				} else if (sub == null) {
					needInsert = true;
					sub = new HashMap<>();
				}
				if (needInsert && o instanceof Map) {
					Map.class.cast(o).put(keys[index], sub);
				} else if (needInsert && o instanceof List) {
					List.class.cast(o).add(sub);
				}
				parseParams(sub, keys, index+1, value);
			}
		};
	}
	
}
