package Crypto;

// services for generating random numbers
public interface RandomInterface {
	
	// generating a random Id as a string
	// non cryptographic random generator
	public String getRandomString();
	
	// getting the cryptographic version of it
	public String getCryptographicRandomString();
	
}
