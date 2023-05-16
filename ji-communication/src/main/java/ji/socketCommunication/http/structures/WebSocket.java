package ji.socketCommunication.http.structures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;

import ji.common.structures.Tuple2;

// https://tools.ietf.org/html/rfc6455#section-5.5.1
// https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers#format
// https://stackoverflow.com/questions/18368130/how-to-parse-and-validate-a-websocket-frame-in-java
public class WebSocket {

	private final OutputStream os;
	private final InputStream is;
	private Boolean closed;
	
	private Consumer<String> onMessage;
	private Consumer<IOException> onError;
	
	private final String key;
	private final String origin;
	
	public WebSocket(OutputStream os, InputStream is, Exchange exchange) {
		this.os = os;
		this.is = is;
		this.closed = null;
		
		this.key = exchange.getHeader("Sec-WebSocket-Key") + "";
		this.origin = exchange.getHeader("Origin").toString();
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getKey() {
		return key;
	}
	
	public void close() {
		this.closed = true;
	}

	public void send(String message) throws IOException {
		send(message.getBytes(), false);
	}

	public void send(byte[] content) throws IOException {
		send(content, true);
	}
	
	public boolean isRunning() {
		return closed != null && !closed;
	}
	
	public boolean isClosed() {
		return closed != null && closed;
	}
	
	public boolean isAccepted() {
		return onMessage != null || onError != null;
	}
	
	public void accept(Consumer<String> onMessage, Consumer<IOException> onError) {
		this.onError = onError;
		this.onMessage = onMessage;
	}
	
	public void waitOnMessages() {
		int code = 0;
		closed = false;
	    while(code != 8 && !closed) {
	    	if (code == 9) { // ping
	    		System.err.println("---- PING ----");
	    		// TODO send pong
	    	}
	    	ByteArrayOutputStream b = new ByteArrayOutputStream();
	    	try {
				while(is.available() > 0) {
					int i = is.read();
					b.write(i);
				}
		   		if (b.toByteArray().length > 0) {
		   			Tuple2<String, Integer> r = parse(b.toByteArray());
		   			code = r._2();
		   			onMessage.accept(r._1());
		   		}
			} catch (IOException e) {
				onError.accept(e);
			}
	    }
	    closed = true;
	    // TODO closing ??
	}
	
    /* this is how and header should be made:
     *   - first byte  -> FIN + RSV1 + RSV2 + RSV3 + OPCODE
     *   - second byte -> MASK + payload length (only 7 bits)
     *   - third, fourth, fifth and sixth bytes -> (optional) XOR encoding key bytes
     *   - following bytes -> the encoded (if a key has been used) payload
     *
     *   FIN    [1 bit]      -> 1 if the whole message is contained in this frame, 0 otherwise
     *   RSVs   [1 bit each] -> MUST be 0 unless an extension is negotiated that defines meanings for non-zero values
     *   OPCODE [4 bits]     -> defines the interpretation of the carried payload
     *
     *   MASK           [1 bit]  -> 1 if the message is XOR masked with a key, 0 otherwise
     *   payload length [7 bits] -> can be max 1111111 (127 dec), so, the payload cannot be more than 127 bytes per frame
     *
     * valid OPCODES:
     *   - 0000 [0]             -> continuation frame
     *   - 0001 [1]             -> text frame
     *   - 0010 [2]             -> binary frame
     *   - 0011 [3] to 0111 [7] -> reserved for further non-control frames
     *   - 1000 [8]             -> connection close
     *   - 1001 [9]             -> ping
     *   - 1010 [A]             -> pong
     *   - 1011 [B] to 1111 [F] -> reserved for further control frames
     */
	// https://stackoverflow.com/questions/45015525/c-sharp-websocket-create-masked-frame/67247742#67247742
	private void send(byte[] content, boolean binary) throws IOException {
		if (closed == null || closed) {
			throw new IOException("Connection is closed or not opened yet.");
		}
		// TODO roznodne potrebuje dodelat podle:
		// https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers
		int maxFrameSize = 125;
		for (int i = 0; i < content.length; i+=maxFrameSize) {
			int len = Math.min(maxFrameSize, content.length - i);
			boolean last = len < maxFrameSize;
			boolean first = i == 0;
			byte fByte = 0x0;
			if (last) { // (last && first) || 
				fByte += 0x80; // 1000 XXXX
			}
			if (first) { // (last && first) || 
				if (binary) {
					fByte += 0x2; // XXXX 0002
				} else {
					fByte += 0x1; // XXXX 0001
				}
			}
			byte sByte = (byte)(0x0 + len); // 0x0 i mask - not masked
			os.write(new byte[] {fByte, sByte});
		    os.write(content, i, len);
			os.flush();
		}
	}
	
	// https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers
	// https://stackoverflow.com/questions/18368130/how-to-parse-and-validate-a-websocket-frame-in-java
	protected Tuple2<String, Integer> parse(byte raw[]) {
        // easier to do this via ByteBuffer
        ByteBuffer buf = ByteBuffer.wrap(raw);

        // Fin + RSV + OpCode byte
        byte b = buf.get();
        /*
        boolean fin = ((b & 0x80) != 0);
      //  System.err.println("FIN: " + fin);
        boolean rsv1 = ((b & 0x40) != 0);
        boolean rsv2 = ((b & 0x20) != 0);
        boolean rsv3 = ((b & 0x10) != 0);
        */
        byte opcode = (byte)(b & 0x0F);
        // 0 - continuation
        // 1 - text
        // 2 - binary
        // 8 - end
        // TODO: add control frame fin validation here
        // TODO: add frame RSV validation here
        // Masked + Payload Length
        b = buf.get();
        boolean masked = ((b & 0x80) != 0);
        int payloadLength = (byte)(0x7F & b);
        int byteCount = 0;
        if (payloadLength == 0x7F) {
            // 8 byte extended payload length
            byteCount = 8;
        } else if (payloadLength == 0x7E) {
            // 2 bytes extended payload length
            byteCount = 2;
        }
        // Decode Payload Length
        while (--byteCount > 0) {
            b = buf.get();
            payloadLength |= (b & 0xFF) << (8 * byteCount);
        }
        // TODO: add control frame payload length validation here
        byte maskingKey[] = null;
        if (masked) {
            // Masking Key
            maskingKey = new byte[4];
            buf.get(maskingKey,0,4);
        }
        // TODO not masked? disconnect
        // TODO: add masked + maskingkey validation here
        // Payload itself
        byte[] payload = new byte[payloadLength];
        buf.get(payload,0,payloadLength);

        // Demask (if needed)
        if (masked) {
            for (int i = 0; i < payload.length; i++) {
                payload[i] ^= maskingKey[i % 4];
            }
        }
        return new Tuple2<>(new String(payload), (int)opcode);
    }
	
}
