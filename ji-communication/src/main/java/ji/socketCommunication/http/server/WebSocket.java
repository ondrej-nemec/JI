package ji.socketCommunication.http.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.commons.codec.digest.DigestUtils;

import ji.common.structures.Tuple2;

// https://tools.ietf.org/html/rfc6455#section-5.5.1
// https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers#format
// https://stackoverflow.com/questions/18368130/how-to-parse-and-validate-a-websocket-frame-in-java
public class WebSocket {

	private final OutputStream os;
	private final InputStream is;
	private final Properties requestHeaders;
	private Boolean closed;
	
	public WebSocket(OutputStream os, InputStream is, Properties requestHeaders) {
		this.os = os;
		this.is = is;
		this.requestHeaders = requestHeaders;
		this.closed = null;
	}
	
	protected List<String> getHeaders() {
		/*
    	System.err.println("Upgrade: " + header.get("Upgrade"));
		System.err.println("Version: " + header.get("Sec-WebSocket-Version"));
		System.err.println("Key: " + header.get("Sec-WebSocket-Key"));
		System.err.println("Extension: " + header.get("Sec-WebSocket-Extensions"));
		System.err.println("Origin: " + header.get("Origin"));
		*/
		return Arrays.asList(
	    	"Upgrade: websocket",
	    	"Connection: Upgrade",
	    	"Sec-WebSocket-Accept: "
	    	+ new String(
	    		Base64.getEncoder().encode(DigestUtils.sha1(
	    			requestHeaders.getProperty("Sec-WebSocket-Key")
	    			+ "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
	    		))
	    	)
    	);
	}
	
	public void close() {
		this.closed = true;
	}

	public void send(String message) throws IOException {
		if (closed == null || closed) {
			throw new IOException("Connection is closed or not opened yet.");
		}
		for (ByteArrayOutputStream byteOs : parse(0x0, message)) {
			os.write(byteOs.toByteArray());
		}
		os.flush();
	}
	
	public boolean isRunning() {
		return closed != null && !closed;
	}
	
	public boolean isClosed() {
		return closed != null && closed;
	}
	
	protected void accept(Consumer<String> onMessage, Consumer<IOException> onError) {
		int code = 0;
		closed = false;
	    while(code != 8 && !closed) {
	    	if (code == 9) { // ping
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
	private List<ByteArrayOutputStream> parse(int code, String text) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(text.getBytes());
		int off = 0;
		List<ByteArrayOutputStream> result = new LinkedList<>();
		while (is.available() > 0) {
			int len = Math.min(127, is.available());
			byte[] payload = new byte[len];
			is.read(payload, off, len);
			off += len;
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			result.add(output);
			 // in our case the first byte will be 10000001 (129 dec = 81 hex).
		    // the length is going to be (masked)1 << 7 (OR) 0 + payload length.
		    byte[] header = new byte[] { (byte)0x81, (byte)(code + len) };
		    output.write(header);
		    output.write(payload);
		    /* 
		    byte[] maskKey = new byte[4];
		    if(masked) {
		        // but if needed, let's create it properly.
		        Random rd = new Random();
		        rd.NextBytes(maskKey);
		    }
		    // this is going to be the whole frame to send.
		    byte[] frame = new byte[header.length + 0 + payload.length];
		    // add the header.
		    Array.Copy(header, frame, header.Length);
		    // add the mask if necessary.
		    if(maskKey.length > 0) {
		        Array.Copy(maskKey, 0, frame, header.Length, maskKey.Length);
		        // let's encode the payload using the mask.
		        for(int i = 0; i < payload.Length; i++) {
		            payload[i] = (byte)(payload[i] ^ maskKey[i % maskKey.length]);
		        }
		    }
		    // add the payload.
		    Array.Copy(payload, 0, frame, header.Length + (masked ? maskKey.Length : 0), payload.Length);
		    */
		}
		return result;
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
