package ji.socketCommunication.peerToPeer;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import ji.socketCommunication.peerToPeer.Communication;

public class P2PClientTest {

	@Test
	@Ignore
	public void testAcceptAnswerRequest() throws IOException {
		//BufferedInputStream is = mock(BufferedInputStream.class);
		//BufferedOutputStream os = mock(BufferedOutputStream.class);
		BufferedReader br = mock(BufferedReader.class);
		when(br.readLine()).thenReturn("first").thenReturn("second").thenReturn(null).thenReturn(Communication.END);
		
		BufferedWriter bw = mock(BufferedWriter.class);
		
		
		//Speaker s = new Speaker((a)->a, mock(Logger.class));
		// TODO mock socket s.serve(br, bw, is, os);
		fail("Mock socket");
		// reader
		verify(br, times(5)).readLine();
		verifyNoMoreInteractions(br);
		
		// writer
		verify(bw, times(1)).write("first");
		verify(bw, times(1)).write("second");
		verify(bw, times(4)).write(anyString());
		verify(bw, times(2)).write("\r\n");
		verify(bw, times(1)).flush();
		verifyNoMoreInteractions(bw);
	}
	
	
}
