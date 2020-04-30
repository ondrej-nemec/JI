package core.text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import common.structures.ThrowingConsumer;

public class Binary {
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	// https://stackoverflow.com/a/9855338
	public static String hexBytesToString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[bytes.length -1 - j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	// https://stackoverflow.com/a/140861
	public static byte[] hexStringToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public static void read(ThrowingConsumer<DataInputStream, IOException> consumer, InputStream stream) throws IOException {
		try (DataInputStream dis = new DataInputStream(stream)) {
			consumer.accept(dis);
		}
	}

	public static void write(ThrowingConsumer<DataOutputStream, IOException> consumer, OutputStream stream) throws IOException {
		try (DataOutputStream dis = new DataOutputStream(stream)) {
			consumer.accept(dis);
		}
	}
}
