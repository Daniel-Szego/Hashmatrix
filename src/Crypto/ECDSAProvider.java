package Crypto;
import java.security.*;


// class providing eliptic curve cryptography functionalities
public class ECDSAProvider implements AsymmetricCryptoInterface {

	public ECDSAProvider() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	// generating keypair
	public KeyPair generateKeyPair() {
		
	}
	
	// deriving public key from rpivate
	public String derivePublicKey(String privateKey) {
				
	}

	// signing input data
	public byte[] applySignature(String privateKey, String input) {
		
	}
	
	// verifying signature
	public boolean verifySignature(String publicKey, String data, byte[] signature) {
		
	}
	
	
	
}
