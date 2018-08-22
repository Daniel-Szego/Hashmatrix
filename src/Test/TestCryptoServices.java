package Test;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Assert;
import org.junit.Test;
import Crypto.*;

public class TestCryptoServices {

	@Test
	public void testCryptoServices() {
		
		CryptoProvider prov = new CryptoProvider();

		// Test generating key pairs
		KeyPairString keys =  prov.generateKeyPair();
		assertTrue(keys.privateKey.length() > 0);
		assertTrue(keys.publicKey.length() > 0);
		System.out.println("Public key : " + keys.publicKey);	
		System.out.println("Private key : " + keys.privateKey);	
		
		//Test signing and verifying message
		String trialMessage = "This is a trial message";
		byte[] signature = prov.applySignature(keys.privateKey, trialMessage);
		assertTrue(signature.length > 0);
		assertTrue(prov.verifySignature(keys.publicKey, trialMessage, signature));
		
		//Test encryption and decryption
		String trialPassword = "myP@ssword1";
 		String encryptedMessage = prov.encryptInput(trialPassword, trialMessage);
		String decryptedMessage = prov.decryptInput(trialPassword, encryptedMessage);
 		assertTrue(trialMessage.equals(decryptedMessage));
 		
 		//Hash function
 		String sha256OfTrialMessage = "BF1B06081B63AF2058A2D35DF5FBEE41334C3F24851052F61E97C5B383470E62";
 		String sha512OfTrialMessage = "921DB33F0E2A25DB33A854C9E5727E41034B605C6337087906ABD55D6FE5A63F382E909B750C9AAAA55F4CA0F4F817093EE1556F48A89AE5F3BEA3E206F4E672";
 		String hash256 = prov.applyHash(trialMessage);
 		String hash512 = prov.applyHashExt(trialMessage);
 		assertTrue(sha256OfTrialMessage.equalsIgnoreCase(hash256));
 		assertTrue(sha512OfTrialMessage.equalsIgnoreCase(hash512));
 			
	}
	
	

}
