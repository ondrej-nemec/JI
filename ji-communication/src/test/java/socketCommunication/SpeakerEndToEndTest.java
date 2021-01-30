package socketCommunication;

import java.util.Optional;

import socketCommunication.peerToPeer.SpeakerClient;

public class SpeakerEndToEndTest {

	public static final char MESSAGE_END = ';';
	public static final char TRANSFER_END = '$';
	
	public static void main(String[] args) {
		try {
			SpeakerClient client = new SpeakerClient(
					"localhost", 
					10123, 
					15000, // connection timeout
					60000, // read timeout 
					Optional.empty(),
					"UTF-8",
					new LoggerImpl()
			);
			
			client.connect();
			client.communicate((br, bw)->{
				StringBuilder message = new StringBuilder();
				int c = br.read();
				while((char)c != MESSAGE_END && c != -1) {
					message.append((char)c);
					c = br.read();
				}
				System.out.println(message.toString());
				
				/* V1
				bw.write("ahoj" + MESSAGE_END);
				bw.flush();
				StringBuilder message = new StringBuilder();
				char c;
				while((c = (char)br.read()) != MESSAGE_END) {
					message.append(c);
				}
				System.out.println(message);
				bw.write("Sbohem" + MESSAGE_END);
				bw.flush();
				//*/
			});
			client.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
