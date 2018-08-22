package Crypto;

// crypto provider for merging all the cryptographical functionalities
public class CryptoProvider implements CryptoInterface {
	
	public HashFunctionInterface hashProvider;
	public AsymmetricCryptoInterface asymmProvider;
	public SymmetricCryptoInterface symmProvider;
	
	public CryptoProvider() {
		hashProvider = new SHAProvider();
		asymmProvider = new ECDSAProvider();
		
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
	public KeyPair generateKeyPair() {
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
	
}
