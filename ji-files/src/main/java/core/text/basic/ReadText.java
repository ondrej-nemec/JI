package core.text.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ReadText {

	public static void perLine(BufferedReader br, Consumer<String> consumer) throws IOException {
		String line = br.readLine();
		while (line != null) {
			consumer.accept(line);
			line = br.readLine();
		}
	}
	
	public static String asString(BufferedReader br) throws IOException {
		StringBuilder result = new StringBuilder();
		String line = br.readLine();
		if(line != null){
			result.append(line);
			line = br.readLine();
		}
		while(line!=null){
			result.append("\n");
			result.append(line);
			line = br.readLine();
		}
		return result.toString();
	}
	
	public static List<String> asList(BufferedReader br) throws IOException {
		List<String> result = new ArrayList<>();
		String line = br.readLine();
		while(line!=null){
			result.add(line);
			line = br.readLine();
		}
		return result;
	}
	
	//TODO add Table structure
	public static Collection<List<String>> asTable(BufferedReader br, final String split) throws IOException {
		List<List<String>> result = new ArrayList<>();
		String line = br.readLine();
		while(line != null){
			String[] cels = line.split(split);
			List<String> row = new ArrayList<>();
			for(int i = 0; i < cels.length; i++){
				row.add(cels[i]);
			}
			result.add(row);
			line = br.readLine();
		}
		return result;
	}
}
