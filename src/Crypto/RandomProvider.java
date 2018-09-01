package Crypto;

import java.util.Random;

import ServiceBus.ServiceBus;
import Utils.Severity;

// classi for generating random numbers
public class RandomProvider implements RandomInterface {

	// generating a random Id as a string
	// non cryptographic random generator
	public String getRandomString() {
		Random rand = new Random();
		int  n = rand.nextInt(60000);
		String Id = ServiceBus.crypto.applyHash(String.valueOf(n));
		return Id;
	}
	
	// getting the cryptographic version of it
	public String getCryptographicRandomString() {
		// not implemented
		 ServiceBus.logger.log("Cryptographic random generator still not implemented in random interface", Severity.WARNING);
		 return "";
	}
	
	
}
