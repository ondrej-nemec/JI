package common.functions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamLoader {

	/**
	 * Try to load file first from classpath and on failure try dir tree
	 * @param clazz - some class for resource stream
	 * @param path path to file **IMPORTANT** without '/' on path start
	 * @return InputStream from classpath or dir tree
	 * @throws IOException
	 */
	public static InputStream createInputStream(Class<?> clazz, final String path) throws IOException {
		try {
			InputStream is = clazz.getResourceAsStream("/" + path);
			if (is != null) {
				return is;
			}
		} catch (Exception e) { /* ignored */ }
		try {
			InputStream is = new FileInputStream(path);
			return is;
		} catch (Exception e) { /* ignored */ }
		throw new FileNotFoundException(path);
	}
	
}
