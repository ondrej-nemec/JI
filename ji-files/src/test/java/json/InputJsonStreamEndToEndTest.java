package json;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import core.text.Text;
import core.text.basic.ReadText;
import json.event.Event;
import json.providers.InputStringProvider;

public class InputJsonStreamEndToEndTest {
	
	public static void main(String[] args) {
		try {
			new InputJsonStreamEndToEndTest().parse(InputJsonStreamEndToEndTest.class.getResourceAsStream("/json/json-input.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parse(InputStream is) throws IOException, JsonStreamException {
		String json = Text.get().read((br)->{return ReadText.get().asString(br);}, is);
		try (InputJsonStream stream = new InputJsonStream(new InputStringProvider(json));) {
			Event e = stream.next();
			while(!e.isJsonEnd()) {
				System.out.println(getPre(e.getLevel()) + e);
				e = stream.next();
			}
			System.out.println(getPre(e.getLevel()) + e);
		}
	}
	
	private String getPre(int level) {
		StringBuilder pre = new StringBuilder();
		IntStream.range(0, level).forEach((i)->{pre.append("  ");});
		return pre.toString();
	}
}
