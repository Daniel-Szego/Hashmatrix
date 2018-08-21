package Crypto;


// general interface for the cryptogrpahical primitives
// for simple interfaces: keys are strings, signature is byte stream
public interface CryptoInterface {

	public String applyHash(String input);
	
	public byte[] applySignature(String privateKey, String input);
	
	public boolean verifySignature(String publicKey, String data, byte[] signature);
		
}
