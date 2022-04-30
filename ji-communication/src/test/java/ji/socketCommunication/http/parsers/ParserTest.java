package ji.socketCommunication.http.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.common.structures.MapInit;
import ji.socketCommunication.LoggerImpl;
import ji.socketCommunication.http.Exchange;
import ji.socketCommunication.http.HttpMethod;
import ji.socketCommunication.http.Request;
import ji.socketCommunication.http.Response;
import ji.socketCommunication.http.StatusCode;
import ji.socketCommunication.http.server.UploadedFile;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ParserTest {
	
	private class BufferedInputStremMock extends BufferedInputStream {
		private final String data;
		int index = 0;
		public BufferedInputStremMock(String data) {
			super(mock(InputStream.class));
			this.data = data;
		}
		public synchronized int read() throws IOException {
			if (index == data.length()) {
				return -1;
			}
			return data.charAt(index++);
		}
		@Override
		public synchronized int available() throws IOException {
			return data.length() - index;
		}
	}
	
	private class BufferedOutputStreamMock extends BufferedOutputStream {
		private ByteArrayOutputStream data = new ByteArrayOutputStream();
		public BufferedOutputStreamMock() {
			super(mock(OutputStream.class));
		}
		@Override 
		public void write(int b) throws IOException {
			if (b == '\n') {
				data.write('\r');
			}
			data.write(b);
		}
		@Override
		public void write(byte[] b) throws IOException {
			if (Arrays.equals("\n".getBytes(), b)) {
				data.write('\r');
			}
			data.write(b);
		}
		
		public ByteArrayOutputStream get() { 
			return data; 
		}
	}
	
	@Test
	@Parameters(method = "getData")
	public void testWriteRequest(String filename, Map<String, List<Object>> headers, Function<Exchange, Object> setBody) throws IOException {
		Parser parser = createParser();
		// String expected = Text.get().read(b->ReadText.get().asString(b), getClass().getResource("/parser/requests/" + filename));
		ByteArrayOutputStream expected = readFile("/parser/requests/" + filename);
		try (BufferedOutputStreamMock bos = new BufferedOutputStreamMock()) {
			Request request = new Request(HttpMethod.POST, "/some/url", "HTTP/1.1");
			request.setHeaders(headers);
			
			//request.setBody(body, type);
			setBody.apply(request);
			
			parser.write(request, bos);

			// temporary fix
		//	assertEquals(new String(expected.toByteArray()), new String(bos.get().toByteArray()));
			
			assertTrue(Arrays.equals(expected.toByteArray(), bos.get().toByteArray()));
			// assertEquals(expected, bos.get());
		}
	}

	@Test
	@Parameters(method = "getData")
	public void testWriteResponse(String filename, Map<String, List<Object>> headers, Function<Exchange, Object> setBody) throws IOException {
		// BodyType type, Object body
		Parser parser = createParser();
		ByteArrayOutputStream expected = readFile("/parser/responses/" + filename);
		try (BufferedOutputStreamMock bos = new BufferedOutputStreamMock()) {
			Response response = new Response(StatusCode.OK, "HTTP/1.1");
			response.setHeaders(headers);
			
			setBody.apply(response);
			// response.setBody(body, type);

			parser.write(response, bos);
			
			assertTrue(Arrays.equals(expected.toByteArray(), bos.get().toByteArray()));
		}
	}

	@Test
	@Parameters(method = "getData")
	public void testReadRequest(String filename, Object headers, Function<Exchange, Object> setBody) throws IOException {
		Parser parser = createParser();
		try (InputStream is = getClass().getResourceAsStream("/parser/requests/" + filename);
				BufferedInputStream bis = new BufferedInputStream(is)) {
			Request request= parser.readRequest(bis);
			assertEquals(HttpMethod.POST, request.getMethod());
			assertEquals("/some/url", request.getUri());
			assertEquals("HTTP/1.1", request.getProtocol());
			
			assertEquals(headers, request.getHeaders());
			assertBody(setBody.apply(null), request.getBody(), request.getBodyType());
			// assertEquals(setBody.apply(null), request.getBody());
		}
	}

	@Test
	@Parameters(method = "getData")
	public void testReadResponse(String filename, Map<String, List<Object>> headers, Function<Exchange, Object> setBody) throws IOException {
		Parser parser = createParser();
		try (InputStream is = getClass().getResourceAsStream("/parser/responses/" + filename);
				BufferedInputStream bis = new BufferedInputStream(is)) {
			Response response = parser.readResponse(bis);
			assertEquals(StatusCode.OK, response.getCode());
			assertEquals("HTTP/1.1", response.getProtocol());
			
			assertEquals(headers, response.getHeaders());

			assertBody(setBody.apply(null), response.getBody(), response.getBodyType());
		}
	}
	
	@Test
	public void testParseRequestUrlParameters() throws IOException {
		Parser parser = createParser();
		try (BufferedInputStream bis = new BufferedInputStremMock("PUT /my/uri?param=val&another=aaa HTTP/1.1")) {
			Request request= parser.readRequest(bis);
			assertEquals(HttpMethod.PUT, request.getMethod());
			assertEquals("/my/uri?param=val&another=aaa", request.getUri());
			assertEquals("HTTP/1.1", request.getProtocol());
			
			assertEquals("/my/uri", request.getPlainUri());
			assertEquals(
				new MapInit<String, Object>()
				.append("param", "val")
				.append("another", "aaa")
				.toDictionaryMap(),
				request.getUrlParameters()
			);
			
			assertEquals(new HashMap<>(), request.getHeaders());
			assertNull(request.getBody());
		}
	}
	
	public Object[] getData() {
		ByteArrayOutputStream binaryData = new ByteArrayOutputStream();
		for (int b : new int[] {137, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 
				82, 0, 0, 0, 32, 0, 0, 0, 32, 8, 2, 0, 0, 0, 252, 
				24, 237, 163, 0, 0, 0, 1, 115, 82, 71, 66, 0, 174, 206, 28, 
				233, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, 177, 143, 11, 252, 
				97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, 195, 0, 
				0, 14, 195, 1, 199, 111, 168, 100, 0, 0, 0, 210, 73, 68, 65, 
				84, 72, 75, 237, 148, 49, 14, 194, 48, 12, 69, 29, 142, 208, 133, 
				137, 9, 113, 156, 114, 3, 78, 199, 13, 224, 56, 168, 19, 19, 11, 
				87, 8, 30, 220, 246, 211, 196, 73, 149, 200, 145, 42, 245, 45, 253, 
				170, 37, 127, 187, 249, 169, 243, 222, 147, 37, 7, 121, 154, 177, 220, 
				192, 221, 63, 162, 136, 252, 237, 40, 170, 130, 217, 0, 91, 35, 89, 
				155, 239, 233, 34, 138, 168, 123, 191, 68, 141, 84, 125, 34, 110, 141, 
				221, 153, 240, 141, 24, 104, 227, 51, 137, 210, 26, 202, 55, 88, 76, 
				138, 96, 201, 60, 69, 173, 12, 18, 81, 169, 12, 107, 249, 6, 97, 
				34, 39, 176, 84, 117, 209, 180, 115, 78, 25, 20, 128, 54, 225, 90, 
				219, 255, 217, 237, 6, 89, 204, 13, 34, 41, 114, 206, 137, 226, 171, 
				176, 34, 99, 231, 71, 47, 138, 104, 184, 62, 69, 141, 252, 25, 96, 
				107, 68, 179, 193, 214, 8, 218, 52, 60, 3, 109, 124, 38, 90, 210, 
				198, 103, 176, 180, 199, 52, 203, 108, 144, 72, 100, 180, 20, 38, 114, 
				162, 105, 138, 218, 94, 52, 11, 182, 158, 34, 162, 31, 130, 213, 94, 
				217, 43, 94, 199, 18, 0, 0, 0, 0, 73, 69, 78, 68, 174, 66, 
				96, 130}) {
			binaryData.write(b);
		}
		
		return new Object[] {
			new Object[] {
				"body-empty.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.toMap(),
				createFunction((ex)->{
					return null;
				})
			},
			new Object[] {
				"body-plain-text.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.append("Content-Type", Arrays.asList("plain/text"))
				.append("Content-Length", Arrays.asList("59"))
				.toMap(),
				createFunction((ex)->{
					String res = "Some UTF-8 text: ěščř Сайн уу 你好 أأهلاً";
					if (ex == null) {
						return res;
					}
					ex.setBodyText(res);
					return null;
				})
			},
			new Object[] {
				"body-urlencode.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.append("Content-Type", Arrays.asList("application/x-www-form-urlencoded"))
				.append("Content-Length", Arrays.asList("221"))
				.toMap(),
				createFunction((ex)->{
					Map<String, Object> data = new MapInit<String, Object>()
					.append(
						"list", 
						Arrays.asList(
							"value-list-1", 
							"value-list-2", 
							"value-list-3"
						)
					)
					.append(
						"map",
						new MapInit<>()
						.append("a", "value-map-a")
						.append("b", "value-map-b")
						.toMap()
					)
					.append(
						"maplist",
						new MapInit<>()
						.append(
							"a", 
							Arrays.asList(
								"value-maplist-a-1", 
								"value-maplist-a-2"
							)
						)
						.append(
							"b", 
							Arrays.asList(
								"value-maplist-b-1", 
								"value-maplist-b-2"
							)
						)
						.toMap()
					)
					.toMap();
					if (ex == null) {
						return data;
					}
					ex.setBodyUrlEncoded(data);
					return null;
				})
				
			},
			new Object[] {
				"body-binary.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.append("Content-Type", Arrays.asList("image/x-icon"))
				.append("Content-Length", Arrays.asList("317"))
				.toMap(),
				createFunction((ex)->{
					byte[] res = binaryData.toByteArray();
					if (ex == null) {
						return res;
					}
					ex.setBodyBinary(res);
					return null;
				})
			},
			new Object[] {
				"body-multipart-no-file.txt",
				new MapInit<String, Object>()
				.append("Content-Type", Arrays.asList("multipart/form-data; boundary=item-separator"))
				.append("Content-Length", Arrays.asList("789"))
				.append("Some-header", Arrays.asList("my header value"))
				.toMap(),
				createFunction((ex)->{
					Map<String, Object> res = 
					new MapInit<String, Object>()
					.append(
						"list", 
						Arrays.asList(
							"value-list-1", 
							"value-list-2", 
							"value-list-3"
						)
					)
					.append(
						"map",
						new MapInit<>()
						.append("a", "value-map-a")
						.append("b", "value-map-b")
						.toMap()
					)
					.append(
						"maplist",
						new MapInit<>()
						.append(
							"a", 
							Arrays.asList(
								"value-maplist-a-1", 
								"value-maplist-a-2"
							)
						)
						.append(
							"b", 
							Arrays.asList(
								"value-maplist-b-1", 
								"value-maplist-b-2"
							)
						)
						.toMap()
					)
					.toMap();
					if (ex == null) {
						return res;
					}
					ex.setBodyFormData(res);
					return null;
				})
			},
			new Object[] {
				"body-multipart-with-file.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.append("Content-Type", Arrays.asList("multipart/form-data; boundary=item-separator"))
				.append("Content-Length", Arrays.asList("516"))
				.toMap(),
				createFunction((ex)->{
					Map<String, Object> res = MapInit.create()
					.append("another", "value")
					.append("fileinput", new UploadedFile("filename.xyz", "IMG", binaryData.toByteArray()))
					.toMap();
					if (ex == null) {
						return res;
					}
					ex.setBodyFormData(res);
					return null;
				})
				
			},
			// TODO websocket test
			/*new Object[] {
				"websocket.txt",
				new MapInit<String, Object>()
				.append("Some-header", Arrays.asList("my header value"))
				.toMap(),
				"TODO"
			}*/
		};
	}
	
	private void assertBody(Object expected, Object actual, BodyType type) {
		switch (type) {
			case PLAIN_TEXT_OR_BINARY:
				if (expected instanceof String) {
					assertEquals(expected, new String((byte[])actual));
				} else {
					assertTrue(Arrays.equals((byte[])expected, (byte[])actual));
				}
				break;
			case EMPTY:
				assertNull(expected);
				assertNull(actual);
				break;
			case FORM_DATA:
			case JSON:
			case URLENCODED_DATA:
			default:
				assertEquals(expected, actual);
		}
	}
	
	private Object createFunction(Function<Exchange, Object> setBody) {
		return setBody;
	}
	
	private ByteArrayOutputStream readFile(String file) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(file)) {
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			int v;
			while((v = is.read()) != -1) {
				data.write(v);
			}
			return data;
		}
	}

	private Parser createParser() {
		return new Parser(new LoggerImpl(), 1024, Optional.empty(), ()->"item-separator");
	}
	
}
