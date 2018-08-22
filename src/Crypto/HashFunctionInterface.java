package Crypto;

// interface only for the hash function
public interface HashFunctionInterface {

	// applying has function to an input
	public String applyHash(String input);

	// applying an extended alternative hash
	public String applyHashExt(String input);
	
}
