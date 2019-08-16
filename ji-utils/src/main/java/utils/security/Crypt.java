package utils.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

	private final Cipher cipher;
	
	private final Key key;
	
	public Crypt(String algoritm, String securityKey, String transfomation) throws CryptException {
		try {
			this.cipher = Cipher.getInstance(transfomation);
			this.key = new SecretKeySpec(securityKey.getBytes("utf-8"), algoritm);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
			throw new CryptException(e);
		}
	}	
	
	public String encrypt(final String message) throws CryptException {
		 try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedMessageBytes = Base64.getEncoder().encode(
					cipher.doFinal(
						message.getBytes()
					)
				);
			return new String(encryptedMessageBytes);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptException(e);
		}	
	}
	
	public String decrypt(final String encryptedString) throws CryptException {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptedMessageBytes = cipher.doFinal(
		    	Base64.getMimeDecoder().decode(
			  		encryptedString.getBytes()
				)
		    );
		    return new String(decryptedMessageBytes);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptException(e);
		}	
	}
	
}
