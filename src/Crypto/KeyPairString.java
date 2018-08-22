package Crypto;

// abstract class containing a pair of keys
public class KeyPairString {
	
	public String publicKey;
	public String privateKey;
	
	public KeyPairString(String _publicKey, String _privateKey) {
		this.publicKey = _publicKey;
		this.privateKey = _privateKey;
	}
	
}
