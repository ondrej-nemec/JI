package socketCommunication.http.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import socketCommunication.http.HttpMethod;
import socketCommunication.http.client.RestApiResponse;
import socketCommunication.http.client.RestApiClient;

@RunWith(JUnitParamsRunner.class)
public class RestApiClientTest {

	@Test
	@Parameters(method = "dataSendReturnExpectedRestApirResponse")
	public void testSendReturnExpectedRestApiResponse(HttpMethod method, boolean doOs) throws IOException {
		/*
		InputStream is = mock(InputStream.class);
		when(is.read()).thenReturn(-1);
		/*/
		InputStream is = getIs();
		//*/
		OutputStream os = mock(OutputStream.class);
		
		HttpURLConnection con = mock(HttpURLConnection.class);
		when(con.getResponseCode()).thenReturn(200);
		when(con.getResponseMessage()).thenReturn("Response message");
		when(con.getInputStream()).thenReturn(is);
		when(con.getOutputStream()).thenReturn(os);

		Properties params = new Properties();
		params.put("key1", "value1");
		params.put("key2", "value2");
		
		Properties header = new Properties();
		header.put("header1", "value1");
		header.put("header2", "value2");
		
		RestApiClient client = getClient("http://example.com");
		RestApiResponse responce = client.send(con, method, header, params);
		assertEquals(new RestApiResponse(200, "Response message", ""), responce);
		
		if (doOs) {
    		verify(con, times(1)).setDoOutput(true);
    		verify(con, times(1)).getOutputStream();
    		verify(os, times(1)).write("key1=value1&key2=value2".getBytes());
    		verify(os, times(1)).flush();
    		verify(os, times(1)).close();
    		verifyNoMoreInteractions(os);
		}
		
		verify(con, times(1)).getInputStream();
		verify(con, times(1)).getResponseCode();
		verify(con, times(1)).getResponseMessage();
		verify(con, times(2)).setRequestProperty(anyString(), anyString());
		verify(con, times(1)).setRequestProperty("header1", "value1");
		verify(con, times(1)).setRequestProperty("header2", "value2");
		verify(con, times(1)).setRequestMethod(method.toString());
		verifyNoMoreInteractions(con);
	}
	
	private InputStream getIs() {
		return new InputStream() {
			
			@Override
			public int read() throws IOException {
				return -1;
			}
		};
	}
	
	public Object[] dataSendReturnExpectedRestApirResponse() {
		return new Object[] {new Object[] {
				HttpMethod.DELETE, true
			},
			new Object[] {
				HttpMethod.PUT, true
			},
			new Object[] {
				HttpMethod.POST, true
			},
			new Object[] {
				HttpMethod.PATCH, true
			},
			new Object[] {
				HttpMethod.GET, false
			}
		};
	}

	@Test
	@Parameters(method = "dataAddParametersReturnExpectedUrl")
	public void testAddParametersReturnExpectedUrl(String expected, HttpMethod method) {
		Properties params = new Properties();
		params.put("key1", "value1");
		params.put("key2", "value2");
		
		RestApiClient client = getClient("http://example.com");
		String actual = client.createUrl("http://example.com", "/uri", method, params);
		assertEquals(expected, actual);
	}
	
	public Object[] dataAddParametersReturnExpectedUrl() {
		return new Object[] {
			new Object[] {
				"http://example.com/uri", HttpMethod.DELETE
			},
			new Object[] {
				"http://example.com/uri", HttpMethod.PUT
			},
			new Object[] {
				"http://example.com/uri", HttpMethod.POST
			},
			new Object[] {
				"http://example.com/uri", HttpMethod.PATCH
			},
			new Object[] {
				"http://example.com/uri?key1=value1&key2=value2", HttpMethod.GET
			}
		};
	}
	
	private RestApiClient getClient(String serverUrl) {
		return new RestApiClient(serverUrl, Optional.empty(), "utf-8", mock(Logger.class));
	}
	
}
