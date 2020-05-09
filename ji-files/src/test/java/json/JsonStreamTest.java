package json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import json.event.Event;
import json.event.EventType;
import json.event.Value;
import json.event.ValueType;
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
		assertEquals(new Event(EventType.DOCUMENT_START, "", new Value<>("", ValueType.NULL), 0), e);
		for (Event expected : events) {
			assertEquals(expected, stream.next());
		}
		e = stream.next();
		assertEquals(new Event(EventType.DOCUMENT_END, "", new Value<>("", ValueType.NULL), 0), e);
	}
	
	public Object[] dataNextTestEventTypes() {
		return new Object[] {
			new Object[] {
				"{\"integer\": 123}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "integer", new Value<>(123, ValueType.INTEGER), 1) 
				}
			},
			new Object[] {
				"{\"negative\": -25}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "negative", new Value<>(-25, ValueType.INTEGER), 1)
				}
			},
			new Object[] {
				"{\"double\": 3.456}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "double", new Value<>(3.456, ValueType.DOUBLE), 1)
				}
			},
			new Object[] {
				"{\"bigNumber\": 4.0e+17}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "bigNumber", new Value<>(4.0e+17, ValueType.DOUBLE), 1)
				}
			},
			new Object[] {
				"{\"string\": \"abc\"}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "string", new Value<>("abc", ValueType.STRING), 1)
				}
			},
			new Object[] {
				"{ \"nullable\": null }",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "nullable", new Value<>(null, ValueType.NULL), 1)
				}
			},
			new Object[] {
				"{ \"booleanTrue\": true}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "booleanTrue", new Value<>(true, ValueType.BOOLEAN), 1)
				}
			},
			new Object[] {
				"{\"booleanFalse\": false}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "booleanFalse", new Value<>(false, ValueType.BOOLEAN), 1)
				}
			},
			new Object[] {
				"{\"empty\": \"\"}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "empty", new Value<>("", ValueType.STRING), 1)
				}
			},
			new Object[] {
				"{\"quote\\\"in\\\"key\": \"\"}",
				new Event[] {
					new Event(EventType.OBJECT_ITEM, "quote\\\"in\\\"key", new Value<>("", ValueType.STRING), 1)
				}
			},
			new Object[] {
				"{\"emptyList\": []}",
				new Event[] {
						new Event(EventType.LIST_START, "emptyList", new Value<>("", ValueType.NULL), 1),
						new Event(EventType.LIST_END, "", new Value<>("", ValueType.NULL), 1)
				}
			},
			new Object[] {
				"{\"emptyObject\": {}}",
				new Event[] {
						new Event(EventType.OBJECT_START, "emptyObject", new Value<>("", ValueType.NULL), 1),
						new Event(EventType.OBJECT_END, "", new Value<>("", ValueType.NULL), 1)
				}
			},
			// TODO more use cases
			new Object[] {
				"{\"list\": [ 1 ]}",
				new Event[] {
						new Event(EventType.LIST_START, "list", new Value<>("", ValueType.NULL), 1),
						new Event(EventType.LIST_ITEM, "", new Value<>(1, ValueType.INTEGER), 2),
						new Event(EventType.LIST_END, "", new Value<>("", ValueType.NULL), 1)
				}
			},
			new Object[] {
				"{\"object\": {\"a\": false}}",
				new Event[] {
						new Event(EventType.OBJECT_START, "object", new Value<>("", ValueType.NULL), 1),
						new Event(EventType.OBJECT_ITEM, "a", new Value<>(false, ValueType.BOOLEAN), 2),
						new Event(EventType.OBJECT_END, "", new Value<>("", ValueType.NULL), 1)
				}
			},
		};
	}

	@Test
	public void testNextIfTextIsEmpty() throws JsonStreamException {
		String testingJson = "";
		StringProvider provider = new StringProvider(testingJson);
		InputJsonStream stream = new InputJsonStream(provider);
		assertEquals(EventType.DOCUMENT_END, stream.next().getType());
	}
	
}
