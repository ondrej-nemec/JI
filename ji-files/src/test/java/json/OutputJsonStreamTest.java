package json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.structures.ThrowingConsumer;
import json.providers.OutputStringProvider;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class OutputJsonStreamTest {

	@Test
	@Parameters(method = "dataOutputJsonStreamReturnsCorrectJson")
	public void testOutputJsonStreamReturnsCorrectJson(
			String json,
			ThrowingConsumer<OutputJsonStream, JsonStreamException> createStream,
			boolean formated) throws JsonStreamException {
		OutputStringProvider provider = new OutputStringProvider();
		OutputJsonStream stream = new OutputJsonStream(provider, formated);
		createStream.accept(stream);
		assertEquals(json, provider.getJson());
	}
	
	public Object[] dataOutputJsonStreamReturnsCorrectJson() {
		return new Object[] {
				new Object[] {
						"{}",
						c((stream)->{
							stream.startDocument();
							stream.endDocument();
						}),
						false
				},
				new Object[] {
						"{\"name\":\"value\"}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectValue("name", "value");
							stream.endDocument();
						}),
						false
				},
				new Object[] {
						"{\"name\":\"value\",\"int\":12,\"special\":null,\"name2\":true}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectValue("name", "value");
							stream.writeObjectValue("int", 12);
							stream.writeObjectValue("special", null);
							stream.writeObjectValue("name2", true);
							stream.endDocument();
						}),
						false
				},
				new Object[] {
						"{\"list\":[\"value1\",\"value2\"]}",
						c((stream)->{
							stream.startDocument();
							stream.writeListStart("list");
							stream.writeListValue("value1");
							stream.writeListValue("value2");
							stream.writeListEnd();
							stream.endDocument();
						}),
						false
				},
				new Object[] {
						"{\"object\":{\"name\":\"value\"}}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectStart("object");
							stream.writeObjectValue("name", "value");
							stream.writeObjectEnd();
							stream.endDocument();
						}),
						false
				},
				new Object[] {
						"{\"object\":{\"name\":\"value\",\"object2\":{\"list\":[12,false,123.4]}}}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectStart("object");
							stream.writeObjectValue("name", "value");
							stream.writeObjectStart("object2");
							stream.writeListStart("list");
							stream.writeListValue(12);
							stream.writeListValue(false);
							stream.writeListValue(123.4);
							stream.writeListEnd();
							stream.writeObjectEnd();
							stream.writeObjectEnd();
							stream.endDocument();
						}),
						false
				},

				new Object[] {
						"{\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.endDocument();
						}),
						true
				},
				new Object[] {
						"{\n"
						+ "  \"name\": \"value\"\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectValue("name", "value");
							stream.endDocument();
						}),
						true
				},
				new Object[] {
						"{\n"
						+ "  \"name\": \"value\",\n"
						+ "  \"int\": 12,\n"
						+ "  \"special\": null,\n"
						+ "  \"name2\": true\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectValue("name", "value");
							stream.writeObjectValue("int", 12);
							stream.writeObjectValue("special", null);
							stream.writeObjectValue("name2", true);
							stream.endDocument();
						}),
						true
				},
				new Object[] {
						"{\n"
						+ "  \"list\": [\n"
						+ "    \"value1\",\n"
						+ "    \"value2\"\n"
						+ "  ]\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.writeListStart("list");
							stream.writeListValue("value1");
							stream.writeListValue("value2");
							stream.writeListEnd();
							stream.endDocument();
						}),
						true
				},
				new Object[] {
						"{\n"
						+ "  \"object\": {\n"
						+ "    \"name\": \"value\"\n"
						+ "  }\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectStart("object");
							stream.writeObjectValue("name", "value");
							stream.writeObjectEnd();
							stream.endDocument();
						}),
						true
				},
				new Object[] {
						"{\n"
						+ "  \"object\": {\n"
						+ "    \"name\": \"value\",\n"
						+ "    \"object2\": {\n"
						+ "      \"list\": [\n"
						+ "        12,\n"
						+ "        false,\n"
						+ "        123.4\n"
						+ "      ]\n"
						+ "    }\n"
						+ "  }\n"
						+ "}",
						c((stream)->{
							stream.startDocument();
							stream.writeObjectStart("object");
							stream.writeObjectValue("name", "value");
							stream.writeObjectStart("object2");
							stream.writeListStart("list");
							stream.writeListValue(12);
							stream.writeListValue(false);
							stream.writeListValue(123.4);
							stream.writeListEnd();
							stream.writeObjectEnd();
							stream.writeObjectEnd();
							stream.endDocument();
						}),
						true
				}
		};
	}
	
	private ThrowingConsumer<OutputJsonStream, JsonStreamException> c(ThrowingConsumer<OutputJsonStream, JsonStreamException> createStream) {
		return createStream;
	}
	
}
