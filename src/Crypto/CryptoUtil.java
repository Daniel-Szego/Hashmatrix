package Crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import State.State;

public class CryptoUtil {
	
	// key to string
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static PrivateKey getPrivateKeyFromString(String keyString) {
		// decode the base64 encoded string
		try{
			byte[] decodedKey = Base64.getDecoder().decode(keyString);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedKey);
			KeyFactory kf = KeyFactory.getInstance("ECDSA");
			PrivateKey privateKey = kf.generatePrivate(privateKeySpec);
			return privateKey;
		}
		catch(Exception e) {
			// error handling
			throw new RuntimeException(e);
		}
	}
	
	public static PublicKey getPublicKeyFromString(String keyString) {
		// decode the base64 encoded string
		try{
			byte[] decodedKey = Base64.getDecoder().decode(keyString);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedKey);
			KeyFactory kf = KeyFactory.getInstance("ECDSA");
			PublicKey publicKey = kf.generatePublic(publicKeySpec);
			return publicKey;
		}
		catch(Exception e) {
			// error handling
			throw new RuntimeException(e);
		}
	}

	
	// getting SHA256 for any given string input
	public static String applySha256(String input){		
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
			throw new RuntimeException(e);
		}
	}
	
	//Applies ECDSA Signature and returns the result ( as bytes ).
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
			Signature dsa;
			byte[] output = new byte[0];
			try {
				dsa = Signature.getInstance("ECDSA", "BC");
				dsa.initSign(privateKey);
				byte[] strByte = input.getBytes();
				dsa.update(strByte);
				byte[] realSig = dsa.sign();
				output = realSig;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return output;
		}
		
		//Verifies a String signature 
		public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
			try {
				Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
				ecdsaVerify.initVerify(publicKey);
				ecdsaVerify.update(data.getBytes());
				return ecdsaVerify.verify(signature);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

}
