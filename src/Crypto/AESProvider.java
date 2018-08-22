package Crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

import ServiceBus.*;

// simple provider supporting AES encryption standard
public class AESProvider implements SymmetricCryptoInterface{
	
	 private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	 private byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	 private IvParameterSpec ivspec = new IvParameterSpec(iv);
	
	// encrypting information with the key
	public String encryptInput(String key, String input) {
		try {
			byte[] keyBytes = this.getBytesFromPassword(key);
			byte[] enrcyptedIntput = this.encrypt(keyBytes, input.getBytes("UTF-8"));
			return new String(Base64.encode(enrcyptedIntput));	
		} catch (Exception ex) {
			// error handling
			ServiceBus.logger.log(ex);
		}
		return null;
	}

	// decrypting informtaion with the key
	public String decryptInput(String key, String input) {
		try {
			byte[] keyBytes = this.getBytesFromPassword(key);
			byte[] decodedValue = Base64.decode(input);
			byte[] decryptedIntput = this.decrypt(keyBytes, decodedValue);
			return new String(decryptedIntput);	
		} catch (Exception ex) {
			// error handling
			ServiceBus.logger.log(ex);
		}
		return null;
	}
	
	protected byte[] encrypt(byte[] key, byte[] plainText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherText The data to decrypt
     */
    protected byte[] decrypt(byte[] key, byte[] cipherText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

        return cipher.doFinal(cipherText);
    }

    // this is not really cryptographically secure
    protected byte[] getBytesFromPassword(String psw) {
    	try {
    	
    		if (psw.length() > 16)
    			return psw.substring(0, 15).getBytes();
    		else {
    			int saltLength = 16 - psw.length();
    			String salt = "";
    			for (int i = 0; i < saltLength; i++){
    				salt += "0";
    			}
    			return (psw + salt).getBytes();
    		}
    	}
    	catch (Exception ex) {
			ServiceBus.logger.log(ex);
    	}
    	return null;
    }
}
