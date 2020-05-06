package json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import json.event.Event;
import json.event.EventType;
import json.event.Value;
import json.providers.StringProvider;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class JsonStreamTest {

	@Test
	@Parameters(method = "dataNextTestEventTypes")
	public void testNextTestEventTypes(String json, Event[] events) throws JsonStreamException {
		StringProvider provider = new StringProvider(json);
		InputJsonStream stream = new InputJsonStream(provider);
		
		Event e = stream.next();
		assertEquals(new Event(EventType.OBJECT_START, "", new Value("", false)), e);
		for (Event expected : events) {
			assertEquals(expected, stream.next());
		}
		e = stream.next();
		assertEquals(new Event(EventType.OBJECT_END, "", new Value("", false)), e);
		e = stream.next();
		assertEquals(new Event(EventType.DOKUMENT_END, "", new Value("", false)), e);
	}
	
	public Object[] dataNextTestEventTypes() {
		return new Object[] {
			new Object[] {
				"{\"integer\": 123}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "integer", new Value("123", false)) }
			},
			new Object[] {
				"{\"negative\": -25}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "negative", new Value("-25", false)) }
			},
			new Object[] {
				"{\"double\": 3.456}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "double", new Value("3.456", false)) }
			},
			new Object[] {
				"{\"bigNumber\": 4.0e+17}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "bigNumber", new Value("4.0e+17", false)) }
			},
			new Object[] {
				"{\"string\": \"abc\"}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "string", new Value("abc", true)) }
			},
			new Object[] {
				"{ \"nullable\": null }",
				new Event[] { new Event(EventType.OBJECT_ITEM, "nullable", new Value("null", false)) }
			},
			new Object[] {
				"{ \"booleanTrue\": true}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "booleanTrue", new Value("true", false)) }
			},
			new Object[] {
				"{\"booleanFalse\": false}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "booleanFalse", new Value("false", false)) }
			},
			new Object[] {
				"{\"empty\": \"\"}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "empty", new Value("", true)) }
			},
			new Object[] {
				"{\"quote\\\"in\\\"key\": \"\"}",
				new Event[] { new Event(EventType.OBJECT_ITEM, "quote\\\"in\\\"key", new Value("", true)) }
			},
			new Object[] {
				"{\"emptyList\": []}",
				new Event[] {
						new Event(EventType.LIST_START, "emptyList", new Value("", false)),
						new Event(EventType.LIST_END, "", new Value("", false))
				}
			},
			new Object[] {
				"{\"emptyObject\": {}}",
				new Event[] {
						new Event(EventType.OBJECT_START, "emptyObject", new Value("", false)),
						new Event(EventType.OBJECT_END, "", new Value("", false))
				}
			},
			// TODO more use cases
		};
	}

	@Test
	public void testNextIfTextIsEmpty() throws JsonStreamException {
		String testingJson = "";
		StringProvider provider = new StringProvider(testingJson);
		InputJsonStream stream = new InputJsonStream(provider);
		assertEquals(EventType.DOKUMENT_END, stream.next());
	}
	
}
