package utils.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.security.Crypt;
import utils.security.CryptException;

public class CryptTest {
    
    private final String transformation = "AES/ECB/PKCS5Padding";
    private final String securityKey = "thisisa128bitkey";
    private final String algoritm = "AES";

	@Test
	public void testEncryptWorks() throws CryptException {
		String message = "This is a secret message";
		String encrypted = "zC6SULWr8tqA+0qPc5FFv6qljaaVB9GjTAGWWWSZW20=";
		Crypt c = new Crypt(algoritm, transformation, securityKey);
		assertEquals(encrypted, c.encrypt(message));
	}
	
	@Test
	public void testDecrypt() throws CryptException {
		String message = "This is a secret message";
		String encrypted = "zC6SULWr8tqA+0qPc5FFv6qljaaVB9GjTAGWWWSZW20=";
		Crypt c = new Crypt(algoritm, transformation, securityKey);
		assertEquals(message, c.decrypt(encrypted));
	}

}
