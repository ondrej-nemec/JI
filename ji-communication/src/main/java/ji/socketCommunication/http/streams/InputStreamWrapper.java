package ji.socketCommunication.http.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper extends FilterInputStream {
	
	private boolean isClosed = false;

	public InputStreamWrapper(InputStream is) {
		super(is);
	}
	
	@Override
	public int read() throws IOException {
		if (isClosed) {
			return -1;
		}
		return super.read();
	}
	
	public boolean isClosed() {
		return isClosed;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		this.isClosed = true;
	}
	
}
