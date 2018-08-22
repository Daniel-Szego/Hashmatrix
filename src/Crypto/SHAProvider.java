package Crypto;

import java.security.MessageDigest;
import java.security.Security;

import Utils.LoggerConsole;
import Utils.Severity;
import ServiceBus.*;

// crypto provider for SHA256 hash function
public class SHAProvider implements HashFunctionInterface {

	public SHAProvider() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	// applying SHA256 hash for the input 
	public String applyHash(String input) {		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			ServiceBus.logger.log(e, Severity.CRITICAL);
		}
		return null;		
	}
		
	public String applyHashExt(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			ServiceBus.logger.log(e, Severity.CRITICAL);
		}
		return null;				
	}
}
