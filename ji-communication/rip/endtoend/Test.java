package endtoend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import common.Logger;
import endtoend.response.BinaryResponse;
import endtoend.response.CssResponse;
import endtoend.response.HtmlResponse;
import endtoend.response.IcoResponse;
import endtoend.response.JsResponse;
import endtoend.response.JsonResponse;
import endtoend.response.NullResponse;
import endtoend.response.Response;
import socketCommunication.LoggerImpl;
import socketCommunication.Servant;
import socketCommunication.Server;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.StatusCode;
import socketCommunication.http.UrlEscape;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.RestApiResponse;

public class Test implements Servant {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
		
	private final Logger logger;
	
	public Test(Logger logger) {
		this.logger = logger;
	}
	
	private Response getResponse(String url) {
		if (url.equals("/")) { // without this works as indexing
			return new HtmlResponse();
		}
		return new BinaryResponse(
				getClass().getResourceAsStream("/endtoend/files" + url)
			);
		// getClass().getResourceAsStream("/endtoend/files/zip.zip")
		/*switch (url) {
		case "/": return new HtmlResponse();
		//case "/style.css": return new CssResponse();
		case "/script.js": return new JsResponse();
		case "/index.php": return new JsonResponse();
		
		case "/style.css": return new BinaryResponse(
				getClass().getResourceAsStream("/endtoend/files/style.css")
			);
		case "/zip.zip": return new BinaryResponse(
				getClass().getResourceAsStream("/endtoend/files/zip.zip")
			);
		case "/favicon.ico": return new BinaryResponse(
				getClass().getResourceAsStream("/endtoend/files/favicon.ico")
			);
		case "/icon.png": return new BinaryResponse(
				getClass().getResourceAsStream("/endtoend/files/icon.png")
			);
		default: return new NullResponse();
		/*
		case "/css": return new CssResponse();
		case "/binary": return new BinaryResponse();
		default:
			return new HtmlResponse();*/
		//}
	}
	
	@Override
	public void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException {
		Properties params = new Properties();
		Properties header = new Properties();
		Properties request = new Properties();
		parseRequest(request, header, params, br);
		
		logger.debug("Request: " + request);
		/*
		RestApiResponse response = createResponce.accept(
				HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase()),
				request.getProperty(URL),
				request.getProperty(FULL_URL),
				request.getProperty(PROTOCOL),
				header,
				params
		);
		*/
		Response response = getResponse(request.getProperty(URL));
		String code = response.getCode().toString();
		logger.debug("Response: " + code);
		
		// write first line
		bw.write(request.getProperty(PROTOCOL));
        bw.write(" ");
        bw.write(code);
        bw.newLine();
		// write heared
        for (String headerLine : response.getHeader()) {
        	bw.write(headerLine);
        	bw.newLine();
        }
		// end of header
        bw.newLine();
		// write context
        
       // bw.write(response.getMessage());
        response.write(bw, os);
        
        bw.newLine();
		bw.flush();
	}
	
	/********* PARSE **************/
	
	private void parseRequest(Properties request, Properties header, Properties params, BufferedReader br) throws IOException {
		// url, method, protocol
		String first = br.readLine();
		parseFirst(request, params, first);
		
        // header
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
        	parseHeaderLine(line, header);
        	line = br.readLine();
        }
        
        // payload
        StringBuilder payload = new StringBuilder();
        while(br.ready()){
        	payload.append((char) br.read());
        }
        parsePayload(params, payload.toString());
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
		//TODO escape payload
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

	public static void main(String[] args) {
		try {
			Server server = new Server(80, 5, 60000, new Test( new LoggerImpl()), "utf-8", new LoggerImpl());
			// Server.create(80, 5, 8000, new Test( new LoggerImpl()), "UTF-8",  new LoggerImpl());
			server.start();
			Thread.sleep(30000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
