package Crypto;

// functionalities for symmetric cryptography
public interface SymmetricCryptoInterface {

	// encrypting information with the key
	public String encryptInput(String key, String input);

	// decrypting informtaion with the key
	public String decryptInput(String key, String input);
	
}
