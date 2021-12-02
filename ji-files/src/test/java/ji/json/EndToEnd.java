package ji.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ji.json.JsonReader;
import ji.json.JsonStreamException;
import ji.json.JsonWritter;

public class EndToEnd {

	public static void main(String[] args) {
		
		Map<String, Object> json1 = new HashMap<>();
		List<String> list = Arrays.asList("a", "b", "c");
		Map<Integer, Object> json2 = new HashMap<>();
		json2.put(2, "value");
		json1.put("list", list);
		json1.put("json", json2);
		
		JsonWritter writter = new JsonWritter();
		String text1 = writter.write(json1);
		String text2 = writter.write(list);
		System.out.println(text1);
		System.out.println(text2);
		
		JsonReader reader = new JsonReader();
		try {
		//	Object obj1 = reader.read(text1);
			Object obj2 = reader.read(text2);
		//	System.out.println(obj1 + " " + obj1.getClass());
			System.out.println(obj2 + " " + obj2.getClass());
		} catch (JsonStreamException e) {
			e.printStackTrace();
		}
	}
	
}
