package Crypto;

// crypto provider for merging all the cryptographical functionalities
public class CryptoProvider implements CryptoInterface {
	
	public HashFunctionInterface hashProvider;
	public AsymmetricCryptoInterface asymmProvider;
	public SymmetricCryptoInterface symmProvider;
	public RandomInterface randomProvider;
	
	public CryptoProvider() {
		hashProvider = new SHAProvider();
		asymmProvider = new ECDSAProvider();
		symmProvider = new AESProvider();
		randomProvider = new RandomProvider();
	}
	
	// applying has function to an input
	public String applyHash(String input) {
		return hashProvider.applyHash(input);
	}

	// applying an extended alternative hash
	public String applyHashExt(String input) {
		return hashProvider.applyHashExt(input);
	}
	
	
	// generating keypair
	public KeyPairString generateKeyPair() {
		return asymmProvider.generateKeyPair();
	}
	
	// deriving public key from rpivate
	public String derivePublicKey(String privateKey) {
		return asymmProvider.derivePublicKey(privateKey);
	}

	// signing input data
	public byte[] applySignature(String privateKey, String input) {
		return asymmProvider.applySignature(privateKey, input);		
	}
	
	// verifying signature
	public boolean verifySignature(String publicKey, String data, byte[] signature) {
		return asymmProvider.verifySignature(publicKey, data, signature);			
	}	
	
	// encrypting information with the key
	public String encryptInput(String key, String input) {
		return symmProvider.encryptInput(key, input);
	}

	// decrypting informtaion with the key
	public String decryptInput(String key, String input) {
		return symmProvider.decryptInput(key, input);
	}
	
	// generating a random Id as a string
	// non cryptographic random generator
	public String getRandomString() {
		return randomProvider.getRandomString();
	}
	
	// getting the cryptographic version of it
	public String getCryptographicRandomString() {
		return randomProvider.getCryptographicRandomString();
	}	

}
