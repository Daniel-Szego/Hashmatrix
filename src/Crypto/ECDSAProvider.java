package Crypto;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import Utils.*;
import ServiceBus.*;


// class providing eliptic curve cryptography functionalities
public class ECDSAProvider implements AsymmetricCryptoInterface {

	public ECDSAProvider() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	// generating keypair
	public KeyPairString generateKeyPair() {
		try {
			PublicKey publicKey;
			PrivateKey privateKey;
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        KeyPair keyPair = keyGen.generateKeyPair();
	        publicKey = keyPair.getPublic();
	        privateKey = keyPair.getPrivate();
	        KeyPairString toReturn = new KeyPairString(
	        		this.getStringFromKey(publicKey),
	        		this.getStringFromKey(privateKey));
	        return toReturn;
		}catch(Exception e) {
		 	ServiceBus.logger.log(e, Severity.CRITICAL, this);
		}
		return null;
	}
	
	// deriving public key from rpivate
	public String derivePublicKey(String privateKey) {
		try {
			PublicKey publicKey;
			PrivateKey privateKeyInt = this.getPrivateKeyFromString(privateKey);
			
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
		    ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

		    ECPoint Q = ecSpec.getG().multiply(((org.bouncycastle.jce.interfaces.ECPrivateKey) privateKeyInt).getD());

		    ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
		    PublicKey publicKeyGenerated = keyFactory.generatePublic(pubSpec);
		    publicKey = publicKeyGenerated;
		    
		    return this.getStringFromKey(publicKey);	
		}catch(Exception e) {
			ServiceBus.logger.log(e, Severity.CRITICAL, this);
		}
		return null;
	}

	// signing input data
	public byte[] applySignature(String privateKey, String input) {
		PrivateKey privateKeyInt = this.getPrivateKeyFromString(privateKey);
		return this.applyECDSASig(privateKeyInt, input);
	}
	
	// verifying signature
	public boolean verifySignature(String publicKey, String data, byte[] signature) {
		PublicKey publicKeyInt = this.getPublicKeyFromString(publicKey);
		return this.verifyECDSASig(publicKeyInt, data, signature);
	}
		
	// key to string
	protected String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	protected PrivateKey getPrivateKeyFromString(String keyString) {
		// decode the base64 encoded string
		try{
			byte[] decodedKey = Base64.getDecoder().decode(keyString);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedKey);
			KeyFactory kf = KeyFactory.getInstance("ECDSA");
			PrivateKey privateKey = kf.generatePrivate(privateKeySpec);
			return privateKey;
		}
		catch(Exception e) {
			ServiceBus.logger.log(e, Severity.CRITICAL, this);
		}
		return null;
	}
	
	
	protected  PublicKey getPublicKeyFromString(String keyString) {
		// decode the base64 encoded string
		try{
			byte[] decodedKey = Base64.getDecoder().decode(keyString);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedKey);
			KeyFactory kf = KeyFactory.getInstance("ECDSA");
			PublicKey publicKey = kf.generatePublic(publicKeySpec);
			return publicKey;
		}
		catch(Exception e) {
			ServiceBus.logger.log(e, Severity.CRITICAL, this);
		}
		return null;
	}

		
	//Applies ECDSA Signature and returns the result ( as bytes ).
	protected byte[] applyECDSASig(PrivateKey privateKey, String input) {
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
				ServiceBus.logger.log(e, Severity.CRITICAL, this);
			}
			return output;
		}
		
		//Verifies a String signature 
	protected boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
			try {
				Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
				ecdsaVerify.initVerify(publicKey);
				ecdsaVerify.update(data.getBytes());
				return ecdsaVerify.verify(signature);
			}catch(Exception e) {
				ServiceBus.logger.log(e, Severity.CRITICAL, this);
			}
			return false;
		}
	
}
