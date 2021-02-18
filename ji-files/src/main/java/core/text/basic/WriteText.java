package core.text.basic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.Implode;

public class WriteText {
	
	public static void write(BufferedWriter bw, final String string) throws IOException {
		bw.write(string);
		bw.newLine();
		bw.flush();
	}
	
	public static void write(BufferedWriter bw, final List<String> list) throws IOException {
		for (String line : list) {
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
	}
	
	public static void write(BufferedWriter bw, final List<List<String>> table, final String split) throws IOException {
		for(List<String> line : table) {
			bw.write(Implode.implode(split, line));			
			bw.newLine();
		}
		bw.flush();
	}
}
