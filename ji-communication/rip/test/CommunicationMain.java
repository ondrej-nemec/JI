package test;

import static common.MapInit.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import clientserver.HttpMethod;
import clientserver.server.Servant;
import clientserver.server.Server;
import common.Logger;
import common.UniqueMap;
import test.response.CssResponse;
import test.response.HtmlResponse;
import test.response.IcoResponse;
import test.response.JsResponse;
import test.response.JsonResponse;
import test.response.NullResponse;
import test.response.Response;

public class CommunicationMain implements Servant {
	
	protected final static String METHOD = "method";
	protected final static String PROTOCOL = "protocol";
	protected final static String URL = "url";
	protected final static String FULL_URL = "full-url";
		
	private final Logger logger;

	public CommunicationMain() {
		this.logger = new LoggerImpl();
	}
	
	@Override
	public void serve(BufferedReader br, BufferedWriter bw, BufferedInputStream is, BufferedOutputStream os) throws IOException {
		Properties params = new Properties();
		Properties header = new Properties();
		Properties request = new Properties();
		parseRequest(request, header, params, br);
		
		
		HttpMethod method = HttpMethod.valueOf(request.getProperty(METHOD).toUpperCase());
		String url = request.getProperty(URL);
		String fullUrl = request.getProperty(FULL_URL);
		String protocol = request.getProperty(PROTOCOL);
		
		logger.debug("Request: " + request);
		
		Response response = select(method, url, fullUrl, protocol);
	/*	
		String code = "";
		List<String> responseHeader = new ArrayList<>();
	*/	
		logger.debug("Response: " + response.getCode());
		
		// write first line
		bw.write(request.getProperty(PROTOCOL));
        bw.write(" ");
        bw.write(response.getCode().toString());
        bw.newLine();
		// write heared
        for (String headerLine : response.getHeader()) {
        	bw.write(headerLine);
        	bw.newLine();
        }
		// end of header
        bw.newLine();
		bw.flush();
		// write context
    //    bw.write(response.getMessage());
        response.write(bw, os);
       // bw.newLine();
	//	bw.flush();
	//	os.flush();
	}

	private Response select(HttpMethod method, String url, String fullUrl, String protocol) {
		/*new BufferedWriter(null);
		new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		*/
		System.err.println(url);
		switch (url) {
    		case "/": return new HtmlResponse();
    		case "/style.css": return new CssResponse();
    		case "/script.js": return new JsResponse();
    	//	case "/favicon.ico": return new IcoResponse();
    		case "/index.php": return new JsonResponse();
    		default: return new NullResponse();
		}
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
				prop.put(keyValue[0], "");
			} else if (keyValue.length == 2) {
				prop.put(keyValue[0], keyValue[1]);
			} else {
	    		logger.warn("Invalid param " + param);
	    	}
		}
	}

	public static void main(String[] args) {
		Logger logger = new LoggerImpl();
		try {
			Server server = new Server(987, 5, 60000, new CommunicationMain(), "utf-8", logger);
			
			server.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
