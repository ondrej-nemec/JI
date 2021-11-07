package core.text.basic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import common.functions.Implode;

public class WriteText {
	
	public static WriteText get() {
		return new WriteText();
	}
	
	public void write(BufferedWriter bw, final String string) throws IOException {
		bw.write(string);
		bw.newLine();
		bw.flush();
	}
	
	public void write(BufferedWriter bw, final List<String> list) throws IOException {
		for (String line : list) {
			bw.write(line);
			bw.newLine();
		}
		bw.flush();
	}
	
	public void write(BufferedWriter bw, final List<List<String>> table, final String delimeter) throws IOException {
		for(List<String> line : table) {
			bw.write(Implode.implode(delimeter, line));			
			bw.newLine();
		}
		bw.flush();
	}
}
