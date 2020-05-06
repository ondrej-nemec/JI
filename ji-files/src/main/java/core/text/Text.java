package core.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import common.structures.ThrowingConsumer;
import common.structures.ThrowingFunction;

public class Text {
			
	/************ read from buffered reader ****************/
	
	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, String path) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, String path, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), charset))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, File file, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, InputStream stream) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, InputStream stream, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset))) {
			return function.apply(br);
		}
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, URL url) throws IOException {
		return read(function, url.openStream());
	}

	public static <T> T read(ThrowingFunction<BufferedReader, T, IOException> function, URL url, String charset) throws IOException {
		return read(function, url.openStream(), charset);
	}
	
	/************** write to buffered writer ***************/
	
	public static void write(ThrowingConsumer<BufferedWriter, IOException> consumer, String path, boolean append) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, append))) {
			consumer.accept(bw);
		}
	}
	
	public static void write(ThrowingConsumer<BufferedWriter, IOException> consumer, String path, String charset, boolean append) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), charset))) {
			consumer.accept(bw);
		}
	}
	
	public static void write(ThrowingConsumer<BufferedWriter, IOException> consumer, OutputStream stream) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream))) {
			consumer.accept(bw);
		}
	}
	
	public static void write(ThrowingConsumer<BufferedWriter, IOException> consumer, OutputStream stream, String charset) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, charset))) {
			consumer.accept(bw);
		}
	}
	
}
