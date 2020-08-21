package socketCommunication.http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;

import common.Logger;
import socketCommunication.Servant;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.UrlEscape;
import socketCommunication.http.server.session.Session;
import socketCommunication.http.server.session.SessionCheckTask;
import socketCommunication.http.server.session.SessionStorage;

public class RestApiServer implements Servant {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
		
	private final Logger logger;

	private final RestApiServerResponseFactory createResponce;
	
	private final SessionStorage sessionsStorage;
	
	private final long sessionExpirationTime; // in ms
	private final SessionCheckTask task;
	
	public RestApiServer(
			long sessionExpirationTime,
			RestApiServerResponseFactory response, 
			SessionStorage sessionsStorage,
			Logger logger) {
		this.sessionExpirationTime = sessionExpirationTime;
		this.logger = logger;
		this.createResponce = response;
		this.sessionsStorage = sessionsStorage;
		this.task = new SessionCheckTask(sessionsStorage, logger);
	}
	
	@Override
	public void serve(Socket socket, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
           	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
			serve(br, bw, is, os, socket.getInetAddress().toString()); // + socket.getPort()
       }
	}
	
	//@Override
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
		
		Session session = getSession(header, clientIp, new Date().getTime());
		
		logger.debug("Request: " + request);
		RestApiResponse response = createResponce.accept(
				HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase()),
				request.getProperty(URL),
				request.getProperty(FULL_URL),
				request.getProperty(PROTOCOL),
				header,
				params,
				session
		);
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
        if (!session.isEmpty()) {
			bw.write(
				"Set-Cookie: SessionID="
				+ session.getSessionId()
				+ "; HttpOnly; SameSite=Strict;"
				+ " Max-Age="
				+ (sessionExpirationTime / 1000)
				);
        	bw.newLine();
		}
		// end of header
        bw.newLine();
        
		// write text context
        response.createTextContent(bw);
        bw.newLine();
        bw.flush();
		// write binary context
        response.createBinaryContent(os);
        os.flush();
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

	/*	if (header.get("Content-Type") != null && header.get("Content-Type").toString().contains("multipart/form-data")) {
			// u souboru - kontrola originu
			// Content-Length
			Binary.write((stream)->{
				int count;
				int bufferSize = 32768;
				byte[] buffer = new byte[bufferSize]; // or 4096, or more 8192
				while ((count = bis.read(buffer)) > 0) {
					stream.write(buffer, 0, count);
					System.out.println(count + " " + bufferSize);
					if (count < bufferSize) {
						break;
					}
				}
			}, "test.png");
		} else {*/
			// payload
	        StringBuilder payload = new StringBuilder();
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
	//	}
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
	
	/*********************/

	// TODO test
	protected Session getSession(Properties header, String clientIp, long now) {
		String sessionId = getSessionIdFromCookieHeader(header.get("Cookie"));
		Session session = getSession(sessionId, clientIp, sessionExpirationTime, now);
		if (now > session.getExpirationTime()) {
			sessionsStorage.removeSession(sessionId);
			return Session.empty();
		}
		session.setExpirationTime(now + sessionExpirationTime);
		sessionsStorage.addSession(session);
		return session;
	}
	
	// TODO test
	private Session getSession(String sessionId, String clientIp, long expirationTime, long now) {
		Session ses = sessionsStorage.getSession(sessionId);
		if (sessionId == null || ses == null) {
			return new Session(
					RandomStringUtils.randomAlphanumeric(50), 
					clientIp, 
					now + expirationTime,
					""
			);
		}
		return ses;
	}
	
	protected String getSessionIdFromCookieHeader(Object cookieString) {
		if (cookieString == null) {
			return null;
		}
		String[] cookiesArray = cookieString.toString().split(";");
		for (String cookies : cookiesArray) {
			String[] cookie = cookies.split("=");
			if (cookie.length == 2 && cookie[0].trim().equals("SessionID")) {
				return cookie[1].trim();
			}
		}
		return null;
	}

	@Override
	public void start() {
		task.startChecking();
	}

	@Override
	public void stop() {
		task.stopChecking();
	}

}
