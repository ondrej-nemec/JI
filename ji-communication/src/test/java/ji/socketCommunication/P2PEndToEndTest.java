package ji.socketCommunication;

import java.util.Optional;

import ji.socketCommunication.peerToPeer.P2PClient;

public class P2PEndToEndTest {

	public static final char MESSAGE_END = ';';
	public static final char TRANSFER_END = '$';
	
	public static void main(String[] args) {
		try {
			P2PClient client = new P2PClient(
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
