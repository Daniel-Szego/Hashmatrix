package Crypto;


// interface only for signing and key generation -> assymetric crypto
public interface AsymmetricCryptoInterface {

	// generating keypair
	public KeyPair generateKeyPair();
	
	// deriving public key from rpivate
	public String derivePublicKey(String privateKey);

	// signing input data
	public byte[] applySignature(String privateKey, String input);
	
	// verifying signature
	public boolean verifySignature(String publicKey, String data, byte[] signature);
	
}
