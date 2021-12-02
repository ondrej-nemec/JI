package ji.socketCommunication.peerToPeer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import ji.socketCommunication.peerToPeer.Communication;

public class CommunicationTest {

	@Test
	public void testReadMessageReadAllFromBuffer() throws IOException {
		BufferedReader br = mock(BufferedReader.class);		
		when(br.readLine()).thenReturn("first").thenReturn("second").thenReturn(null);
		
		String actual = Communication.readMessage(br);		
		assertEquals("first\r\nsecond\r\n", actual);
		
		verify(br, times(3)).readLine();
		verifyNoMoreInteractions(br);
	}
	
	@Test
	public void testWriteMessageWriteAllDataToBuffer() throws IOException {
		BufferedWriter bw = mock(BufferedWriter.class);
		
		Communication.writeMessage(bw, "first", "second");
		
		verify(bw, times(1)).write("first");
		verify(bw, times(1)).write("second");
		verify(bw, times(4)).write(anyString());
		verify(bw, times(2)).write("\r\n");
		verify(bw, times(1)).flush();
		verifyNoMoreInteractions(bw);
	}
	
}
